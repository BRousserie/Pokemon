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

import Character.Trainer;
import Fight.FightingPkmn;
import FileIO.DataReader;
import FileIO.ReaderException;
import GameEngine.Game;
import GameEngine.TrainerFight;
import Item.Item;
import java.util.ArrayList;

/**
 *
 * @author brousserie
 */
public class Arena {

    // <editor-fold defaultstate="collapsed" desc="Attributes">
    /**
     * ArrayList of the dressors in an arena
     */
    protected final ArrayList<Trainer> dressors;

    /**
     * Condition to enter an arena
     */
    protected final String prerequisites;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Contructor">
    /**
     * Constructor of the class arena
     *
     * @param dressors
     * @param prerequisites
     */
    public Arena(ArrayList<Trainer> dressors, String prerequisites) {
        this.dressors = dressors;
        this.prerequisites = prerequisites;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters / Setters">
    /**
     * Gets the condition to enter
     *
     * @return the condition
     */
    public String getPrerequisites() {
        return prerequisites;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Other methods">
    /**
     * Stars the fight against a dressor
     */
    public void fight() {
        Game.getGame().setFight(new TrainerFight(
                dressors.get(Game.getGame().getPlayer().getNextDressorToFight()), true));
    }

    /**
     * Says if the conditions are respected
     *
     * @return
     */
    public boolean isNextArena() {
        return Game.getGame().getPlayer().getCondition(prerequisites);
    }

    /**
     * Loads the arenas of the game from files
     * @param arena
     * @return a new arena
     * @throws ReaderException
     */
    public static Arena loadArenaFromFile(String arena) throws ReaderException {
        ArrayList<Trainer> dressors = new ArrayList<>();
        String[] arenaInfos = DataReader.readFileArray("arenas", arena);
        int i = 2;
        while (i < arenaInfos.length) {
            if (arenaInfos[i] != "@rival") {
                String name = arenaInfos[i];
                i++;

                ArrayList<FightingPkmn> pokemon = new ArrayList<>();
                ArrayList<Item> items = new ArrayList<>();
                int id = 0;

                while (i < arenaInfos.length
                        && !arenaInfos[i].equals("TRAINER")
                        && !arenaInfos[i].equals("ITEMS")) {
                    pokemon.add(pkmnFromData(arenaInfos[i], id));
                    i++;
                    id++;
                }
                i = (arenaInfos[i].equals("ITEMS")) ? ++i : i;
                while (i < arenaInfos.length
                        && !arenaInfos[i].equals("TRAINER")) {
                    items.add(Game.getGame().getDatas().getItem(arenaInfos[i]));
                    i++;
                }
                i++;
                dressors.add(new Trainer(name, pokemon, items));
            } else {
                dressors.add(Game.getGame().getRival());
            }
        }
        return new Arena(dressors, arenaInfos[1]);
    }

    
    private static FightingPkmn pkmnFromData(String pkmn, int id) {
        return new FightingPkmn(pkmn.substring(0, pkmn.indexOf(",")),
                Integer.parseInt(pkmn.substring(pkmn.indexOf(",") + 1)),
                id);
    }
    // </editor-fold>
}
