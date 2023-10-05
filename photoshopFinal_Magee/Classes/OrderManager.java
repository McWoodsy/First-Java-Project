package Classes;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class OrderManager {

    public OrderManager () {};

    //  This is needed if the JSON is empty
    public void writeToJSON (List<Item> chosenItems , List<String> amounts, Customer currentCustomer,String customerName,  String orderNumber, String pickupDateTime  ) { // instead of pastOrders,
        JSONArray itemsArray = new JSONArray(); 
        ////        CATCH THIS ERROR                                                                            // use readFromJson?
            for (Item chosenItem : chosenItems) {
                int i = 0;
                JSONObject itemObject = new JSONObject();
                itemObject.put("id", chosenItem.getId());
                itemObject.put("name", chosenItem.getName());
                itemObject.put("price", chosenItem.getPrice());
                itemObject.put("amount",amounts.get(i));
                i++;
                itemsArray.add(itemObject);
            }

            JSONObject orderObject = new JSONObject();
            orderObject.put("orderDate", LocalDate.now().toString());
            orderObject.put("customer", customerName);
            orderObject.put("email", currentCustomer.getCustomerEmail());
            orderObject.put("pickup date", pickupDateTime);
            orderObject.put("address", currentCustomer.getCustomerStrtNameHouseNr());
            orderObject.put("items", itemsArray);
            

            JSONObject fullOrder = new JSONObject(); 
            fullOrder.put(orderNumber, orderObject);

        // Save JSON to a file
        try (FileWriter file = new FileWriter("JSON\\orders.json")) {
            file.append(fullOrder.toJSONString());
            System.out.println("Order data saved to order.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
    //  adds new JSON object to all the previous orders JSON object and rewrites to JSON
    public void appendToJSON (Order order, Customer currentCustomer, String customerName, JSONObject pastOrders) { 

            JSONArray itemsArray = new JSONArray();                                                                   
            int i = 0;
            for (Item chosenItem : order.chosenItems) {                
                JSONObject itemObject = new JSONObject();
                itemObject.put("name", chosenItem.getName());
                itemObject.put("price", chosenItem.getPrice());
                itemObject.put("amount",order.amounts.get(i));
                itemObject.put("id", chosenItem.getId());
                i++;
                itemsArray.add(itemObject);
            }

            JSONObject orderObjectOne = new JSONObject();
            orderObjectOne.put("orderDate", LocalDate.now().toString());    

            orderObjectOne.put("customer", customerName);
            orderObjectOne.put("email", currentCustomer.getCustomerEmail());
            orderObjectOne.put("address", currentCustomer.getCustomerStrtNameHouseNr());
            orderObjectOne.put("pickup date", order.pickupDateTime);
            orderObjectOne.put("items", itemsArray);

            JSONObject fullOrder = new JSONObject();
            fullOrder.put(order.orderNumber, orderObjectOne);

             // Add the new order to the pastOrders JSONObject
            pastOrders.put(order.orderNumber, orderObjectOne);

        // Save JSON to a file
        try (FileWriter file = new FileWriter("JSON\\orders.json")) {
            file.write(pastOrders.toJSONString());
            System.out.println("Order data saved to order.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
    //  Returns JSON of all previous orders
    public JSONObject readWholeJson() {
        JSONParser parser = new JSONParser();
        JSONObject pastOrders = new JSONObject(); 
        
        try (FileReader reader = new FileReader("JSON\\orders.json")) {
            pastOrders = (JSONObject) parser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return pastOrders;
    }

    //  Returns JSON of order whose number was provided as parameter
    public JSONObject readSpecificJSON(String orderNumber) {
        JSONParser parser = new JSONParser();
        JSONObject pastOrders = new JSONObject(); 
        
        try (FileReader reader = new FileReader("JSON\\orders.json")) {
            pastOrders = (JSONObject) parser.parse(reader); /// this is the whole JSON
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        JSONObject specificOrder = (JSONObject) pastOrders.get(orderNumber);
        return specificOrder;
    }



    //  Ensures ascending order number and no duplicates
    public String generateOrderNumber (JSONObject pastOrders) {
        Set<String> keys = pastOrders.keySet();
        String highestKey = keys.toArray(new String[0])[keys.size() - 1];   //  Converts to an array and accesses the last element
        int highKey = Integer.parseInt(highestKey);
        highKey++;
        String newOrderNumber = Integer.toString(highKey);
        return newOrderNumber;
    }

    //  Adds new items to the indexed order in JSON
    public void addOrderItems (String orderNumber, Scanner scanner, List<Item> catalog, List<Item> newChosenItems, List<String> newAmounts, OrderManager orderManager) {
        System.out.println("\nSelect an item to add to your shopping cart, writing the name or ID, followed by a space and then the amount: \t\t\t (Type 'd' when finished adding items)");     
        System.out.println("\nTo remove an item, write its ID followed by a '0'\n");     
        String userInput = " ";       

            // write item id, then a space, then the amount of that item needed in order to fill the shopping cart (chosenItems)
            while (!userInput.equals("d")) {
                userInput = scanner.nextLine();
                if (!userInput.equals("d")) {
                String [] parts = userInput.split(" ");
                    if (parts.length == 2) {
                String item = parts[0];
                String amount = parts[1];
                for (Item currentItem : catalog) {
                    if (currentItem.getId().equals(item) || currentItem.getName().equals(item)) {
                        newChosenItems.add(currentItem);
                        newAmounts.add(amount);
                    }
                }
            }
                    else {
                        System.out.println("Please provide input according to the instructions");
                    }
                }
            }

        JSONObject targetOrder = orderManager.readSpecificJSON(orderNumber);

        JSONArray itemsArray = (JSONArray)targetOrder.get("items");

    for (int i = 0; i < newChosenItems.size(); i++) {
        Item chosenItem = newChosenItems.get(i);
        String amount = newAmounts.get(i);

        // Create a JSONObject for each item and amount
        JSONObject itemObject = new JSONObject();
        itemObject.put("id", chosenItem.getId());
        itemObject.put("name", chosenItem.getName());
        itemObject.put("amount", amount);

        // Add the itemObject to the itemsArray
        itemsArray.add(itemObject);
    }
        targetOrder.put("items", itemsArray);
                
        JSONObject fullOrder = new JSONObject();
        fullOrder.put(orderNumber, targetOrder);
        JSONObject pastOrders = orderManager.readWholeJson();
        pastOrders.put(orderNumber, targetOrder);


        // Save JSON to a file
            try (FileWriter file = new FileWriter("JSON\\orders.json")) {
                file.write(pastOrders.toJSONString());
                System.out.println("\nOrder data saved to order.json\n");
            } catch (IOException e) {
                e.printStackTrace();
            } 
        }
    
    //  Calculates working time in hours for an order taking item count into consideration
    public int calculateTotalWorkingTime (List<Item> chosenItems,  List<String> amounts) {
        int totalWorkingTime = 0;
        for (int i = 0; i < chosenItems.size(); i++) {
            totalWorkingTime += chosenItems.get(i).getWorkingHours() * Integer.parseInt(amounts.get(i));
        }
        return totalWorkingTime;
    }

    //  Returns a Hashmap for opening hours (number index for day of week as key, and the number of hours 
    //  available to work that day as the value)
    public HashMap<String, String> readOpeningHours () {
        HashMap<String,String> openingHours = new HashMap<String,String>();
        try {
            Scanner csvReader = new Scanner(new File("CSV\\PhotoShop_OpeningHours.csv"));
            int i = 1;
            while (i < 8) {
                //  Start on first line of pricelist CSV and split into parts, then loop into next lines and repeat
                String line = csvReader.nextLine();
                String[] parts = line.split(";");
                LocalTime startTime = LocalTime.parse(parts[2]);
                LocalTime endTime = LocalTime.parse(parts[3]);
                long hoursDifference = ChronoUnit.HOURS.between(startTime, endTime);
                String hoursDifferenceString = Long.toString(hoursDifference);
                openingHours.put(Integer.toString(i), hoursDifferenceString);
                i++;
            }
            csvReader.close();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
        return openingHours;
        }
    
    //  Gets start time of indexed day
    public LocalTime getStartTime (int dayOfWeek) {
        String startTime = "00:00";
        try {
            Scanner csvReader = new Scanner(new File("CSV\\PhotoShop_OpeningHours.csv"));
            int i = 0;
            while (i < 7) {
                String line = csvReader.nextLine();
                String[] parts = line.split(";");
                if (Integer.parseInt(parts[0]) == dayOfWeek) {
                    startTime = parts[2];
                }
                i++;
            }
            csvReader.close();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
        LocalTime openTime = LocalTime.parse(startTime);
        return openTime;
        } 

    //  Gets end time of indexed day
    public LocalTime getEndTime(int dayOfWeek) {
        String startTime = "00:00";
        try {
            Scanner csvReader = new Scanner(new File("CSV\\PhotoShop_OpeningHours.csv"));
            int i = 1;
            while (i < 8) {
                String line = csvReader.nextLine();
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    int csvDayOfWeek = Integer.parseInt(parts[0].trim()); 
                    if (csvDayOfWeek == dayOfWeek) {
                        startTime = parts[3].trim(); 
                        break; 
                    }
                }
                i++;
            }
            csvReader.close();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
        LocalTime endTime = LocalTime.parse(startTime);
        return endTime;
    }
    
    //  Uses getEndTime() and getStartTime() and the time calculators in the Order class
    //  also uses daysToProduce() and hoursToProduce() from Order class
    //  adds this to the pickupDateTime in most recent order, then checks if it falls within opening hours
    public String getPickupDateTime ( int daysPassed, int remainingWorkTime,int dayOfWeek, HashMap<String,String> openingHours, OrderManager orderManager, JSONObject pastOrders) {
        
        //  JSON object of all previous orders is passed in, and an array is made with all of its keys
        Set<String> keys = pastOrders.keySet();
        String highestKey = keys.toArray(new String[0])[keys.size() - 1];

        //  Make a new JSON object for a specific order with the last item in the array as index,
        //  then we get the pickupdate of that order and convert it to LocalDateTime
        JSONObject specificOrder = (JSONObject) pastOrders.get(highestKey);
        String pickupDate = (String) specificOrder.get("pickup date");
        LocalDateTime lastOrderPickup = formatToLDT(pickupDate, "yyyy-MM-dd HH:mm:ss");
        LocalDateTime pickupDateTime = LocalDateTime.now();

        long daysBetween = 0;

        //  pickupdatetime is already set to now, because if the most recent pickup datetime is in the past,
        //  we will start calculations from present time. Otherwise, we start from the most recent orders pickuptime
        if (lastOrderPickup.isAfter(LocalDateTime.now())) {
            pickupDateTime = lastOrderPickup;
            pickupDateTime = pickupDateTime.plusHours(remainingWorkTime);
            pickupDateTime = pickupDateTime.plusDays(daysPassed);
            
            //  Update the dayOfWeek variable to get accurate opening hours
            daysBetween = ChronoUnit.DAYS.between(pickupDateTime, lastOrderPickup);
            dayOfWeek = (dayOfWeek + (int)daysBetween)%7;

        }
        else {
            pickupDateTime = pickupDateTime.plusHours(remainingWorkTime);
            pickupDateTime = pickupDateTime.plusDays(daysPassed);
        }
        LocalTime pickupTime = pickupDateTime.toLocalTime();

        // now we check if the pickup time has surpassed the current days closing time, and add those hours to tomorrows start time
        if (pickupTime.isAfter(getEndTime((dayOfWeek+daysPassed)%7))) {

            //  This gets the time between the pickuptime we arrived at and the end time of the day we landed on
            Duration duration = Duration.between(getEndTime((dayOfWeek + daysPassed) % 7), pickupTime); 
            long hoursExceeded = duration.toHours();

            //  Then we add this difference to the start time of 'tomorrow'
            pickupTime = getStartTime((dayOfWeek+daysPassed+1)%7).plusHours(hoursExceeded);
            LocalDate pickupDateFix = pickupDateTime.toLocalDate();
            pickupDateFix = pickupDateFix.plusDays(1);
            LocalDateTime newPickupDateTime = LocalDateTime.of(pickupDateFix, pickupTime);
            String formattedDateTime = formatToString(newPickupDateTime, "yyyy-MM-dd HH:mm:ss");
            return formattedDateTime;

        //  Or we check if it has undercut the start time, and add the hours from yesterdays end time to midnight, 
        //  and midnight to the undercut pickup time. This time is then added to the start time of the day we landed on

        } else if (pickupTime.isBefore((getStartTime((dayOfWeek+daysPassed)%7)))){
            LocalTime midnight = LocalTime.MIDNIGHT;
            LocalTime lastEndTime = getEndTime((dayOfWeek+daysPassed-1)%7); //  Here we get yesterdays end time
                        System.out.println(lastEndTime); 

            long hoursSinceMidnight = midnight.until(pickupTime, ChronoUnit.HOURS);
            System.out.println("hours since midnight " + hoursSinceMidnight); //  Time from start of the day to the incorrect pickup time
            long hoursSinceLastEndTime = midnight.until(lastEndTime, ChronoUnit.HOURS); //  Time from the last end time until midnight
            hoursSinceLastEndTime = 24 - hoursSinceLastEndTime;

            //  Then we add these invalid work hours to the start time of 'today'
            LocalTime pickupTimeFix = getStartTime((dayOfWeek+daysPassed)%7);
            pickupTimeFix = pickupTimeFix.plusHours(hoursSinceLastEndTime + hoursSinceMidnight);   
            pickupDateTime = pickupDateTime.with(pickupTimeFix);

                //  one last check to see if it is has surpassed the end time again,
                //  in which case we repeat the first if statement

                if (pickupTimeFix.isAfter(getEndTime((dayOfWeek+daysPassed)%7))) {

                    //  This gets the time between the pickuptime we arrived at and the end time of the day we landed on
                    Duration duration = Duration.between(getEndTime((dayOfWeek + daysPassed) % 7), pickupTime); 
                    System.out.println("End time "+ getEndTime((dayOfWeek + daysPassed) % 7));
                    long hoursExceeded = duration.toHours();
                    System.out.println("hours exceeded  " + hoursExceeded);

                    //  Then we add this difference to the start time of 'tomorrow'
                    pickupTime = getStartTime((dayOfWeek+daysPassed+1)%7).plusHours(hoursExceeded);
                    LocalDate pickupDateFix = pickupDateTime.toLocalDate();
                    pickupDateFix = pickupDateFix.plusDays(1);
                    LocalDateTime newPickupDateTime = LocalDateTime.of(pickupDateFix, pickupTime);
                    String formattedDateTime = formatToString(newPickupDateTime, "yyyy-MM-dd HH:mm:ss");
                    return formattedDateTime;
                        }
        }   
        String formattedDateTime = formatToString(pickupDateTime, "yyyy-MM-dd HH:mm:ss");
        return formattedDateTime;
    } 

    // Formats LocalDateTime into String
    public static String formatToString(LocalDateTime localDateTime, String formatPattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
        return localDateTime.format(formatter);
    }

    //  Formats Strings from a JSON into LocalDateTime
    public static LocalDateTime formatToLDT(String dateTimeString, String formatPattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    //  returns false if the scanner input is not in the customer JSON
    public static boolean isPresent (JSONObject pastOrders, String inputNumber) {

        boolean isPresent = false;
        Set<String> keys = pastOrders.keySet();
        String[] orderNumberArray = keys.toArray(new String[0]);
        String formattedInput = "";
        String formattedName = "";

        for (String number : orderNumberArray) {
            formattedInput = inputNumber.trim();
            if (formattedInput.equals(number)) {
                isPresent = true;
                break;
            }
        }
    return isPresent;
}
    
    //  Collects and validates customer input, then calls editOrder() to change order information in JSON
    public void changeOrderInfo (Scanner scanner, String orderNumber, OrderManager orderManager) {
         System.out.println("\nWhich field would you like to change?\n\n1. City\n2. Email\n3. Street name and house number\n");
         int option = scanner.nextInt();
         String fieldToChange = "";
         String newValue = "";
         switch (option) {
             case 1:
                fieldToChange = "city";
                scanner.nextLine();
                System.out.println("Please enter the new value:");
                newValue = scanner.nextLine();
                break;
             case 2:
                fieldToChange = "email";
                Boolean checker = false;
                while (!checker) {
                    scanner.nextLine();
                    System.out.println("Please enter the new value:");
                    newValue = scanner.nextLine();
                    if (Customer.containsAtSymbol(newValue)) {
                        checker = true;
                    }
                    else if(!Customer.containsAtSymbol(newValue)) {
                        System.out.println("Invalid input.");
                    }}
                 break;
             case 3:
                fieldToChange = "address";
                break;
             default:
                scanner.nextLine();
                System.out.println("Please input a field: ");
                fieldToChange = scanner.nextLine();
                break;
         }
        // scanner.nextLine();
         orderManager.editOrder(orderNumber,fieldToChange,newValue);
         System.out.println("\nThe " + fieldToChange + " field has been successfully changed to " + newValue + " for order " + orderNumber + "\n");
    }
    
    public void editOrder (String orderNumber, String fieldToChange, String newValue) { 
        JSONParser parser = new JSONParser();
        JSONObject pastOrders = new JSONObject(); 
        try (FileReader reader = new FileReader("JSON\\orders.json")) {
            pastOrders = (JSONObject) parser.parse(reader); /// this is the whole JSON
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        JSONObject specificCustomer = (JSONObject) pastOrders.get(orderNumber);

        specificCustomer.put(fieldToChange,newValue);

        try (FileWriter writer = new FileWriter("JSON\\orders.json")) {
            writer.write(pastOrders.toJSONString()); // Write the modified JSON back to the file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double calculateTotalPrice(List<Item> chosenItems, List<String> amounts) {
        double totalPrice = 0;
        for (int index = 0; index < chosenItems.size(); index++) {
            totalPrice += chosenItems.get(index).getPrice() * Integer.parseInt(amounts.get(index));
        }
        return totalPrice;
    }

    //  Matches user input to item name iteratively in item catalog
    //  adds it to another list if there is a match
    //  controls for user input
    public void fillShoppingCart (Scanner scanner, List<Item> catalog, List<Item> chosenItems, List<String> amounts) {
    
        System.out.println("\nSelect an item to add to your shopping cart, writing the name or ID, followed by a space and then the amount: \t\t\t (Type 'd' when finished adding items)\n");     
        for (Item item : catalog) {
            System.out.println("ID: " + item.getId() + " ---- " + item.getName());
        }
        String userInput = " ";
        while (!userInput.equals("d")) {
            userInput = scanner.nextLine();
            if (!userInput.equals("d")) {
            String [] parts = userInput.split(" ");
                if (parts.length == 2) {
            String item = parts[0];
            String amount = parts[1];
            for (Item currentItem : catalog) {
                if (currentItem.getId().equals(item) || currentItem.getName().equals(item)) {
                    chosenItems.add(currentItem);
                    amounts.add(amount);
                }
            }
        }
                else {
                    System.out.println("Please provide input according to the instructions");
                }
        }
    }
}
}