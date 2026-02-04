#!/usr/bin/env bash
# Build Mastermind: compile, JAR, .app bundle, and optionally .pkg installer.
set -e

APP_NAME="Mastermind"
JAR_NAME="Mastermind.jar"
PKG_NAME="Mastermind.pkg"
BUILD_DIR="build"
APP_BUNDLE="$BUILD_DIR/$APP_NAME.app"

echo "==> Cleaning $BUILD_DIR..."
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"

echo "==> Compiling Java..."
javac -sourcepath src/main/java -d bin src/main/java/mastermind/*.java

echo "==> Copying resources to bin..."
cp -r src/main/resources/* bin/

echo "==> Building runnable JAR..."
# JAR needs mastermind/*.class and images/*.png for getResource("/images/...")
jar cfe "$BUILD_DIR/$JAR_NAME" mastermind.Main -C bin mastermind -C bin images

echo "==> Creating macOS app bundle..."
mkdir -p "$APP_BUNDLE/Contents/MacOS"
mkdir -p "$APP_BUNDLE/Contents/Resources/Java"
cp "$BUILD_DIR/$JAR_NAME" "$APP_BUNDLE/Contents/Resources/Java/"

cat > "$APP_BUNDLE/Contents/MacOS/launcher" << 'LAUNCHER'
#!/bin/bash
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
APP_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
JAR="$APP_ROOT/Contents/Resources/Java/Mastermind.jar"

if [ -n "$JAVA_HOME" ]; then
  JAVA="$JAVA_HOME/bin/java"
else
  JAVA_HOME="$(/usr/libexec/java_home 2>/dev/null)" && JAVA="$JAVA_HOME/bin/java"
fi
[ -z "$JAVA" ] && JAVA="java"

exec "$JAVA" -jar "$JAR"
LAUNCHER
chmod +x "$APP_BUNDLE/Contents/MacOS/launcher"

cat > "$APP_BUNDLE/Contents/Info.plist" << 'PLIST'
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
	<key>CFBundleExecutable</key>
	<string>launcher</string>
	<key>CFBundleIdentifier</key>
	<string>com.mastermind.game</string>
	<key>CFBundleName</key>
	<string>Mastermind</string>
	<key>CFBundleDisplayName</key>
	<string>Mastermind</string>
	<key>CFBundlePackageType</key>
	<string>APPL</string>
	<key>CFBundleShortVersionString</key>
	<string>1.0</string>
	<key>CFBundleVersion</key>
	<string>1</string>
	<key>NSHighResolutionCapable</key>
	<true/>
	<key>NSHumanReadableCopyright</key>
	<string>Mastermind</string>
</dict>
</plist>
PLIST

echo "==> Built $APP_BUNDLE"

if [ "$1" = "--pkg" ]; then
  echo "==> Building installer .pkg..."
  PKG_ROOT="$BUILD_DIR/pkg-root"
  mkdir -p "$PKG_ROOT/Applications"
  cp -r "$APP_BUNDLE" "$PKG_ROOT/Applications/"
  pkgbuild --identifier com.mastermind.game \
           --version 1.0 \
           --root "$PKG_ROOT" \
           --install-location / \
           "$BUILD_DIR/$PKG_NAME"
  echo "==> Built $BUILD_DIR/$PKG_NAME (install to /Applications)"
else
  echo "==> Run with --pkg to also create an installer .pkg"
fi

echo "==> Done. Run the app: open $APP_BUNDLE"
