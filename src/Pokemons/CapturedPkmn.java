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

import Fight.Attack;
import Fight.FightingPkmn;
import FileIO.DataReader;
import FileIO.ReaderException;
import FileIO.Savable;
import GameEngine.Game;
import Item.Item;
import com.eclipsesource.json.JsonObject;
import java.util.ArrayList;

/**
 *
 * @author Baptiste
 */
public class CapturedPkmn extends FightingPkmn implements Savable<JsonObject>{
    private String size;
    private String weight;
    private String description;
    private int expCurve;
    private Evolution evolution;
    private int exp;
    private int levelUpXP;
    private Item item;

    public CapturedPkmn(String name, int level, int ID) throws ReaderException {
        super(name, level, ID);
        
        String[] pkmnLine = DataReader.readFileArray("pokemonStats", name);
        
        this.size = pkmnLine[13];
        this.weight = pkmnLine[14];
        this.description = pkmnLine[12];
        this.expCurve = Integer.parseInt(pkmnLine[8]);
        this.evolution = new Evolution(pkmnLine[10]);
        determinateXP();
        item = null;
    }

    public CapturedPkmn(String name, int level, int ID, int[] IV, int[] EV, ArrayList<Attack> attacks, int exp, Item item) throws ReaderException {
        super(name, level, ID, IV, EV, attacks);
        
        String[] pkmnLine = DataReader.readFileArray("pokemonStats", name);
        
        this.size = pkmnLine[13];
        this.weight = pkmnLine[14];
        this.description = pkmnLine[12];
        this.expCurve = Integer.parseInt(pkmnLine[8]);
        this.evolution = new Evolution(pkmnLine[10]);
        determinateXP();
        this.exp = exp;
        this.item = item;
    }

    public CapturedPkmn(FightingPkmn newpkmn) throws ReaderException {
        super(newpkmn);
        
        String[] pkmnLine = DataReader.readFileArray("pokemonStats", newpkmn.getName());
        
        this.size = pkmnLine[13];
        this.weight = pkmnLine[14];
        this.description = pkmnLine[12];
        this.expCurve = Integer.parseInt(pkmnLine[8]);
        this.evolution = new Evolution(pkmnLine[10]);
        determinateXP();
        item = null;
    }

    public CapturedPkmn(JsonObject pokemon) throws ReaderException {
        super(pokemon.get("fightingPkmn").asObject());
        determinateXP();
        exp = pokemon.getInt("exp", 0);
        if (pokemon.getString("item", null) != null) {
            item = Game.getGame().getDatas().getItem(pokemon.getString("item", null));
        }
    }
    
    
    
    private void determinateXP() {
        int level = super.getLevel();
        switch (expCurve) {
            case (1): //courbe "rapide", cf pokepedia dans la doc
                levelUpXP = (int) (0.8 * (level * level * level));
                break;
            case (2): //courbe "moyenne", cf pokepedia dans la doc
                levelUpXP = level * level * level;
                break;
            case (3): //courbe "parabolique", cf pokepedia dans la doc
                levelUpXP = (int) (1.2 * (level * level * level) - 15 * (level * level) + 100 * level - 140);
                break;
            case (4): //courbe "lente", cf pokepedia dans la doc
                levelUpXP = (int) (1.25 * (level * level * level));
                break;
            default :
                levelUpXP = (level * level * level);
        }
        exp = 0;
    }
    
    //      METHODS FOR LEVELING AND EVOLVING
    /**
     * Set the value of exp depending on an amount earned points
     */
    public void earnExp(int earnedPoints) {
        exp += earnedPoints;
        if (exp >= levelUpXP) {
            int remainingExp = exp - levelUpXP;
            try {
            levelUp();
            } catch (Exception e) {
                
            }
            earnExp(remainingExp);
        }
    }
    
    /**
     * Get the amount of experience points needed to level Up
     */
    public int getLevelUpXP() {
        return levelUpXP;
    }
    
    /**
     * Set the value of level and updates the value of all the pokemon's stats
     */
    public void levelUp() throws ReaderException {
        super.incrementLevel();
        exp = 0;
        super.determinateStats();
        String evolution = this.evolution.evolve(super.getLevel(), item);
        if (!"".equals(evolution)) {
            doEvolve(evolution);
        }
        determinateXP();
        //checkAtkLearning();
    }
    
    
    
    @Override
    protected void doEvolve(String evolution) {
        try {
            super.doEvolve(evolution);
        
        String[] infos = DataReader.readFileArray("pokemonStats", evolution);
        this.size = infos[0];
        this.weight = infos[1];
        this.description = infos[2];
        this.expCurve = Integer.parseInt(infos[3]);
        this.evolution = new Evolution(infos[4]);
        } catch (ReaderException ex) {
        }
    }
    
    @Override
    public String toString() {
        return pkmnSpecies.getName() 
                + " Niv" + level + " " 
                + this.getHp() +"/" + this.getMaxHP() + "HP";
    }
    
    public String getSize() {
        return size;
    }

    public String getWeight() {
        return weight;
    }

    public String getDescription() {
        return description;
    }

    public int getExpCurve() {
        return expCurve;
    }

    public Evolution getEvolution() {
        return evolution;
    }

    public int getExp() {
        return exp;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public JsonObject save() {
        JsonObject pokemon = new JsonObject().add("fightingPkmn", super.save())
                               .add("exp", exp);
        if (item != null)
            pokemon.add("item", item.getName());
        return pokemon;
    }

    public void giveEVs(int[] EVs) {
        for (int i = 0; i < 5; i++) {
            this.EVs[i] += EVs[i];
        }
    }
    
    
}
