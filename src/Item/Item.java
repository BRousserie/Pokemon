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
import java.util.HashMap;
import java.util.Objects;

/**
 *
 * @author brousserie
 */
public abstract class Item {
    String name;
    String description;
    int buyPrice;

    public Item(String name, String description, String buyPrice) {
        this.name = name;
        this.description = description;
        this.buyPrice = Integer.parseInt(buyPrice);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName( String n){
        this.name = n;
    }

    public String getDescription() {
        return description;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public int getSellPrice() {
        return buyPrice/2;
    }
    
    
    public static HashMap<String, Item> loadItemsFromFile() throws ReaderException {
        HashMap<String, Item> items = new HashMap<>();
        
        DataReader.readFileArray("balls").forEach(itemDatas -> 
                items.put(itemDatas[0], new Ball(itemDatas)));
        DataReader.readFileArray("CTCS").forEach(itemDatas -> 
                items.put(itemDatas[0], new CTCS(itemDatas)));
        DataReader.readFileArray("potions").forEach(itemDatas -> 
                items.put(itemDatas[0], new Potion(itemDatas)));
        DataReader.readFileArray("rares").forEach(itemDatas -> 
                items.put(itemDatas[0], new RareItems(itemDatas)));
        DataReader.readFileArray("stones").forEach(itemDatas -> 
                items.put(itemDatas[0], new Stone(itemDatas)));
        
        return items;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Item other = (Item) obj;
        if (this.buyPrice != other.buyPrice) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        return true;
    }
}
