import java.lang.Exception;
import java.lang.String;
import java.util.ArrayList;

public class TrySimpleTest {
    public static void main(String[] args) {
        try {
            int i = 0;
            ArrayList<String> s = new ArrayList<>();
        } catch (Exception e) {
            String st = "Error";
        }
        finally {
            int i = 100;
        }
    }
}