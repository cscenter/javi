import java.awt.*;
import java.awt.event.*;
import java.lang.Override;
import java.lang.Runnable;
import java.lang.Thread;
import javax.swing.*;
import javax.swing.text.AbstractDocument;

public class Class4 {
    public static void main(String[]args){
        JFrame frame = new JFrame("AnonDemo2");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void method2() {
        List list = new ArrayList();
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                int a = ((Integer) o1).intValue();
                int b = ((Integer) o2).intValue();
                return a < b ? 1 : a == b ? 0 : -1;
            }
        });

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (;;) {}
            }
        });
    }

    class A {
        void g() {
            Class4 c= new class4() {
                void method1() {
                }
            };
    }

        public Content content() {
            return new Content() {
                private int i = 11;
                public int value() {
                    return i;
                }
            };
        }
    }
}
