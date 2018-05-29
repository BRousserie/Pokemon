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

import Fight.Attack;
import Fight.FightingPkmn;
import Fight.Status;
import FileIO.ReaderException;
import FileIO.Savable;
import GameEngine.Game;
import Item.Item;
import Map.Zone;
import Pokemons.CapturedPkmn;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class allowing to create playable dressors (adds a list of stored pokemons)
 */
public class Player extends Trainer implements Savable<JsonObject>{

    //Player's captured pokemons, accessible by their ID
    private ArrayList<CapturedPkmn> myPokemons;
    private HashMap<String, ArrayList<String>> foundItems;
    private String starter;
    private HashMap<String, Integer> wonFightsInArena = new HashMap<>();
    private Zone lastPokeCenter;

    /**
     * Constructor for a new player
     *
     * @param name
     */
    public Player(String name) {
        super(name, new ArrayList<>(), 5000);
        myPokemons = new ArrayList<>();
        foundItems = new HashMap<>();
    }
    
    public Player(JsonObject save) throws ReaderException {
        super(save.get("dressor").asObject());
        
        myPokemons = new ArrayList<>();
        for(JsonValue pokemon : save.get("myPokemons").asArray()) {
            addNewPokemon(pokemon.asObject());
        }
        
        foundItems = new HashMap<>();
        for(JsonValue foundItemValue : save.get("foundItems").asArray()) {
            String foundItem = foundItemValue.asString();
            this.findsItem(foundItem.substring(0, foundItem.indexOf(":")),
                           foundItem.substring(foundItem.indexOf(":")+1));
        }
        
        starter = save.getString("starter", null);
    }

    /**
     * Constructor for a player loaded from save file
     *
     * @param name
     * @param currentPkmns
     * @param items
     * @param money
     * @param myPokemons
     */
    public Player(String name, ArrayList<FightingPkmn> currentPkmns,
            HashMap<Item, Integer> items, int money,
            ArrayList<CapturedPkmn> myPokemons) {
        super(name, currentPkmns, items, money);
        this.myPokemons = myPokemons;
    }

    /**
     * @return all the player's captured pokemons, accessible by ID
     */
    public ArrayList<CapturedPkmn> getMyPokemons() {
        return myPokemons;
    }

    public String getStarter() {
        return starter;
    }
    
    public Zone getLastPokeCenter() {
        return lastPokeCenter;
    }

    public void setStarter(String starter) {
        this.starter = starter;
        try {
            addNewPokemon(starter, 5);
        } catch (ReaderException ex) {
            System.out.println("Couldn't choose "+starter);
        }
    }

    public void healPokemon() {
        lastPokeCenter = Game.getGame().getCurrentZone();
        for (FightingPkmn pkmn : currentPkmns) {
            for (int i = 0; i < pkmn.getStatsVariations().length; i++) {
                pkmn.getStatsVariations()[i] = 0;
            }
            for (Attack atk : pkmn.getAttacks()) {
                if (atk != null) atk.restorePP(atk.getPPMAX());
            }
            pkmn.setPkmnStatus(Status.OK);
        }
    }
    
    /**
     * Stores a new CapturedPokemon to myPokemons, from a FightingPokemon
     *
     * @param name species of the CapturedPokemon
     * @param level level of the CapturedPokemon
     * @throws ReaderException if the pokemon species isn't found in data
     */
    public void addNewPokemon(String name, int level) throws ReaderException {
        myPokemons.add(new CapturedPkmn(name, level, myPokemons.size()));
        if (currentPkmns.size() < Trainer.MAX_CURRENTPKMNS) {
            currentPkmns.add(myPokemons.get(myPokemons.size() - 1));
        }
    }
    
    public void addNewPokemon(FightingPkmn newpkmn) throws ReaderException {
        myPokemons.add(new CapturedPkmn(newpkmn));
        if (currentPkmns.size() < Trainer.MAX_CURRENTPKMNS) {
            currentPkmns.add(myPokemons.get(myPokemons.size() - 1));
        }
    }
    
    public void addNewPokemon(JsonObject newPkmn) throws ReaderException {
        myPokemons.add(new CapturedPkmn(newPkmn));
        if (currentPkmns.size() < Trainer.MAX_CURRENTPKMNS) {
            currentPkmns.add(myPokemons.get(myPokemons.size() - 1));
        }
    }
    
    public ArrayList<CapturedPkmn> getCapturedCurrentPkmns() {
        Object[] objectArray = currentPkmns.stream()
                .map(e -> myPokemons.get(e.getID()))
                .toArray();
        ArrayList<CapturedPkmn> capturedPokemonsList = new ArrayList<>();
        for (int i = 0; i < objectArray.length; i++) {
            capturedPokemonsList.add((CapturedPkmn)objectArray[i]);
        }
        return capturedPokemonsList;
    }

    public boolean getCondition(String test) {
        if (bag.contains(test))
            return true;
        try {
            if (Integer.parseInt(test) < Game.getGame().getCurrentStoryEvent())
                return true;
        } catch (NumberFormatException e) {}
        return false;
    }

    public void findsItem(String zone, String item) {
        bag.addItem(Game.getGame().getDatas().getItem(item));
        ArrayList<String> items = new ArrayList<>();
        try {
            items.addAll(foundItems.get(zone));
        } catch (NullPointerException e) {}
        items.add(item);
        foundItems.put(zone, items);
    }

    public HashMap<String, ArrayList<String>> getFoundItems() {
        return foundItems;
    }

    public int getNextDressorToFight() {
        String arena = Game.getGame().getCurrentZone().getName();
        return wonFightsInArena.containsKey(arena) 
                ? wonFightsInArena.get(arena)
                : 0;
    }
    
    @Override
    public JsonObject save() {
        JsonArray myPokemons = new JsonArray();
        for(CapturedPkmn pkmn : this.myPokemons) myPokemons.add(pkmn.save());
        
        JsonArray foundItems = new JsonArray();
        for(String zoneName : this.foundItems.keySet()) {
            for(String item : this.foundItems.get(zoneName)) {
                foundItems.add(zoneName+":"+item);
            }
        }
        return new JsonObject().add("dressor", super.save())
                               .add("myPokemons", myPokemons)
                               .add("foundItems", foundItems)
                               .add("starter", starter);
    }

    public void wonAFightIn(String Arena) {
        if (wonFightsInArena.containsKey(name)) {
            wonFightsInArena.put(Arena, wonFightsInArena.get(Arena)+1);
        } else {
            wonFightsInArena.put(Arena, 1);
        }
    }
}
