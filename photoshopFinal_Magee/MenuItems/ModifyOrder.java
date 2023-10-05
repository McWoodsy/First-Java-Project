package MenuItems;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import Classes.Item;
import Classes.OrderManager;
import Classes.ShoppingCart;
    
    public class ModifyOrder {
        
        static ShoppingCart shoppingCart = new ShoppingCart();
        static Item item = new Item();
        static OrderManager orderManager = new OrderManager();
        static final String jsonFilePath = "JSON\\customers.json";
        static LocalDate orderDate = LocalDate.now();
        static LocalDateTime orderDateTime = LocalDateTime.now();

    public static void main(Scanner scanner) {
                
        boolean exitloop = false;

    do {

        List<Item> catalog = item.csvReader(); 
        List<Item> newChosenItems = new ArrayList<Item>();
        List<String> newAmounts = new ArrayList<String>();
        boolean checker = false;
        String orderNumber = "";
        //  Pick target order and action to take
        System.out.println("\nPlease enter the order number of the order you would like to modify: ");

    while (!checker) {
        orderNumber = scanner.nextLine();
        checker = orderManager.isPresent(orderManager.readWholeJson(), orderNumber);
        if (!checker) {
            System.out.println("Invalid input or no order found. Try again.");
        }
        else if (checker) {
            break;
        }       
    }
        System.out.println("\nWould you like to:\t\t1. Add/remove items\t2. Modify info?");
        int branch = scanner.nextInt();
        scanner.nextLine();
        if (branch == 1) {
            for (Item item : catalog) {
                System.out.println("ID: " + item.getId() + " ---- " + item.getName());
            }
            //  As in OldCustomerOrder.java, user inputs item id/name, then a space, then the amount of that item needed
            orderManager.addOrderItems(orderNumber, scanner, catalog, newChosenItems, newAmounts, orderManager);
            System.out.println("The following items have been added to the order:");
            int i = 0;
            for (Item item: newChosenItems) {
                System.out.println(newAmounts.get(i) + " x " + item.getName());
                i++;
            }
        }
        if (branch == 2) {
            //  Here there is some argument validation, then it rewrites to JSON and prints brief summary of the change
            orderManager.changeOrderInfo(scanner, orderNumber, orderManager);
        }
    //  User gets option to repeat the process or return to the main program
    System.out.println("\nTo return to the main menu type 1 and hit enter\nTo modify more type 2 and hit enter");
    boolean choice = false;

    while (!choice) {
    int  input = scanner.nextInt();
   // scanner.nextLine(); // This kills a lingering newline that was causing problems on the second run of this sub-program



        if (input == 1) {
            exitloop = true;
            choice =true;
        } 
        else if (input ==2) {
            scanner.nextLine();
            choice = true;
        }
        else {
            System.out.println("Invalid input.");
            
        }}       
        } while (!exitloop);
    }
}   