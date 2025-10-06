package tools;

import java.util.Scanner;

public class Inputter {

    private static final Scanner sc = new Scanner(System.in);

    public static String getString(String msg) {
        System.out.print(msg);
        return sc.nextLine().trim();
    }

    public static int getInt(String msg, int min, int max) {
        while (true) {
            try {
                System.out.print(msg);
                int n = Integer.parseInt(sc.nextLine());
                if (n < min || n > max) {
                    throw new Exception();
                }
                return n;
            } catch (Exception e) {
                System.out.println("Invalid int [" + min + ".." + max + "]");
            }
        }
    }
}
