/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <brousserie@iut.u-bordeaux.fr> and <tpedrero@iut.u-bordeaux.fr>
 * wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy us a beer in return Baptiste and Tony
 * ----------------------------------------------------------------------------
 */
package Item;

import FileIO.DataReader;
import FileIO.ReaderException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author brousserie
 */
public abstract class Item
{

    // <editor-fold defaultstate="collapsed" desc="Attributes">
    String name;
    String description;
    int buyPrice;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Contructor">
    /**
     *
     * @param name
     * @param description
     * @param buyPrice
     */
    public Item(String name, String description, String buyPrice)
    {
        this.name = name;
        this.description = description;
        this.buyPrice = Integer.parseInt(buyPrice);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters / Setters">
    /**
     * Gets the item name
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the desscrition of an item
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Gets the items price
     *
     * @return buy price
     */
    public int getBuyPrice()
    {
        return buyPrice;
    }

    /**
     * Gets the sell price of an item
     * 
     * @return sell price
     */
    public int getSellPrice()
    {
        return buyPrice / 2;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Other methods">
    /**
     * puts the items in a list
     * 
     * @return the list
     */
    public List<Item> asList()
    {
        List<Item> items = new ArrayList<>();
        items.add(this);
        return items;
    }

    /**
     * Loads items from the files and store into an hashmap
     * @return the hashmap
     * @throws ReaderException
     */
    public static HashMap<String, Item> loadItemsFromFile() throws ReaderException
    {
        HashMap<String, Item> items = new HashMap<>();

        DataReader.readFileArray("balls").forEach(itemDatas
                -> items.put(itemDatas[0], new Ball(itemDatas)));
        DataReader.readFileArray("CTCS").forEach(itemDatas
                -> items.put(itemDatas[0] + " " + itemDatas[1], new CTCS(itemDatas)));
        DataReader.readFileArray("potions").forEach(itemDatas
                -> items.put(itemDatas[0], new Potion(itemDatas)));
        DataReader.readFileArray("rares").forEach(itemDatas
                -> items.put(itemDatas[0], new RareItems(itemDatas)));
        DataReader.readFileArray("stones").forEach(itemDatas
                -> items.put(itemDatas[0], new Stone(itemDatas)));

        return items;
    }

    /**
     * Returns a hash code value for the object.
     * @return the code
     */
    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.name);
        return hash;
    }

    /**
     * Says if it's equals
     * @param obj
     * @return true or false
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Item other = (Item) obj;
        if (this.buyPrice != other.buyPrice)
            return false;
        if (!Objects.equals(this.name, other.name))
            return false;
        if (!Objects.equals(this.description, other.description))
            return false;
        return true;
    }
    // </editor-fold>
}
