import java.util.Scanner;

public class Main {


        //  This was made so that I could safely close the scanner

        static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        Menu.main(scanner);
        scanner.close();

    }
    
}
