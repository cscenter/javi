public class ReturnSimpleTest {
    String g;

    public static String main(String[] args) {
        int i = 0;
        if (i != 0) {
            g = "sdfsd";
            return "Hello Bad";
        }
        int a = 0;
        a = 5;
        return "Hello";
    }
}