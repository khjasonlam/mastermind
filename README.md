# Mastermind

A Java Swing implementation of the Mastermind code-breaking game.

## Folder structure

```
mastermind/
├── .vscode/           # Editor settings (lint, format, Java)
├── src/
│   └── main/
│       ├── java/
│       │   └── mastermind/   # Java sources
│       └── resources/
│           └── images/      # PNG assets (loaded from classpath)
├── bin/               # Compiled classes + copied resources (created on build)
└── build.sh           # Build script for JAR and macOS app
```

- **Source**: `src/main/java/mastermind/` — common Java layout (Maven/Gradle style).
- **Resources**: `src/main/resources/images/` — images are loaded via classpath (`getResource("/images/...")`), so the app works from the command line and from a JAR.

## Build & run

### Command line

```bash
javac -sourcepath src/main/java -d bin src/main/java/mastermind/*.java
cp -r src/main/resources/* bin/
java -cp bin mastermind.Main
```

### macOS app and installer (.pkg)

To build a **double-clickable app** and an **installer package**:

```bash
./build.sh          # Build Mastermind.app into build/
./build.sh --pkg    # Also build Mastermind.pkg (installs to /Applications)
```

- **`build/Mastermind.app`** — run with `open build/Mastermind.app` or double-click in Finder.
- **`build/Mastermind.pkg`** — run the .pkg to install Mastermind into `/Applications`. Requires a JDK/JRE on the target Mac (e.g. Azul Zulu or Oracle JDK).

The app uses the system Java (`/usr/libexec/java_home` or `JAVA_HOME`).

## Entry point

`mastermind.Main` — opens the main window and launch menu.
