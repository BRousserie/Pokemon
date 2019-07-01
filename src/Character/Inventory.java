/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <brousserie@iut.u-bordeaux.fr> and <tpedrero@iut.u-bordeaux.fr>
 * wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy us a beer in return Baptiste and Tony
 * ----------------------------------------------------------------------------
 */
package Character;

import FileIO.Savable;
import GameEngine.Game;
import Item.Item;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class allowing to store the items and money of a dressor
 */
public class Inventory implements Savable<JsonObject>
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    //Trainer's amount of money
    private int pokeDollars;
    //Trainer's list of items
    private List<Item> items;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Creates Inventories for trainers that have money and multiple items.
     *
     * @param items trainer's list of items
     * @param pokeDollars trainer's amount of money
     */
    public Inventory(List<Item> items, int pokeDollars)
    {
        this.pokeDollars = pokeDollars;
        this.items = items;
    }

    /**
     * Creates Inventories for trainers that have money and one item.
     *
     * @param items trainer's list of items
     * @param pokeDollars trainer's amount of money
     */
    public Inventory(Item item, int pokeDollars)
    {
        this.pokeDollars = pokeDollars;
        items = new ArrayList<>();
        items.add(item);
    }

    /**
     * Creates Inventories for trainers that only have money.
     *
     * @param pokeDollars trainer's amount of money
     */
    public Inventory(int pokeDollars)
    {
        this(new ArrayList<>(), pokeDollars);
    }

    /**
     * Constructor for creating Inventories out of a game saved in Json format.
     *
     * @param bag the bag previously saved in Json format
     */
    public Inventory(JsonObject bag)
    {
        pokeDollars = bag.getInt("money", 0);
        items = new ArrayList<>();
        for (JsonValue itemValue : bag.get("items").asArray()) {
            String item = itemValue.asString();
            for (int i = 0; i < Integer.parseInt(
                 item.substring(item.indexOf(":") + 1)); i++)
                items.add(Game.getGame().getDatas()
                        .getItem(item.substring(0, item.indexOf(":"))));
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    /**
     * Gets the trainer's amount of money.
     *
     * @return the trainer's amount of money
     */
    public int getPokeDollars()
    {
        return pokeDollars;
    }

    /**
     * Gets the trainer's list of items, not modifiable.
     *
     * @return the trainer's list of items
     */
    public List<Item> getItems()
    {
        return Collections.unmodifiableList(items);
    }

    /**
     * Gets a Stream containing only the Items of the specified type from a
     * trainer's inventory.
     *
     * @param wanted type of Item you want to get
     * @return the list of items of the specified type
     */
    public Stream getItemsOfType(Class wanted)
    {
        return items.stream().filter(e -> e.getClass().equals(wanted));
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Method on money">
    /**
     * Updates player's money depending on the amount he recieved/gave.
     *
     * @param monetaryFlux the amount to add (+) or remove(-) from inventory
     */
    public void exchangeMoney(int monetaryFlux)
    {
        pokeDollars += monetaryFlux;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods on the items list">
    /**
     * Adds an item to an inventory.
     *
     * @param item the item to add
     */
    public void addItem(Item item)
    {
        items.add(item);
    }

    /**
     * Removes an item from the list of items of a trainer.
     *
     * @param item
     */
    public void remove(Item item)
    {
        try {
            items.remove(item);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Given an String, checks if the Inventory contains an item containing this
     * String (using .contains allows matching with CTs and CSs without telling
     * their number).
     *
     * @param itemName name the item
     * @return true if the bag contains this item
     */
    public boolean contains(String itemName)
    {
        return items.stream()
                .anyMatch(e -> e.getName().contains(itemName));
    }

    /**
     * Groups each item of the inventory and links it with its quantity.
     *
     * Method found on this website :
     * https://www.mkyong.com/java8/java-8-collectors-groupingby-and-mapping-example/
     *
     * @return map linking items with their quantity
     */
    public Map<Item, Long> regroup()
    {
        return items.stream().collect(Collectors
                .groupingBy(i -> i, Collectors.counting()));
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Save to Json">
    /**
     * Converts the inventory into a JsonObject. Each item is stacked and
     * concatenated with its quantity.
     *
     * @return JsonObject containing all needed informations on the inventory
     */
    @Override
    public JsonObject save()
    {
        JsonArray items = new JsonArray();
        Map<Item, Long> regrouped = regroup();
        regrouped.keySet().forEach(i -> items.add(i.getName() + ":" + regrouped.get(i)));

        return new JsonObject().add("money", pokeDollars)
                .add("items", items);
    }
    // </editor-fold>
}
