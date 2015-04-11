public class ReturnSimpleTest {
    String g;

    public static String main(String[] args) {
        int i = 0;
        if (i != 0) {
            return "Hello Bad";
        }
        int a = 0;
        return "Hello";
    }
}