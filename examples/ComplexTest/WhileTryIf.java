import java.lang.*;
import java.lang.Exception;
import java.lang.NullPointerException;
import java.util.ArrayList;

public class WhileTryIf {
    public static void main(String[] args) {
        try {
            ArrayList<Integer> in = new ArrayList<>();
            for (int i = 0; i <=100; i++) {
                if (i+23 % 2) {
                    in.add(i);
                } else {
                    in.remove(i);
                }
            }

            int k = 100;
            while (k > 20) {
                k--;
            }
        } catch (NullPointerException e) {
            e.printStackTrace(System.err);
        }
    }
}