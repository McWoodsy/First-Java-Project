package Classes;

public class Invoice {

    public Invoice () {}

    public static void printInvoice(Customer customer, Order order, OrderManager orderManager) {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println();
        System.out.println("Invoice of Photoshop order:\t" + order.orderNumber);
        System.out.println("--------------------------------------------------------------");
        System.out.println();
    
        System.out.println("Customer info:");
        System.out.println("-------------\n");
        System.out.println("Name:\t\t" + customer.getCustomerName());
        System.out.println("Address:\t" + customer.getCustomerStrtNameHouseNr());
        System.out.println("Post Code:\t" + customer.getCustomerPostCode());
        System.out.println("City:\t\t" + customer.getCustomerCity());
        System.out.println("Email:\t\t" + customer.getCustomerEmail());
        System.out.println("Phone:\t\t" + customer.getCustomerPhoneNumber());
    
        System.out.println();
        System.out.println("--------------------------------------------------------------");
        System.out.println();
    
        System.out.println("Order specifications:");
        System.out.println("--------------------\n");
        System.out.println("Order number:\t" + order.orderNumber);
        System.out.println("Order date:\t" + order.orderDate);
        System.out.println("Production time in working hours:\t" + orderManager.calculateTotalWorkingTime(order.chosenItems, order.amounts));
        System.out.println("You can pick up your order after:\t" + order.pickupDateTime);
    
        System.out.println();
        System.out.println("--------------------------------------------------------------");
        System.out.println();
    
        System.out.printf("%-30s%-10s%-10s%-15s%n", "Photo type:", "Price:", "Amount:", "Total costs:");
        System.out.printf("%-30s%-10s%-10s%-15s%n", "----------", "------", "-------", "------------");
    
        double total = 0;
        int i = 0;
        for (Item item : order.chosenItems) {
            double price = item.getPrice();
            double amountDouble = Double.parseDouble(order.amounts.get(i));
            double totalPrice = price * amountDouble;
            System.out.printf("%-30s%-10.2f%-10s%-15.2f%n", item.getName(), price, order.amounts.get(i), totalPrice);
            total += totalPrice;
            i++;
        }
    
        System.out.println();
        System.out.printf("%-19s%.2f%n", "Total costs:", total);

        System.out.println("\n\n\n\n\n");

    }
}