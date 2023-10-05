import java.util.Scanner;
import MenuItems.ModifyCustomer;
import MenuItems.ModifyOrder;
import MenuItems.NewCustomerOrder;
import MenuItems.OldCustomerOrder;

import java.util.InputMismatchException;

public class Menu {

    //  This is real start point of the program

    public static void main(Scanner scanner) {

        int choice = 0;

                
            
            boolean exitloop = false;

    while (!exitloop) {
        try {
        System.out.println("\nChoose an option:");
        System.out.println("\n1. Create new order for existing customer");
        System.out.println("\n2. Create new order for new customer");
        System.out.println("\n3. Modify existing order");
        System.out.println("\n4. Modify customer data");
        System.out.println("\n5. Exit program\n");
        choice = scanner.nextInt();

        scanner = new Scanner(System.in);

        switch (choice) {
            case 1:           
                System.out.println("\nYou selected Option 1.");
                try {
                    OldCustomerOrder.main(scanner);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                System.out.println("\nYou selected Option 2.");
                try {
                    NewCustomerOrder.main(scanner);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                System.out.println("\nYou selected Option 3.");
                try {
                    ModifyOrder.main(scanner);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                System.out.println("\nYou selected Option 4.");
                try {
                    ModifyCustomer.main(scanner);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 5:
            exitloop = true;
            System.out.println("\n\nYou have ended the program.\n\n");
                break;
            default:
                System.out.println("\nInvalid input. Please choose a valid option.\n");
                break;
        }
    }
           
      catch (InputMismatchException e) {
            System.out.println("\nInvalid input. Please enter a valid option.\n");
        }
        scanner.nextLine();
        }   
    }
}
    