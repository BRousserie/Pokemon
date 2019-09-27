/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <brousserie@iut.u-bordeaux.fr> and <tpedrero@iut.u-bordeaux.fr>
 * wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy us a beer in return Baptiste and Tony
 * ----------------------------------------------------------------------------
 */
package Pokemons;

import Item.Item;

/**
 *
 * @author Baptiste
 */
public class Evolution {

    private final String name;
    private final String condition;
    
    // <editor-fold defaultstate="collapsed" desc="Contructor">

    /**
     * Constructor of the class Evolution
     *
     * @param evolution
     */
    public Evolution(String evolution) {
        if (!evolution.equals("")) {
            int cut = evolution.indexOf("-");
            this.name = evolution.substring(0, cut);
            this.condition = evolution.substring(cut + 1);
        } else {
            name = "stop";
            condition = "";
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Method">
    /**
     * Sets the evolving conditions
     *
     * @param level
     * @param item
     * @return
     */
    public String evolve(int level, Item item) {
        if (name.equals("stop")) {
            return "";
        }
        if (condition.contains("PIERRE")) {
            if (item.getName() == condition) {
                return name;
            } else {
                return "";
            }
        } else {
            if (Integer.parseInt(condition) <= level) {
                return name;
            } else {
                return "";
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters / Setters">
    /**
     * Gets the pokemon name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the condition to evolve
     *
     * @return the condition
     */
    public String getCondition() {
        return condition;
    }
    // </editor-fold>
}
