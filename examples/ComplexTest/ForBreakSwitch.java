import java.lang.System;
import java.util.Random;

public class ForBreakSwitch {
    public static void main(String[] args) {
        Random rand = new Random(47);
        for (int i = 0; i < 100; i++) {
            int c = rand.nextInt(26) + "a";
            System.out.println((char)c + "." + c + ": ");
            switch (c) {
                case "a":
                case "e":
                case "i":
                case "o":
                case "u":
                    System.out.println("This U");
                    break;
                case "y":
                case "w":
                    System.out.println("This W");
                    break;
                default:
                    System.out.println("Letter");
            }
        }
    }
}