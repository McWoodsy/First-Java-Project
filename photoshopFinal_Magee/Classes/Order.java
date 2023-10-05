package Classes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;

public class Order extends ShoppingCart {
    String orderNumber;
    LocalDate orderDate;
    Customer customer;
    int totalWorkingTime;
    String pickupDateTime;

    public Order() {
    }

    public Order(String orderNumber, LocalDate orderDate, Customer customer, List<Item> chosenItems,List<String> amounts,int totalWorkingTime) { // sTRING PICKUPdATEtIME
        this.chosenItems = chosenItems;
        this.amounts = amounts;
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.customer = customer;
        this.totalWorkingTime = totalWorkingTime;
        this.pickupDateTime = pickupDateTime;
    }
    
    public Order(String orderNumber, LocalDate orderDate, Customer customer, List<Item> chosenItems,List<String> amounts,int totalWorkingTime, String pickupDateTime) { 
        this.chosenItems = chosenItems;
        this.amounts = amounts;
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.customer = customer;
        this.totalWorkingTime = totalWorkingTime;
        this.pickupDateTime = pickupDateTime;
    }

    public double calculateTotalPrice(List<Item> chosenItems) {
        double totalPrice = 0;
        for (int index = 0; index < chosenItems.size(); index++) {
            totalPrice += chosenItems.get(index).getPrice();
        }
        return totalPrice;
    }

    //  this returns the number of days that pass for production
    public int daysToProduce(int dayOfWeek, HashMap<String, String> openingHours, int totalWorkingTime) {
        int daysPassed = 0;
        int remainingNegativeHours = 0;

        while (totalWorkingTime > 0) {
            if (dayOfWeek == 8) {
                dayOfWeek = 1;
            }

            List<String> values = new ArrayList<>(openingHours.values());

            // Calculate how many hours can be worked on the current day
            int hoursAvailable = Integer.parseInt(values.get(dayOfWeek - 1));

            // Calculate the remaining working time for the current day
            int remainingWorkingTime = totalWorkingTime - hoursAvailable;

            // Update the daysPassed and remainingNegativeHours accordingly
            if (remainingWorkingTime < 0) {
                remainingNegativeHours = Math.abs(remainingWorkingTime);
                return daysPassed;
            }
            
            // Move to the next day and update the totalWorkingTime
            dayOfWeek++;
            totalWorkingTime = remainingWorkingTime;
            daysPassed++;
        }
        return daysPassed;
    }

    /// This returns the number of extra hours to be added to the number of days
    public int hoursToProduce(int dayOfWeek, HashMap<String, String> openingHours, int totalWorkingTime) {
            System.out.println(dayOfWeek);
            int dayIndex = dayOfWeek - 1;
            int daysPassed = 0;
            int remainingWorkingTime = 0;
            boolean weekSkipped = false;
            //dayOfWeek = dayOfWeek%7;
                       // Get the values from the openingHours map as a list
            List<String> values = new ArrayList<>(openingHours.values());
    if (totalWorkingTime > Integer.parseInt(values.get(dayIndex))) {
            while ( totalWorkingTime >= Integer.parseInt(values.get(dayIndex))) {     //|| totalWorkingTime >= Integer.parseInt(values.get(dayOfWeek%7))) {
                                                                                        // this is causing problems
                // Calculate how many hours can be worked on the current day
                int hoursAvailable = Integer.parseInt(values.get(dayOfWeek - 1));
                // Calculate the remaining working time for the current day
                remainingWorkingTime = totalWorkingTime - hoursAvailable;    
                dayOfWeek++;
                totalWorkingTime = remainingWorkingTime;
                daysPassed++;
                if (dayOfWeek == 8) {
                    dayOfWeek = 1;  ///// this is messing it up
                    weekSkipped = true;
               }
               dayIndex = dayOfWeek - 1;
        } System.out.println(weekSkipped);       
            return remainingWorkingTime;
    }
    else {
        return totalWorkingTime;
    }
}    

    public static String formatToString(LocalDateTime localDateTime, String formatPattern) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
            return localDateTime.format(formatter);
        }


















}