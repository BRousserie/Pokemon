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
import GameEngine.DressorFight;
import GameEngine.Game;
import Item.Item;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author brousserie
 */
public class Arena {

    private static FightingPkmn pkmnFromData(String pkmn, int id) {
        return new FightingPkmn(pkmn.substring(0, pkmn.indexOf(",")),
                        Integer.parseInt(pkmn.substring(pkmn.indexOf(",")+1)),
                        id);
    }
    private final ArrayList<Trainer> dressors;
    private final String prerequisites;
    public Arena(ArrayList<Trainer> dressors, String prerequisites) {
        this.dressors = dressors;
        this.prerequisites = prerequisites;
    }
    
    public String getPrerequisites() {
        return prerequisites;
    }
    
    public void fight() {
        Game.getGame().setFight(new DressorFight(
                dressors.get(Game.getGame().getPlayer().getNextDressorToFight()), true));
    }

    public boolean isNextArena() {
        return Game.getGame().getPlayer().getCondition(prerequisites);
    }
    
    public static Arena loadArenaFromFile(String arena) throws ReaderException {
        ArrayList<Trainer> dressors = new ArrayList<>();
        String[] arenaInfos = DataReader.readFileArray("arenes", arena);
        int i = 2;
        while(i < arenaInfos.length) {
            int id = 0;
            String name = arenaInfos[i];
            ArrayList<FightingPkmn> pokemon = new ArrayList<>();
            HashMap<Item, Integer> items = new HashMap<>();
            while(!arenaInfos[i].equals("TRAINER") && !arenaInfos[i].equals("ITEMS")) {
                pokemon.add(pkmnFromData(arenaInfos[i], id));
                i++;
                id++;
            }
            while(!arenaInfos[i].equals("TRAINER")) {
                items.put(Game.getGame().getDatas().getItem(arenaInfos[i]), 1);
                i++;
            }
            dressors.add(new Trainer(name, pokemon, items, 200));
        }
        return new Arena(dressors, arenaInfos[1]);
    }
}
