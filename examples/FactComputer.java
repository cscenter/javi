
public class FactComputer
{
    public static void main(String[] args) {
        try {
            int x = Integer.parseInt(args[0]);
            System.out.println(x + "! = " + Factorial.factorial(x));
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Argument not found");
        }
        catch (NumberFormatException e) {
            System.out.println("Argument must be integer");
        }
        catch (IllegalArgumentException e) {
            System.out.println("Bad argument");
        }
    }
}
