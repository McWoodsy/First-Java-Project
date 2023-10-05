package Classes;
import java.util.List;

public class ShoppingCart {
            List<Item> chosenItems;
            List<String> amounts;

    public ShoppingCart() {}

    public ShoppingCart(List<Item> chosenItems) {
        this.chosenItems = chosenItems;
    }

    public List<Item> getChosenItems() {
        return this.chosenItems;
    }

    public void setChosenItems(List<Item> chosenItems) {
        this.chosenItems = chosenItems;
    }

    public void addItemByID(String id, List<Item> items) {
        for (Item item : items) {
            if (item.getName().equals(id)) {
                chosenItems.add(item);
            }
        }
    }

    public void addItemByName(String name, List<Item> items) {
        for (Item item : items) {
            if (item.getName().equals(name)) {
                chosenItems.add(item);
            }
        }
    }
}