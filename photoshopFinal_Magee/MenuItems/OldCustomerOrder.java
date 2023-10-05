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

public class OldCustomerOrder {
        
        static ShoppingCart shoppingCart = new ShoppingCart();
        static Item item = new Item();
        static OrderManager orderManager = new OrderManager();
        static final String jsonFilePath = "JSON\\customers.json";
        static LocalDate orderDate = LocalDate.now();
        static LocalDateTime orderDateTime = LocalDateTime.now();


    public static void main(Scanner scanner) {

        //  These are needed throughout
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        HashMap<String,String> openingHours = orderManager.readOpeningHours();
        boolean exitLoop = false;
        List<Item> catalog = item.csvReader(); 
        
        
    do {

        //  get existing customer from customers JSON
        Customer currentCustomer = new Customer();
        String existingCustomer = "";
        JSONObject pastCustomers = currentCustomer.readWholeJson();
        //boolean checker = currentCustomer.isPresent(pastCustomers, existingCustomer);
        int i = 0;

        while (i < 4) {
            System.out.println("Please enter your name:");
            existingCustomer = scanner.nextLine();
            boolean checker = currentCustomer.isPresent(pastCustomers, existingCustomer);
            if (!checker) {
                System.out.println("Invalid input. Try again.");
                i++;
            }
            else if (checker) {
                break;
            }   
        }

        if (i < 4) {

            currentCustomer = currentCustomer.getCustomer(Customer.toTitleCase(existingCustomer));
        
            // user input is split into 2 variables which are seperately put into identically indexed 
            // lists for item chosen and its corresponding amount 
            List<Item> chosenItems = new ArrayList<Item>();
            List<String> amounts = new ArrayList<String>();
            orderManager.fillShoppingCart(scanner, catalog, chosenItems, amounts);

            //  Here we are preparing everything needed to create a full order object
            JSONObject pastOrders = orderManager.readWholeJson();
            int totalWorkingTime = orderManager.calculateTotalWorkingTime(chosenItems, amounts);
            Order order = new Order(orderManager.generateOrderNumber(pastOrders),orderDate, currentCustomer, chosenItems,amounts,totalWorkingTime);
            int daysToProduce = order.daysToProduce(dayOfWeek, openingHours,totalWorkingTime);
            int surplusHours = order.hoursToProduce(dayOfWeek, openingHours,totalWorkingTime);
            String pickupDateTime = orderManager.getPickupDateTime(daysToProduce, surplusHours , dayOfWeek, openingHours, orderManager, pastOrders);

            order = new Order(orderManager.generateOrderNumber(pastOrders),orderDate, currentCustomer, chosenItems,amounts,totalWorkingTime, pickupDateTime);

            //  Once the object is fully made, we add it to the JSON file for orders, and then we print the invoice
            orderManager.appendToJSON(order, currentCustomer, existingCustomer, pastOrders);
            Invoice.printInvoice(currentCustomer, order, orderManager);

            //  User gets option to repeat the process or return to the main program
            System.out.println("\nTo return to the main menu type 1 and hit enter\nTo modify more type 2 and hit enter");
            boolean choice = false;

            while (!choice) {

                int  input = scanner.nextInt();

                if (input == 1) {
                    exitLoop = true;
                    choice =true;
                } 
                else if (input ==2) {
                    scanner.nextLine();
                    choice = true;
                }
                else {
                    System.out.println("Invalid input.");
                    
                }
            } 
            }
        }
        while (!exitLoop);
        }
    }