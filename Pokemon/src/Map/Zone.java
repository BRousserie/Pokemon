/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <brousserie@iut.u-bordeaux.fr> and <tpedrero@iut.u-bordeaux.fr>
 * wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy us a beer in return Baptiste and Tony
 * ----------------------------------------------------------------------------
 */
package Map;

import GameEngine.Game;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Baptiste
 */
public abstract class Zone {

    // <editor-fold defaultstate="collapsed" desc="Attributes">
    /**
     * The name of the zone
     */
    protected final String name;

    /**
     * The description of the zone
     */
    protected final String description;

    /**
     * The porbability of meeting a dressor
     */
    protected final int meetingTrainerProba;

    /**
     * List of accessible zones
     */
    protected final ArrayList<String> accessibleZones;

    /**
     * List of items in the zone
     */
    protected final ArrayList<String> items;

    /**
     * List of fishable pokemons
     */
    protected final ArrayList<WildPkmn> fishablePkmns;
// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Contructor">
    /**
     * Construtor of the Class Zone
     *
     * @param name
     * @param meetingTrainerProba
     * @param description
     * @param accessibleZones
     * @param items
     * @param fishablePkmns
     */
    Zone(String name, int meetingTrainerProba, String description, ArrayList<String> accessibleZones,
            ArrayList<String> items, ArrayList<WildPkmn> fishablePkmns) {
        this.name = name;
        this.meetingTrainerProba = meetingTrainerProba;
        this.description = description;
        this.accessibleZones = accessibleZones;
        this.items = items;
        try {
            this.items.removeAll(Game.getGame().getPlayer().getFoundItems().get(name));
        } catch (Exception e) {
        }
        this.fishablePkmns = fishablePkmns;

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters / Setters">
    /**
     * Gets the zone name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * gets the description of the zone
     *
     * @return the zone description
     */
    public String getDescription() {
        return description;
    }

    /**
     * gets the access
     *
     * @return the access
     */
    public List<String> getAccessibleZones() {
        return accessibleZones.stream()
                .filter(this::unlocked)
                .map(this::removeCondition)
                .collect(Collectors.toList());
    }

    /**
     * Gets the probability of meeting a pokemon
     * @return
     */
    public int getMeetingTrainerProba() {
        return meetingTrainerProba;
    }
    
    /**
     * gets the zone type
     * @return type of zone
     */
    public abstract String getZoneType();

    /**
     * Gets the meeting proba
     * @return proba
     */
    public abstract int getMeetingPkmnProba();

    /**
     * Gets the shop
     * @return shop
     */
    public abstract ArrayList<String> getShop();

    /**
     * Gets the arena
     * @return arena
     */
    public abstract boolean hasArena();

    /**
     * gets the pokeCenter
     * @return pokeCenter
     */
    public abstract boolean hasPokeCenter();
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * Gets the probability of meeting a pokemon when surfing
     */
    public void searchWildFish() {
    }

    /**
     *  Finds a wild pokemon
     */
    public abstract void searchWildPokemon();

    
    /**
     * Remove the access condition
     *
     * @param accessibleZones
     * @return accessibleZone
     */
    private String removeCondition(String accessibleZones) {
        if (accessibleZones.contains("-")) {
            accessibleZones = accessibleZones.substring(0, accessibleZones.indexOf("-"));
        }
        return accessibleZones;
    }

    /**
     *
     * Says if the zone is unlocked
     *
     * @param zone
     * @return true or false
     */
    private boolean unlocked(String zone) {
        if (zone.contains("-")) {
            if (Game.getGame().getPlayer()
                    .getCondition(zone.substring(zone.indexOf("-") + 1))) return true;
            try {
                if (Game.getGame().getPlayer()
                    .getCondition(Integer.parseInt(zone.substring(zone.indexOf("-") + 1)))) return true;
            } catch (NumberFormatException e) {
            }
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Search for items
     * @return the found item
     */
    public String searchItems() {
        if (!items.isEmpty()) {
            Game.getGame().getPlayer().findsItem(name, items.get(0));
            return Game.getGame().getPlayer().getName() + " a reçu " + items.remove(0);
        } else {
            return Game.getGame().getPlayer().getName() + " n'a rien trouvé ";
        }
    }
// </editor-fold>
}
