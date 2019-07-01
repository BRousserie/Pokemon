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
import Map.Zone;
import Pokemons.CapturedPkmn;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Class allowing to create playable dressors (adds a list of stored pokemons)
 */
public class Player extends Trainer implements Savable<JsonObject>
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    // Player's captured pokemons, accessible by their ID
    private final ArrayList<CapturedPkmn> myPokemons;
    
    // List of items found, linked to the name of the zone where it was
    private final HashMap<String, ArrayList<String>> foundItems;
    
    // Name of the first Pokemon the player has choosen
    private String starter;
    
    // Number of fights won per arena
    private HashMap<String, Integer> wonFightsInArena = new HashMap<>();
    
    // Zone of the last PokeCenter the player has used
    private Zone lastPokeCenter;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Constructor for a new player
     *
     * @param name Name of the new player
     */
    public Player(String name)
    {
        super(name, 5000);
        myPokemons = new ArrayList<>();
        foundItems = new HashMap<>();
    }

    /**
     * Constructor for a player loaded from a saved game
     * 
     * @param save Previously saved game, parsed to Json
     * @throws ReaderException this uses datas from file to create instances
     *                         of Captured Pokemon.
     */
    public Player(JsonObject save) throws ReaderException
    {
        super(save.get("dressor").asObject());

        myPokemons = new ArrayList<>();
        for (JsonValue pokemon : save.get("myPokemons").asArray())
            addNewPokemon(pokemon.asObject());

        foundItems = new HashMap<>();
        for (JsonValue foundItemValue : save.get("foundItems").asArray()) {
            String foundItem = foundItemValue.asString();
            this.addFoundItem(foundItem.substring(0, foundItem.indexOf(":")),
                              foundItem.substring(foundItem.indexOf(":") + 1));
        }

        starter = save.getString("starter", null);

        wonFightsInArena = new HashMap<>();
        for (JsonValue arenaInfos : save.get("arenas").asArray()) {
            String arena = arenaInfos.asString();
            wonFightsInArena.put(arena.substring(0, arena.indexOf(",")),
                                 Integer.parseInt(arena.substring(arena.indexOf(",") + 1)));
        }

        lastPokeCenter = Game.getGame().getDatas()
                .getLoadedZone(save.getString("pokecenter", "BOURG PALETTE"));
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and setters">
    /**
     * Gets the list of Pokemon captured by this player, not modifiable
     * 
     * @return the list of Pokemon captured by this player
     */
    public List<CapturedPkmn> getMyPokemons()
    {
        return Collections.unmodifiableList(myPokemons);
    }

    /**
     * Gets the list of Pokemon captured by this player, not modifiable,
     * of type CapturedPkmn
     * 
     * @return
     */
    public List<CapturedPkmn> getCapturedCurrentPkmns()
    {
        Object[] objectArray = currentPkmns.stream()
                .map(e -> myPokemons.get(e.getID()))
                .toArray();
        ArrayList<CapturedPkmn> capturedPokemonsList = new ArrayList<>();
        for (int i = 0; i < objectArray.length; i++)
            capturedPokemonsList.add((CapturedPkmn) objectArray[i]);
        return Collections.unmodifiableList(capturedPokemonsList);
    }

    /**
     * Gets the name of the first Pokemon choosed by the player
     * 
     * @return the name of the first Pokemon choosed by the player
     */
    public String getStarter()
    {
        return starter;
    }

    /**
     * Gets the Zone of the latest PokeCenter where the player went
     * 
     * @return the Zone of the latest PokeCenter where the player went
     */
    public Zone getLastPokeCenter()
    {
        return lastPokeCenter;
    }

    /**
     * Tests if the player has the specified item in his bag
     * 
     * @param test The item to be contained
     * @return True if the player has got this item
     */
    public boolean getCondition(String test)
    {
        return bag.contains(test);
    }
    
    /**
     * Tests if the player has reached the specified event
     * 
     * @param event number of the event to be reached
     * @return True if the player has reached this event
     */
    public boolean getCondition(int event)
    {
        return (event < Game.getGame().getCurrentStoryEvent());
    }

    /**
     * Gets the list of items found by the player, linked to the zone they were
     * 
     * @return the list of items found by the player, grouped by zone
     */
    public HashMap<String, ArrayList<String>> getFoundItems()
    {
        return foundItems;
    }

    /**
     * Considering where the player is, gets the number of the next dressor
     * to fight in the arena of the zone, or 0.
     * 
     * @return the number of the next dressor to fight in the local arena
     */
    public int getNextDressorToFight()
    {
        String arena = Game.getGame().getCurrentZone().getName();
        return wonFightsInArena.containsKey(arena)
               ? wonFightsInArena.get(arena)
               : 0;
    }

    /**
     * Sets the value of the first Pokemon the player has choosed
     * 
     * @param starter name of the player's first pokemon
     */
    public void setStarter(String starter)
    {
        this.starter = starter;
        try {
            addNewPokemon(starter, 5);
        } catch (ReaderException ex) {
            System.err.println("Couldn't choose " + starter);
            ex.printStackTrace();
        }
    }

    /**
     * Sets the value of the last PokeCenter the player went to
     * 
     * @param pokeCenterZone zone of the last PokeCenter
     */
    public void setLastPokeCenter(Zone pokeCenterZone)
    {
        lastPokeCenter = pokeCenterZone;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * Restores all HP, PP and clears Status of all carried Pokemon
     */
    public void healPokemon()
    {
        lastPokeCenter = Game.getGame().getCurrentZone();
        for (FightingPkmn pkmn : currentPkmns) {
            for (int i = 0; i < pkmn.getStatsVariations().length; i++)
                pkmn.getStatsVariations()[i] = 0;
            for (Attack atk : pkmn.getAttacks())
                if (atk != null)
                    atk.restorePP(atk.getPPMAX());
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
    public void addNewPokemon(String name, int level) throws ReaderException
    {
        myPokemons.add(new CapturedPkmn(name, level, myPokemons.size()));
        if (currentPkmns.size() < Trainer.MAX_CURRENTPKMNS)
            currentPkmns.add(myPokemons.get(myPokemons.size() - 1));
    }

    /**
     * Adds a FightingPkmn (most likely a wild pokemon you've juste captured)
     * to your Pokemon's list.
     * 
     * @param newpkmn Pokemon to add
     * @throws ReaderException if the Pokemon's not found in database
     */
    public void addNewPokemon(FightingPkmn newpkmn) throws ReaderException
    {
        myPokemons.add(new CapturedPkmn(newpkmn));
        if (currentPkmns.size() < Trainer.MAX_CURRENTPKMNS)
            currentPkmns.add(myPokemons.get(myPokemons.size() - 1));
    }

    /**
     * Adds a previously saved Pokemon to your Captured Pokemon's list, 
     * from the JsonObject of the saved Pokemon
     * 
     * @param newPkmn new Pokemon to add
     * @throws ReaderException uf the Pokemon's not found in database
     */
    public void addNewPokemon(JsonObject newPkmn) throws ReaderException
    {
        myPokemons.add(new CapturedPkmn(newPkmn));
        if (currentPkmns.size() < Trainer.MAX_CURRENTPKMNS)
            currentPkmns.add(myPokemons.get(myPokemons.size() - 1));
    }

    /**
     * Adds the item to your bag and registers the fact that you've found
     * this item, to avoid you to pick it up again
     * 
     * @param zone zone where the item's been found
     * @param item name of the item found
     */
    public void findsItem(String zone, String item)
    {
        bag.addItem(Game.getGame().getDatas().getItem(item));
        addFoundItem(zone, item);
    }
    
    private void addFoundItem(String zone, String item)
    {
        ArrayList<String> items = new ArrayList<>();
        try {
            items.addAll(foundItems.get(zone));
        } catch (NullPointerException e) {
        }
        items.add(item);
        foundItems.put(zone, items);
    }
    
    /**
     * Increments the number of the next dressor to fight in this arena
     * 
     * @param Arena arena where the player's won a fight
     */
    public void wonAFightIn(String Arena)
    {
        if (wonFightsInArena.containsKey(name))
            wonFightsInArena.put(Arena, wonFightsInArena.get(Arena) + 1);
        else
            wonFightsInArena.put(Arena, 1);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Save to Json">
    /**
     * Converts the player into a JsonObject.
     *
     * @return JsonObject containing all needed informations on the player
     */
    @Override
    public JsonObject save()
    {
        JsonArray myPokemons = new JsonArray();
        for (CapturedPkmn pkmn : this.myPokemons)
            myPokemons.add(pkmn.save());

        JsonArray foundItems = new JsonArray();
        for (String zoneName : this.foundItems.keySet())
            for (String item : this.foundItems.get(zoneName))
                foundItems.add(zoneName + ":" + item);

        JsonArray arenasAchievements = new JsonArray();
        for (String arena : this.wonFightsInArena.keySet())
            arenasAchievements.add(arena + "," + wonFightsInArena.get(arena));
        return new JsonObject().add("dressor", super.save())
                .add("myPokemons", myPokemons)
                .add("foundItems", foundItems)
                .add("starter", starter)
                .add("arenas", arenasAchievements)
                .add("pokecenter", lastPokeCenter.getName());
    }
    // </editor-fold>
}
