public class BreakLabelSimpleTest {
    public static void main(String[] args) throws Exception {
        label1:
        for (;;) {
            int k = 5;
            break label1;
        }
    }
}