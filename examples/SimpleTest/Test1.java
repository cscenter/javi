import java.lang.System;

public class Test1 {
    public void method1() {
        int l = 0;
    }

    public static void main(String[] args) {
        for (int i = 0; i <10; i++) {
            i += 1;
        }
    }
}

class Test2 {
    public void method1() {
        int k = 5;
        while(k < 10) {
            System.out.println(k);
            k++;
        }
    }

    private void method2() {
        int k = 5;
    }
}