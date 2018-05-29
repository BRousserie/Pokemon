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
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * Class allowing to store the items and money of a dressor
 */
public class Inventory implements Savable<JsonObject> {
    //Player's amount of money
    private int pokeDollars;
    //Player's list of items
    private HashMap<Item, Integer> items;

    /**
     * Constructor for creating Inventories for dressors that have
     * money and multiple items
     */
    public Inventory(int pokeDollars, HashMap<Item, Integer> bag) {
        this.pokeDollars = pokeDollars;
        this.items = bag;
    }

    /**
     * Constructor for creating Inventories for dressors that have
     * money and one item
     */
    public Inventory(int pokeDollars, Item item) {
        this.pokeDollars = pokeDollars;
        items = new HashMap<>(); 
        items.put(item, 1);
    }

    /**
     * Constructor for creating Inventories for dressors that only
     * have money
     */
    public Inventory(int pokeDollars) {
        this.pokeDollars = pokeDollars;
        items = new HashMap<>(); 
    }
    
    public Inventory(JsonObject bag) {
        pokeDollars = bag.getInt("money", 0);
        items = new HashMap<>();
        for(JsonValue itemValue : bag.get("items").asArray()) {
            String item = itemValue.asString();
            items.put(Game.getGame().getDatas()
                    .getItem(item.substring(0, item.indexOf(":"))),
                    Integer.parseInt(item.substring(item.indexOf(":")+1)));
        }
    }
    
    public void addItem(Item item) {
        if (contains(item.getName())) {
            items.put(item, items.get(item)+1);
        } else {
            items.put(item, 1);
        }
    }
    
    /**
     * Given an item name, checks if the Inventory contains an item with
     * the same name.
     * @param itemName name the item
     * @return true if the bag contains this item
     */
    public boolean contains(String itemName) {
        return items.keySet().stream()
                .anyMatch(e -> e.getName().contains(itemName));
    }

    /**
     * Update player's money depending on the amount he recieved/gave
     */
    public void exchangeMoney(int monetaryFlux) {
        pokeDollars += monetaryFlux;
    }

    /**
     * @return the player's amount of money 
     */
    public int getPokeDollars() {
        return pokeDollars;
    }

    /**
     * @return the player's list of items
     */
    public HashMap<Item, Integer> getItems() {
        return items;
    }
    
    public Stream getItemsOfType(Class wanted){
        return items.keySet().stream().filter(e -> e.getClass().equals(wanted));
    }

    public void decrement(Item item) {
        try {
            items.put(item, items.get(item)-1);
            if (items.get(item) == 0) {
                items.remove(item);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JsonObject save() {
        JsonArray items = new JsonArray();
        this.items.keySet()
                .forEach(i -> items.add(i.getName()+":"+this.items.get(i)));
        return new JsonObject().add("money", pokeDollars)
                               .add("items", items);
    }
}
