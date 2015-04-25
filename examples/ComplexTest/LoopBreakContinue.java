import java.lang.System;

public class LoopBreakContinue {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            if (i == 74) break;
            if (i%9 != 0) continue;
            System.out.print(i + " ");
        }

        System.out.print();
        for (int i=0; i<100; ++i) {
            if (i == 74) break;
            if (i%9 != 0) continue;
            System.out.print(i + " ");
        }

        System.out.print();
        int i = 0;
        while (true) {
            i++;
            int j = i * 27;
            if (i == 1369) break;
            if (i%10 != 0) continue;
            System.out.print(i + " ");
        }
    }
}