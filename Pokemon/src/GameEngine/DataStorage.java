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
import Map.Arena;
import Map.SpecialZone;
import Map.Town;
import Map.WildZone;
import Map.Zone;
import Pokemons.PkmnStats;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Baptiste
 */
public class DataStorage {

    // <editor-fold defaultstate="collapsed" desc="Attributes">
    /**
     * The game contains planty datas (151 Pokemons, 165 Attacks, 25 Roads, 11
     * Towns, 8 Arenas...) containing many different attributes (sometimes
     * others game datas), but the player won't have to load a quarter of these
     * to play a game session. However, he's going to access the same datas
     * multiple time (for example his Pokemons and their attacks will be used on
     * each fight) Thus, Pokemon, Attack and Zone datas will be loaded
     * individually, and be stored for not to load it from database again next
     * time.
     */
    private HashMap<String, PkmnStats> loadedPkmns = new HashMap<>();
    private HashMap<String, Attack> loadedAtks = new HashMap<>();
    private HashMap<String, Zone> loadedZones = new HashMap<>();
    private HashMap<String, Arena> loadedArenas = new HashMap<>();

    /**
     * Items will also not all be used. However, they are just composed of a
     * short String for the name, a pretty short one for the description and and
     * Integer for the price. Moreover, they are not as numberous as Pokemons
     * and Attacks. Thus, they won't be long to load nor heavy to store, so
     * we've choosed to load them all when launching the game.
     */
    private HashMap<String, Item> items = new HashMap<>();
    private ArrayList<Scenario> scenario = new ArrayList<>();

    private List<String> towns = new ArrayList<>();
    private List<String> specials = new ArrayList<>();
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * DataStorage contructor
     */
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
        Collections.addAll(towns, "ARGENTA", "AZURIA", "BOURG PALETTE", "CARMIN-SUR-MER", "CELADOPOLE", "CRAMOIS'ILE", "JADIELLE", "LAVANVILLE", "PARMANIE", "PLATEAU INDIGO", "SAFRANIA");
        Collections.addAll(specials, "CENTRALE", "FORET DE JADE", "GROTTE", "GROTTE INCONNUE", "ILES ECUME", "MANOIR DE CRAMOIS'ILE", "L'OCEANE", "PARC SAFARI", "REPERE ROCKET DU CASINO", "ROUTE VICTOIRE", "SIEGE SOCIAL DE LA SYLPHE SARL", "SOUTERRAIN EST-OUEST", "SOUTERRAIN NORD-SUD", "TOUR POKEMON", "TUNNEL TAUPIQUEUR", "VILLA DE LEO");
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters">
    /**
     * Gets the name of the item 
     * @param name the name  of an item
     * @return i the item name
     */
    public Item getItem(String name) {
        Item i = items.get(name);
        if (i == null) {
            throw new RuntimeException("il n'existe pas d'item " + name);
        }
        return i;
    }

    /**
     * Gets the list of events 
     * @return the scenario
     */
    public ArrayList<Scenario> getScenario() {
        return scenario;
    }

    /**
     * Gets the pokemon with the name or add the pokemon to loaded ones
     * @param pkmnName 
     * @return the pokemon stats
     * @throws ReaderException
     */
    public PkmnStats getLoadedPkmn(String pkmnName) throws ReaderException {
        if (loadedPkmns.containsKey(pkmnName)) {
            return loadedPkmns.get(pkmnName);
        } else {
            loadedPkmns.put(pkmnName, PkmnStats.loadPkmnStatsFromFile(pkmnName));
            return loadedPkmns.get(pkmnName);
        }
    }

    /**
     * Gets the loaded attacks or add it to laded ones
     * @param atkName
     * @return the loaded attack 
     * @throws ReaderException
     */
    public Attack getLoadedAtk(String atkName) throws ReaderException {
        if (loadedAtks.containsKey(atkName)) {
            return loadedAtks.get(atkName);
        } else {
            atkName = DataReader.normalyzeUpperOnFirst(atkName);
            loadedAtks.put(atkName, Attack.loadAtkFromFile(atkName));
            return loadedAtks.get(atkName);
        }
    }

    /**
     * Gets the zone or add it to loaded ones
     * @param zoneName
     * @return the loaded zones
     * @throws ReaderException
     */
    public Zone getLoadedZone(String zoneName) throws ReaderException {
        if (loadedZones.containsKey(zoneName)) {
            return loadedZones.get(zoneName);
        } else {
            if (towns.contains(zoneName)) {
                loadedZones.put(zoneName, Town.loadZoneFromFile(zoneName));
            } else if (specials.contains(zoneName)) {
                loadedZones.put(zoneName, SpecialZone.loadZoneFromFile(zoneName));
            } else if (zoneName.startsWith("ROUTE") || zoneName.startsWith("CHENAL")) {
                loadedZones.put(zoneName, WildZone.loadZoneFromFile(zoneName));
            } else {
                System.out.println("La zone n'existe pas dans la base de donnée");
            }
            return loadedZones.get(zoneName);
        }
    }

    /**
     *  Gets the loaded arena in the zones
     * @return the loaded arenas
     * @throws ReaderException
     */
    public Arena getLoadedArena() throws ReaderException {
        String zoneName = Game.getGame().getCurrentZone().getName();
        if (loadedArenas.containsKey(zoneName)) {
            return loadedArenas.get(zoneName);
        } else {
            try {
                loadedArenas.put(zoneName, Arena.loadArenaFromFile(zoneName));
            } catch (Exception e) {
                System.out.println("L'arène de " + zoneName + " n'a pas été trouvée.");
            }
            return loadedArenas.get(zoneName);
        }
    }
    // </editor-fold>
}
