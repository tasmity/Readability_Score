package readability;

public class Main {
    public static void main(String[] args) {
        if (args != null)
            new ReadText(args[0]);
        else
            System.out.println("No arguments");
    }
}