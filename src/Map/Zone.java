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

    protected final String name;
    protected final String description;
    protected final int meetingDressorProba;
    protected final ArrayList<String> accessibleZones;
    protected final ArrayList<String> items;
    protected final ArrayList<WildPkmn> fishablePkmns;
    

    Zone(String name, int meetingDressorProba,String description, ArrayList<String> accessibleZones,
            ArrayList<String> items, ArrayList<WildPkmn> fishablePkmns) {
        this.name = name;
        this.meetingDressorProba = meetingDressorProba;
        this.description = description;
        this.accessibleZones = accessibleZones;
        this.items = items;
        try {
            this.items.removeAll(Game.getGame().getPlayer().getFoundItems().get(name));
        } catch (Exception e) {
        }
        this.fishablePkmns = fishablePkmns;
        
    }

    public String getName() {
        return name;
    }
    
    public String getDescription(){
        return description;
    }
    
    public List<String> getAccessibleZones() {
        return accessibleZones.stream()
                .filter(this::unlocked)
                .collect(Collectors.toList());
    }
    
    private boolean unlocked(String zone) {
        if (zone.contains("-")) {
            return Game.getGame().getPlayer()
                    .getCondition(zone.substring(zone.indexOf("-")+1));
        } else {
            return true;
        }
    }

    public int getMeetingDressorProba() {
        return meetingDressorProba;
    }

    public abstract void searchWildPokemon();
    public abstract void searchWildFish();
    public abstract String getZoneType();
    public abstract int getMeetingPkmnProba() ;
    public abstract ArrayList<String> getShop();
    public abstract boolean hasArena();
    public abstract boolean hasPokeCenter();

    public String searchItems() {
        if (!items.isEmpty()) {
            Game.getGame().getPlayer().findsItem(name, items.get(0));
            return Game.getGame().getPlayer().getName() + " a reçu " + items.remove(0);
        } else {
            return  Game.getGame().getPlayer().getName() + " n'a rien trouvé ";
        }
    }

    
}
