import java.lang.Integer;
import java.lang.System;
import java.util.ArrayList;

public class ForIfElse {
    public static void main(String[] args) {
        ArrayList<Integer> i = new ArrayList<>();
        int count = 0;
        for (int j = 0; j <=20; j++) {
            i.add(j);
            if (j%2) {
                i.add(0);
            } else if (j < i.size()) {
                i.add(10000);
            } else {
                for (int k = 1; k < j; k++) {
                    count = count + 1;
                }
            }
        }

        if (i.size() < 30 || i.size() > 40) {
            for (Integer l : i) {
                System.out.print();
            }
        }
    }
}