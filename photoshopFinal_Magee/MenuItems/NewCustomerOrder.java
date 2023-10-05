package MenuItems;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import org.json.simple.JSONObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import Classes.*;

public class NewCustomerOrder {
        
        static ShoppingCart shoppingCart = new ShoppingCart();
        static Item item = new Item();
        static OrderManager orderManager = new OrderManager();
        static LocalDate orderDate = LocalDate.now();
        static LocalDateTime orderDateTime = LocalDateTime.now();

    public static void main(Scanner scanner) {

        boolean exitloop = false;

        do {

            //  These are needed throughout the program
            Calendar calendar = Calendar.getInstance();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            HashMap<String,String> openingHours = orderManager.readOpeningHours();
            List<Item> catalog = item.csvReader(); 
            List<Item> chosenItems = new ArrayList<Item>();
            List<String> amounts = new ArrayList<String>();
            Customer currentCustomer = new Customer();
        
            //  Get existing customer from JSON
            currentCustomer = currentCustomer.createCustomer(scanner);

            // user input is split into 2 variables which are seperately put into identically indexed 
            // lists for item chosen and its corresponding amount    
            orderManager.fillShoppingCart(scanner, catalog, chosenItems, amounts);

            // Here we prepare all the necessary parameters to append a newly created order object to the JSON file
            JSONObject pastOrders = orderManager.readWholeJson();
            JSONObject pastCustomers = currentCustomer.readWholeJson();
            int totalWorkingTime = orderManager.calculateTotalWorkingTime(chosenItems, amounts);
            Order order = new Order(orderManager.generateOrderNumber(pastOrders),orderDate, currentCustomer, chosenItems,amounts,totalWorkingTime);
            int daysToProduce = order.daysToProduce(dayOfWeek, openingHours,totalWorkingTime);
            int surplusHours = order.hoursToProduce(dayOfWeek, openingHours,totalWorkingTime);
            String pickupDateTime = orderManager.getPickupDateTime(daysToProduce, surplusHours , dayOfWeek, openingHours, orderManager, pastOrders);

            //  Then we make the full object and add it to the orders JSON
            order = new Order(orderManager.generateOrderNumber(pastOrders),orderDate, currentCustomer, chosenItems,amounts,totalWorkingTime,pickupDateTime);
            orderManager.appendToJSON(order, currentCustomer, currentCustomer.getCustomerName(), pastOrders);

            //  Add newly created customer object to customers JSON
            currentCustomer.appendToJSON(pastCustomers);

            //  Create new invoice
            Invoice.printInvoice(currentCustomer, order, orderManager);

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