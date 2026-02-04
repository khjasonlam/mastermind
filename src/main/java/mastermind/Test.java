package mastermind;

import java.util.Random;
import java.util.Scanner;

class Test {

    public static void main(String[] args) {
        String[] ans = new String[4];
        String[] amp = new String[4];
        String[] check = new String[4];
        int i, j;

        for (i = 0; i < 4; i++) {
            Random rand = new Random();
            int num = rand.nextInt(6);

            switch (num) {
                case 0:
                    ans[i] = "R";
                    break;
                case 1:
                    ans[i] = "O";
                    break;
                case 2:
                    ans[i] = "Y";
                    break;
                case 3:
                    ans[i] = "G";
                    break;
                case 4:
                    ans[i] = "B";
                    break;
                case 5:
                    ans[i] = "V";
                    break;
            }
            if (i > 0) {
                for (j = 0; j < i; j++) {
                    if (ans[j].equals(ans[i])) {
                        i--;
                    }
                }
            }
        }

        int count = 1;
        Scanner sc = new Scanner(System.in);
        while ((count <= 10)) {
            System.out.println("R: Red, O: Orange, Y: Yellow, G: Green, B: Blue, V: Violet");
            for (i = 0; i < 4; i++) {
                switch (i) {
                    case 0:
                        System.out.print("What is the 1st colour	: ");
                        break;
                    case 1:
                        System.out.print("What is the 2nd colour	: ");
                        break;
                    case 2:
                        System.out.print("What is the 3rd colour	: ");
                        break;
                    case 3:
                        System.out.print("What is the 4th colour	: ");
                        break;
                }
                String guess = sc.next();
                guess = guess.toUpperCase();
                if ((guess.equals("R")) || (guess.equals("O")) || (guess.equals("Y")) || (guess.equals("G"))
                        || (guess.equals("B")) || (guess.equals("V"))) {
                    amp[i] = guess;
                } else {
                    System.out.println();
                    System.out.println("ERROR! Invalid Input!!");
                    System.out.println("Please try again!");
                    System.out.println();
                    i--;
                }
                for (j = 0; j < i; j++) {
                    if (amp[i].equals(amp[j])) {
                        System.out.println();
                        System.out.println("ERROR! Colour must be distinct!!");
                        System.out.println("Please try again!");
                        System.out.println();
                        i--;
                    }
                }
            }

            String judge = null;
            for (i = 0; i < 4; i++) {
                for (j = 0; j < 4; j++) {
                    if ((ans[i].equals(amp[j])) && (i == j)) {
                        judge = "true";
                        break;
                    } else if (ans[i].equals(amp[j])) {
                        judge = "false";
                        break;
                    } else {
                        judge = "null";
                    }
                }
                switch (judge) {
                    case "true":
                        check[i] = "+";
                        break;
                    case "false":
                        check[i] = "-";
                        break;
                    default:
                        check[i] = "";
                        break;
                }
            }

            System.out.println();
            System.out.print("Attempt " + count + " : ");
            for (i = 0; i < 4; i++) {
                System.out.print(amp[i]);
            }

            System.out.println();
            for (i = 0; i < 4; i++) {
                System.out.print(check[i]);
            }
            System.out.println();
            if ((check[0].equals("+")) && (check[1].equals("+")) && (check[2].equals("+")) && (check[3].equals("+"))) {
                System.out.println("Well Done!! You finished in " + count + " time(s).");
                System.out.print("The answer is ");
                for (i = 0; i < 4; i++) {
                    System.out.print(ans[i]);
                }
                break;
            } else if (count > 0) {
                System.out.println("Please try again!! " + (10 - count) + " guess(es) left.");
                System.out.println();
            } else {
                System.out.println("GAMEOVER!");
                System.out.print("The answer is ");
                for (i = 0; i < 4; i++) {
                    System.out.print(ans[i]);
                }
            }
            count++;
        }
        sc.close();
    }
}
