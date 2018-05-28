/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <brousserie@iut.u-bordeaux.fr> and <tpedrero@iut.u-bordeaux.fr>
 * wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy us a beer in return Baptiste and Tony
 * ----------------------------------------------------------------------------
 */
package GameEngine;

import Fight.Attack;
import FileIO.DataReader;
import FileIO.ReaderException;
import Item.Item;
import Map.Town;
import Map.WildZone;
import Map.Zone;
import Pokemons.PkmnStats;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Baptiste
 */
public class DataStorage {

    /*The game contains planty datas (151 Pokemons, 165 Attacks, 25 Roads,
    * 11 Towns, 8 Arenas...) containing many different attributes (sometimes
    * others game datas), but the player won't have to load a quarter of these
    * to play a game session. However, he's going to access the same datas
    * multiple time (for example his Pokemons and their attacks will be used
    * on each fight)
    * Thus, Pokemon, Attack and Zone datas will be loaded individually,
    * and be stored for not to load it from database again next time. 
     */
    private HashMap<String, PkmnStats> loadedPkmns = new HashMap<>();
    private HashMap<String, Attack> loadedAtks = new HashMap<>();
    private HashMap<String, Zone> loadedZones = new HashMap<>();

    /*Items will also not all be used. However, they are just composed of a
    * short String for the name, a pretty short one for the description and
    * and Integer for the price. Moreover, they are not as numberous as Pokemons
    * and Attacks.
    * Thus, they won't be long to load nor heavy to store, so we've choosed
    * to load them all when launching the game.
     */
    private HashMap<String, Item> items = new HashMap<>();
    private ArrayList<Scenario> scenario = new ArrayList<>();

    public DataStorage() {
        try {
            items = Item.loadItemsFromFile();
        } catch (ReaderException ex) {
            System.out.println("Items couldn't be loaded properly");
        }
        try {
            scenario = Scenario.loadScenarioFromFile();
        } catch (ReaderException ex) {
            System.out.println("Scenario couldn't be loaded properly");
        }
    }
    
    public Item getItem(String name) {
        Item i= items.get(name);
        if(i== null) throw  new RuntimeException("il n'existe pas d'item " + name);
        return i;
    }

    public ArrayList<Scenario> getScenario() {
        return scenario;
    }
    
    public PkmnStats getLoadedPkmn(String pkmnName) throws ReaderException {
        if (loadedPkmns.containsKey(pkmnName)) {
            return loadedPkmns.get(pkmnName);
        } else {
            loadedPkmns.put(pkmnName, PkmnStats.loadPkmnStatsFromFile(pkmnName));
            return loadedPkmns.get(pkmnName);
        }
    }

    public Attack getLoadedAtk(String atkName) throws ReaderException {
        if (loadedAtks.containsKey(atkName)) {
            return loadedAtks.get(atkName);
        } else {
            atkName = DataReader.normalyzeUpperOnFirst(atkName);
            loadedAtks.put(atkName, Attack.loadAtkFromFile(atkName));
            return loadedAtks.get(atkName);
        }
    }

    public Zone getLoadedZone(String zoneName) throws ReaderException {
        if (loadedZones.containsKey(zoneName)) {
            return loadedZones.get(zoneName);
        } else {
            try {
                loadedZones.put(zoneName, WildZone.loadZoneFromFile(zoneName));
            } catch (Exception wild) {
                try {
                    loadedZones.put(zoneName, Town.loadZoneFromFile(zoneName));
                } catch (Exception town) {}
            }
            return loadedZones.get(zoneName);
        }
    }
}
