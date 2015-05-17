import java.lang.System;

public class MethodOverloading {
    public void method1(String s) {
        int k = 5;
        System.out.print(k + s);
    }

    public void method1(int i) {
        if (i < 10) {
            System.out.print(i);
        } else {
            i++;
            System.out.println(i);
            System.out.print(i > 10);
        }
    }

    public void method1(String s, int i, int k) {
        if (i < k) {
            System.out.print(s);
        }
    }

    public void method1() {
        System.out.print("Hello!");
    }
}