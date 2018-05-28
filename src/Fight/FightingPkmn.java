/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <brousserie@iut.u-bordeaux.fr> and <tpedrero@iut.u-bordeaux.fr>
 * wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy us a beer in return Baptiste and Tony
 * ----------------------------------------------------------------------------
 */
package Fight;

import FileIO.ReaderException;
import FileIO.Savable;
import GameEngine.Game;
import Map.WildPkmn;
import Pokemons.PkmnStats;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Class allowing to create ready to fight pokemons
 *
 * @param pokemon : name of the pokemon in the pokedex - Pokemons can't have
 * surname in this game
 * @param level : urrent level
 * @param exp : current experience points, at his current level
 * @param levelUpXP : experience points needed to level up, at his current level
 * @param maxHP : maximum amount of HealthPoints, at his current level
 * @param atk : attack points
 * @param def : defense points
 * @param spe : special points
 * @param speed : speed points
 * @param hp : current amount of HealthPoints
 * @param attacks : attacks the pokemon is able to use
 * @param pkmnStatus : tells if the pokemon is paralysed, poisoned, burned...
 */
public class FightingPkmn implements Savable<JsonObject> {

    protected int ID;
    protected PkmnStats pkmnSpecies;
    protected int level;
    protected int[] IVs = new int[5];
    protected int[] EVs = {0, 0, 0, 0, 0};
    protected int[] stats = new int[5];
    protected int[] statsVariations = {0, 0, 0, 0, 0};
    protected Attack[] attacks = new Attack[4];
    protected Status pkmnStatus = null;

    public FightingPkmn(String name, int level, int ID) {

        try {
            pkmnSpecies = Game.getGame().getDatas().getLoadedPkmn(name);
        } catch (ReaderException ex) {
        }

        this.level = level;
        this.ID = ID;
        determinateIVs();
        determinateStats();
        try {
            determinateAttacks();
        } catch (ReaderException ex) {
        }
        this.pkmnStatus = Status.OK;
    }

    public FightingPkmn(String name, int level, int ID, int[] IV, int[] EV, ArrayList<Attack> attacks) {
        try {
            pkmnSpecies = Game.getGame().getDatas().getLoadedPkmn(name);
        } catch (ReaderException ex) {
            Logger.getLogger(FightingPkmn.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.level = level;
        this.ID = ID;
        this.IVs = IV;
        this.EVs = EV;
        for (Attack atk : attacks) {
            this.attacks[attacks.indexOf(atk)] = atk;
        }
        this.pkmnStatus = Status.OK;
    }

    public FightingPkmn(WildPkmn pkmn) {
        try {
            pkmnSpecies = Game.getGame().getDatas().getLoadedPkmn(pkmn.getName());
        } catch (ReaderException ex) {
        }
        if (pkmn.getLevel().length == 1) {
            this.level = pkmn.getLevel()[0];
        } else {
            this.level = pkmn.getLevel()[0] + 
                    (int)(Math.random()*(pkmn.getLevel()[0]-pkmn.getLevel()[1]));
        }
        this.ID = 0;
        determinateIVs();
        determinateStats();
        try {
            determinateAttacks();
        } catch (ReaderException ex) {
        }
        this.pkmnStatus = Status.OK;

    }

    public FightingPkmn(FightingPkmn capturedWildPkmn) {
        ID = Game.getGame().getPlayer().getMyPokemons().size();
        pkmnSpecies = capturedWildPkmn.pkmnSpecies;
        level = capturedWildPkmn.level;
        IVs = capturedWildPkmn.IVs;
        EVs = capturedWildPkmn.EVs;
        stats = capturedWildPkmn.stats;
        statsVariations = capturedWildPkmn.statsVariations;
        attacks = capturedWildPkmn.attacks;
        pkmnStatus = capturedWildPkmn.pkmnStatus;
    }

    public FightingPkmn(JsonObject pokemon) throws ReaderException {
        ID = pokemon.getInt("ID", 0);
        
        try {
            pkmnSpecies = Game.getGame().getDatas()
                    .getLoadedPkmn(pokemon.getString("name", null));
        } catch (ReaderException ex) {
        }
        
        level = pokemon.getInt("level", 0);
        IVs[0] = pokemon.getInt("IVhp", 0);
        IVs[1] = pokemon.getInt("IVatk", 0);
        IVs[2] = pokemon.getInt("IVdef", 0);
        IVs[3] = pokemon.getInt("IVspe", 0);
        IVs[4] = pokemon.getInt("IVspeed", 0);
        EVs[0] = pokemon.getInt("EVhp", 0);
        EVs[1] = pokemon.getInt("EVatk", 0);
        EVs[2] = pokemon.getInt("EVdef", 0);
        EVs[3] = pokemon.getInt("EVspe", 0);
        EVs[4] = pokemon.getInt("EVspeed", 0);
        statsVariations[0] = pokemon.getInt("hpLost", 0);
        for (int i = 1; i < 5; i++) statsVariations[i] = 0;
        determinateStats();
        pkmnStatus = Status.valueOf(pokemon.getString("pkmnStatus", null));
        int iterator = 0;
        for (JsonValue attack : pokemon.get("attacks").asArray()) {
            this.attacks[iterator] = Game.getGame().getDatas().getLoadedAtk(attack.asString());
            iterator++;
        }
    }

    protected void doEvolve(String evolution) throws ReaderException {
        try {
            pkmnSpecies = Game.getGame().getDatas().getLoadedPkmn(evolution);
            if (pkmnSpecies == null) {
                pkmnSpecies = PkmnStats.loadPkmnStatsFromFile(evolution);
            }
            determinateStats();
            determinateAttacks();
        } catch(Exception e) {};
    }

    private void determinateIVs() {
        for (int i = 0; i < 5; i++) {
            this.IVs[i] = (int) (Math.random() * 16);
        }
    }

    public void determinateStats() {
        this.stats[0] = (int) ((IVs[0] + pkmnSpecies.getBasicHp() + Math.sqrt(EVs[0] / 8) + 50) * level / 50 + 10);
        for (int i = 1; i < 5; i++) {
            stats[i] = (int) ((IVs[i] + pkmnSpecies.getBasicStats()[i] + Math.sqrt(EVs[i] / 8)) * level / 50 + 5);
        }
    }

    private void determinateAttacks() throws ReaderException {
        int atkSlot = 0;
        String[] atksToLearn = {"", "", "", ""};
        for (String atk : pkmnSpecies.getLearnableAtks().keySet()) {
            atkSlot = atkSlot < 3 ? atkSlot : 0;
            if (pkmnSpecies.getLearnableAtks().get(atk) <= level) {
                atksToLearn[atkSlot++] = atk;
            }
        }
        for (int i = 0; i < 4; i++) {
            if (atksToLearn[i] != "") {
                this.attacks[i] = Game.getGame().getDatas().getLoadedAtk(atksToLearn[i]);
            }
        }
    }

    //      GETTERS FOR THE POKEMON INFORMATIONS
    /**
     * Get the pokemon's name
     */
    public String getName() {
        try {
            return this.pkmnSpecies.getName();
        } catch (NullPointerException e) {
            return "null";
        }
    }

    public int getID() {
        return ID;
    }

    /**
     * Get the corresponding pokemon in the Pokedex
     */
    public String getType() {
        return this.pkmnSpecies.getType();
    }

    //      GETTERS FOR THE POKEMON'S STATISTICS
    /**
     * Get the pokemon's maximum amount of health points
     */
    public int getMaxHP() {
        return stats[0];
    }

    /**
     * Get the pokemon's amount of health point
     */
    public int getHp() {
        return stats[0] + statsVariations[0];
    }

    /**
     * Get the pokemon's amount of attack points
     */
    public float getAtk() {
        if(statsVariations[1] == 0)
            return stats[1];
        if(statsVariations[1] > 0) 
            return (float)stats[1] * (float)((float)(2+statsVariations[1])/2);
        return     (float)stats[1] * (float)(2/(float)(-statsVariations[1]+2));
    }

    /**
     * Get the pokemon's amount of defense points
     */
    public float getDef() {
        if(statsVariations[2] == 0)
            return stats[2];
        if(statsVariations[2] > 0) 
            return (float)stats[2] * (float)((float)(2+statsVariations[2])/2);
        return     (float)stats[2] * (float)(2/(float)(-statsVariations[2]+2));
    }

    /**
     * Get the pokemon's amount of special points
     */
    public float getSpe() {
        if(statsVariations[3] == 0)
            return stats[3];
        if(statsVariations[3] > 0) 
            return (float)stats[3] * (float)((float)(2+statsVariations[3])/2);
        return     (float)stats[3] * (float)(2/(float)(-statsVariations[3]+2));
    }

    /**
     * Get the pokemon's amount of speed points
     */
    public float getSpeed() {
        if(statsVariations[4] == 0)
            return stats[4];
        if(statsVariations[4] > 0) 
            return (float)stats[4] * (float)((float)(2+statsVariations[4])/2);
        return     (float)stats[4] * (float)(2/(float)(-statsVariations[4]+2));
    }

    public int getCaptureRate() {
        return pkmnSpecies.getCaptureRate();
    }

    /**
     * Get the pokemon's level
     */
    public int getLevel() {
        return level;
    }

    //      GETTERS AND SETTERS FOR FIGHTING
    /**
     * Get the list of attacks the pokemon can do
     */
    public Attack[] getAttacks() {
        return attacks;
    }

    public int[] getIVs() {
        return IVs;
    }

    public int[] getEVs() {
        return EVs;
    }

    public int[] getStats() {
        return stats;
    }

    public int[] getStatsVariations() {
        return statsVariations;
    }

    public void setStatsVariations(int[] statsVariations) {
        this.statsVariations = statsVariations;
    }

    public PkmnStats getPkmnSpecies() {
        return pkmnSpecies;
    }

    /**
     * Get the pokemon's status
     */
    public Status getPkmnStatus() {
        return pkmnStatus;
    }

    /**
     * Set the pokemon's status
     */
    public void setPkmnStatus(Status newVar) {
        pkmnStatus = newVar;
    }

    /**
     * Set the Health Points depending on an amount of damage
     */
    public void loseHp(int damageAmount) {
        if (!pkmnStatus.equals(Status.KO)) {
            statsVariations[0] -= damageAmount;
        }
        if (stats[0] + statsVariations[0] <= 0) {
            pkmnStatus = Status.KO;
            statsVariations[0] = -stats[0];
        }
        if (statsVariations[0] > 0) statsVariations[0] = 0;
    }

    /**
     * Restores EnnemyPkmn's health point
     */
    public void restoreHP() {
        if (!pkmnStatus.equals("K.O.")) {
            statsVariations[0] = 0;
        }
    }

    public void incrementLevel() {
        level++;
    }

    @Override
    public JsonObject save() {
        JsonArray attacks = new JsonArray();
        Stream.of(this.attacks).filter(a -> a != null)
                               .forEach(a -> attacks.add(a.getName()));
        
        return new JsonObject().add("ID", ID)
                               .add("name", getName())
                               .add("level", level)
                               .add("IVhp", IVs[0])
                               .add("IVatk", IVs[1])
                               .add("IVdef", IVs[2])
                               .add("IVspe", IVs[3])
                               .add("IVspeed", IVs[4])
                               .add("EVhp", EVs[0])
                               .add("EVatk", EVs[1])
                               .add("EVdef", EVs[2])
                               .add("EVspe", EVs[3])
                               .add("EVspeed", EVs[4])
                               .add("hpLost", EVs[0])
                               .add("pkmnStatus", pkmnStatus.name())
                               .add("attacks", attacks);
    }
}
