package Classes;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Customer {
    private String customerName;
    private String customerPhoneNumber;
    private String customerEmail;
    private String customerStrtNameHouseNr;
    private String customerPostCode;
    private String customerCity;

    public Customer() {}

    public Customer (String customerName,String customerPhoneNumber,String customerEmail,String customerStrtNameHouseNr,String customerPostCode, String customerCity) {
        this.customerName = customerName;
        this.customerPhoneNumber = customerPhoneNumber;
        this.customerEmail = customerEmail;
        this.customerStrtNameHouseNr = customerStrtNameHouseNr;
        this.customerPostCode = customerPostCode;
        this.customerCity = customerCity;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNumber() {
        return this.customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getCustomerEmail() {
        return this.customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerStrtNameHouseNr() {
        return this.customerStrtNameHouseNr;
    }

    public void setCustomerStrtNameHouseNr(String customerStrtNameHouseNr) {
        this.customerStrtNameHouseNr = customerStrtNameHouseNr;
    }

    public String getCustomerPostCode() {
        return this.customerPostCode;
    }

    public void setCustomerPostCode(String customerPostCode) {
        this.customerPostCode = customerPostCode;
    }

    public String getCustomerCity() {
        return this.customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    //  this is needed if JSON\\customers.json is empty
    public void writeToJSON (Customer currentCustomer) {
        
            JSONObject customerObject = new JSONObject();
            customerObject.put("email", currentCustomer.getCustomerEmail());
            customerObject.put("address", currentCustomer.getCustomerStrtNameHouseNr());
            customerObject.put("Phone number", currentCustomer.getCustomerPhoneNumber());
            customerObject.put("Post code", currentCustomer.getCustomerPostCode());
            customerObject.put("city", currentCustomer.getCustomerCity());
            

            JSONObject fullOrder = new JSONObject(); ///    is this doing anything
            fullOrder.put(currentCustomer.getCustomerName(), customerObject);

        // Save JSON to a file
        try (FileWriter file = new FileWriter("JSON\\customers.json")) {
            file.append(fullOrder.toJSONString());
            System.out.println("Order data saved to JSON\\customers.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //  Adds new customer to customer JSON
    //  this won't work if JSON\\customers.json is empty
    public void appendToJSON (JSONObject pastCustomers) { 

            JSONObject customerObject = new JSONObject();
            customerObject.put("Email", this.getCustomerEmail());
            customerObject.put("Street and house number", this.getCustomerStrtNameHouseNr());
            customerObject.put("Postcode", this.getCustomerPostCode());
            customerObject.put("City", this.getCustomerCity());
            customerObject.put("Phone number", this.getCustomerPhoneNumber());

            JSONObject fullOrder = new JSONObject(); ///    Could/should this be a seperate function?
            fullOrder.put(this.getCustomerName(), customerObject);

             // Add the new order to the pastOrders JSONObject
            pastCustomers.put(this.getCustomerName(), customerObject);

        // Save JSON to a file
        try (FileWriter file = new FileWriter("JSON\\customers.json")) {
            file.write(pastCustomers.toJSONString());
            System.out.println("Customer data saved to order.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //  Returns object of all customers
    public JSONObject readWholeJson() {
        JSONParser parser = new JSONParser();
        JSONObject pastCustomers = new JSONObject(); 
        
        try (FileReader reader = new FileReader("JSON\\customers.json")) {
            pastCustomers = (JSONObject) parser.parse(reader); /// this is only the first key for some reason
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return pastCustomers;
    }

    //  Returns object of existing customer
    public Customer getCustomer (String customerNameString) {
        JSONParser parser = new JSONParser();
        JSONObject customerDatabase = new JSONObject(); 
        Customer placeholderCustomer = new Customer();
        try (FileReader reader = new FileReader("JSON\\customers.json")) {
            customerDatabase = (JSONObject) parser.parse(reader); /// this is only the first key for some reason
            JSONObject specificCustomer = (JSONObject)customerDatabase.get(customerNameString);
            String phoneNumber = (String)specificCustomer.get("Phone number");
            String email = (String)specificCustomer.get("Email");
            String streetNameHouseNr = (String)specificCustomer.get("Street and house number");
            String city = (String)specificCustomer.get("City");
            String postCode = (String)specificCustomer.get("Postcode");
            Customer oldCustomer = new Customer(customerNameString,phoneNumber,email,streetNameHouseNr,postCode,city);
            return oldCustomer;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return placeholderCustomer;
    }
    
    //  Retrieves customer object from JSON
    public Customer getCustomerFromJSON (String customerName) {
        JSONObject pastCustomers = readWholeJson();
        String customerStrtNameHouseNr = (String)pastCustomers.get("Street and house number");
        String customerPostCode = (String)pastCustomers.get("Postcode");
        String customerEmail = (String)pastCustomers.get("Email");
        String customerCity = (String)pastCustomers.get("City");
        String customerPhoneNumber = (String)pastCustomers.get("Phone number");

        Customer retrievedCustomer = new Customer(customerName,customerPhoneNumber, customerEmail,customerStrtNameHouseNr,customerPostCode,customerCity);
        return retrievedCustomer;
    }

    // returns a new customer object based on user input
    public Customer createCustomer (Scanner currentSession) {
        // Get customer info
        System.out.print("Name: ");
        Boolean checker = false;
        String customerName = "";
        String customerPhoneNumber = "";
        String customerEmail = "";
        while (!checker) {
             customerName = currentSession.nextLine();
            if (!containsNumbers(customerName)) {
                checker = true;
            }
            else if(containsNumbers(customerName)) {
                System.out.println("Invalid input.");
            }
        }
        System.out.print("Phone number:");
        checker = false;
        while (!checker) {
         customerPhoneNumber = currentSession.nextLine();
            if (!containsLetters(customerPhoneNumber)) {
                checker = true;
            }
            else if(containsLetters(customerPhoneNumber)) {
                System.out.println("Invalid input.");
            }
        }
        System.out.print("Email adress:");
        checker = false;
        while (!checker) {
         customerEmail = currentSession.nextLine();
            if (containsAtSymbol(customerEmail)) {
                checker = true;
            }
            else if(!containsAtSymbol(customerEmail)) {
                System.out.println("Invalid input.");
            }
        }
        System.out.print("Street and house number:");
        String customerStrtNameHouseNr = currentSession.nextLine();
        System.out.println("Postcode:");
        String customerPostCode = currentSession.nextLine();
        System.out.println("City:");
        String customerCity = currentSession.nextLine();

        Customer newCustomer = new Customer(customerName,customerPhoneNumber,customerEmail,customerStrtNameHouseNr, customerPostCode,  customerCity);
        return newCustomer;
    }

    //  returns false if the scanner input is not in the customer JSON
    public boolean isPresent (JSONObject pastCustomers, String inputName) {

            boolean isPresent = false;
            Set<String> keys = pastCustomers.keySet();
            String[] namesArray = keys.toArray(new String[0]);
            String formattedInput = "";
            String formattedName = "";

            for (String name : namesArray) {
                formattedInput = inputName.toLowerCase();
                formattedInput = formattedInput.trim();
                formattedName = name.toLowerCase();
                formattedName = formattedName.trim();
                if (formattedInput.equals(formattedName)) {
                    isPresent = true;
                    break;
                }
            }
        return isPresent;
    }

    //  Allows user to replace values of address and contact
    public void changeCustomerInfo (Scanner scanner) {

        System.out.println("\nPlease enter your name: \n");
        String customerName = scanner.nextLine();
        String formattedCustomerName = customerName.toLowerCase();
        formattedCustomerName = formattedCustomerName.trim();
        System.out.println("\nPlease enter the field you would like to change: ");
        System.out.println("\n1. Street and house number\n2. Email\n3. Phone number\n4. Postcode\n5. City\n");
        int option = scanner.nextInt();
        String fieldToChange = "";
        String newValue = "";
        boolean flag = false;
        while (!flag) {
            String customerPhoneNumber = "";
            switch (option) {
                case 1:
                    fieldToChange = "Street and house number";
                    scanner.nextLine();
                    System.out.println("New value:");
                    newValue = scanner.nextLine();
                    flag = true;
                    break;
                case 2: 
                    fieldToChange = "Email";
                    Boolean checker = false;
                    scanner.nextLine();
                    while (!checker) {                 
                        System.out.println("New value:");
                        newValue = scanner.nextLine();
                            if (containsAtSymbol(newValue)) {
                                checker = true;
                                flag = true;
                            }
                            else if(!containsAtSymbol(newValue)) {
                                System.out.println("Invalid input.");
                            }
            }
                    break;
                case 3: /// Fix the customerPhoneNumber scope
                    fieldToChange = "Phone number";
                    checker = false;
                    while (!checker) {
                        scanner.nextLine();
                        System.out.println("New value:");
                        newValue = scanner.nextLine();
                        if (!containsLetters(newValue)) {
                            checker = true;
                            flag = true;
                        }
                        else if(containsLetters(customerPhoneNumber)) {
                            System.out.println("Invalid input.");
                        }
                    }
                    break;
                case 4: 
                    fieldToChange = "Postcode";
                    scanner.nextLine();
                    System.out.println("New value:");
                    newValue = scanner.nextLine();
                    flag = true;
                    break;
                case 5: 
                    fieldToChange = "City";
                    scanner.nextLine();
                    System.out.println("New value:");
                    newValue = scanner.nextLine();
                    flag = true;
                    break;
                default:
                System.out.println("Invalid input.");
                    break;
            }
    }

    formattedCustomerName = Customer.toTitleCase(formattedCustomerName);

    Customer oldCustomer = getCustomer(formattedCustomerName);

    oldCustomer.editCustomer(formattedCustomerName, fieldToChange, newValue);

    System.out.println("\nCustomer info successfully changed:\n" + customerName + "'s " + fieldToChange + " changed to " + newValue);

}

    //  This gets called in changeCustomerInfo() after argument validation
    public void editCustomer (String customer, String fieldToChange, String newValue) { 
        JSONParser parser = new JSONParser();
        JSONObject customerDatabase = new JSONObject(); 
        try (FileReader reader = new FileReader("JSON\\customers.json")) {
            customerDatabase = (JSONObject) parser.parse(reader); /// this is the whole JSON
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        JSONObject specificCustomer = (JSONObject) customerDatabase.get(customer);

        specificCustomer.put(fieldToChange,newValue);

        try (FileWriter writer = new FileWriter("JSON\\customers.json")) {
            writer.write(customerDatabase.toJSONString()); // Write the modified JSON back to the file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder(input.length());
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    //  Returns true if there are any numbers in a string
    public static boolean containsNumbers(String input) {
        return input.matches(".*\\d.*");
    }

    //  Returns true if there are any characters in a string
    public static boolean containsLetters(String input) {
        return input.matches(".*\\p{Alpha}.*");
    }

    // returns true if there is an @ symbol in a string
    public static boolean containsAtSymbol(String input) {
        return input.contains("@");
    }

}