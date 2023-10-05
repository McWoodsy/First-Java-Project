package MenuItems;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;
import Classes.Customer;
import Classes.Item;
import Classes.OrderManager;
import Classes.ShoppingCart;

public class ModifyCustomer {        
    
        static ShoppingCart shoppingCart = new ShoppingCart();
        static Item item = new Item();
        static OrderManager orderManager = new OrderManager();
        static final String jsonFilePath = "JSON\\customers.json";
        static LocalDate orderDate = LocalDate.now();
        static LocalDateTime orderDateTime = LocalDateTime.now();

    public static void main(Scanner scanner) {
    
        boolean exitloop = false;

        do{
        
        Customer oldCustomer = new Customer();

        oldCustomer.changeCustomerInfo(scanner);

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
    }while (!exitloop);
        
        }
    }
