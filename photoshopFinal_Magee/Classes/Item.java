package Classes;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Item {
    private String id;
    private String name;
    private double price;
    private int workingHours;
    private String amount;
    public final String itemCsvPath = "CSV\\PhotoShop_PriceList.csv";

    public Item(String id, String name, double price, int workingHours) {
       this.id = id;
       this.name = name;
       this.price = price;
       this.workingHours = workingHours;
    }
    public String getAmount() {
        return this.amount;
    }
    public void setamount(String amount) {
        this.amount = amount;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() {
        return this.price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getWorkingHours() {
        return this.workingHours;
    }
    public void setWorkingHours(int workingHours) {
        this.workingHours = workingHours;
    }
    public String getItemCsvPath() {
        return this.itemCsvPath;
    }

    public Item() {
    }
    
    //  creates the catalog as list of items
    public List<Item> csvReader() {
        List<Item> items = new ArrayList<>();
        try {
            Scanner csvReader = new Scanner(new File(itemCsvPath));
            int i = 0;
            while (i < 12) { // !isEmpty() or forEach / for (?)
                //  Start on first line of pricelist CSV and split into parts, then loop into next lines and repeat
                String line = csvReader.nextLine();
                String[] parts = line.split(";");
                Item newItem = new Item(parts[0], parts[1], Double.parseDouble(parts[2]), Integer.parseInt(parts[3]));
                items.add(newItem);
                i++;
            }
            csvReader.close();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
        return items;
    }
}