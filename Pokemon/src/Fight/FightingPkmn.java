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
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class allowing to create ready to fight Pokemon
 */
public class FightingPkmn implements Savable<JsonObject>
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    /**
     * Number of the Pokemon
     */
    protected int ID;

    /**
     * Species of the Pokemon, which provides his basic statistics
     */
    protected PkmnStats pkmnSpecies;

    /**
     * Level of the Pokemon
     */
    protected int level;

    /**
     * Random numbers that are going to give unique statistics to each Pokemon
     */
    protected int[] IVs = new int[5];

    /**
     * Points earned by defeating others Pokmon. They improve Pokemon's stats
     */
    protected int[] EVs = {0, 0, 0, 0, 0};

    /**
     * The global stats of the Pokemon, in order hp/atk/def/spe/speed
     */
    protected int[] stats = new int[5];

    /**
     * Variations applied to Pokemon's stats. They are temporary.
     */
    protected int[] statsVariations = {0, 0, 0, 0, 0};

    /**
     * List of attacks the Pokemon can use
     */
    protected Attack[] attacks = new Attack[4];

    /**
     * Status of the Pokmon -not implemented yet
     */
    protected Status pkmnStatus = null;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Constructor for Trainer's fighting Pokemon
     * 
     * @param name Pokemon's name
     * @param level Pokemon's level
     * @param ID Pokemon's ID
     */
    public FightingPkmn(String name, int level, int ID)
    {

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

    /**
     * Constructor for wild fighting Pokemon
     * 
     * @param pkmn Wild Pokemon's stats
     */
    public FightingPkmn(WildPkmn pkmn)
    {
        try {
            pkmnSpecies = Game.getGame().getDatas().getLoadedPkmn(pkmn.getName());
        } catch (ReaderException ex) {
        }
        if (pkmn.getLevel().length == 1)
            this.level = pkmn.getLevel()[0];
        else
            this.level = pkmn.getLevel()[0]
                         + (int) (Math.random() * (pkmn.getLevel()[0] - pkmn.getLevel()[1]));
        this.ID = 0;
        determinateIVs();
        determinateStats();
        try {
            determinateAttacks();
        } catch (ReaderException ex) {
            Logger.getLogger(FightingPkmn.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.pkmnStatus = Status.OK;

    }

    /**
     * Constructor for FightingPokemon that you've captured
     * 
     * @param capturedWildPkmn Pokemon you've captured
     */
    public FightingPkmn(FightingPkmn capturedWildPkmn)
    {
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

    /**
     * Constructor for previously saved Pokemon
     * 
     * @param pokemon the Pokemon saved
     * @throws ReaderException 
     */
    public FightingPkmn(JsonObject pokemon)
    {
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
        for (int i = 1; i < 5; i++)
            statsVariations[i] = 0;
        determinateStats();
        pkmnStatus = Status.valueOf(pokemon.getString("pkmnStatus", null));
        int iterator = 0;
        for (JsonValue attack : pokemon.get("attacks").asArray()) {
            try {
                this.attacks[iterator] = Game.getGame().getDatas().getLoadedAtk(attack.asString());
            } catch (ReaderException ex) {
                Logger.getLogger(FightingPkmn.class.getName()).log(Level.SEVERE, null, ex);
            }
            iterator++;
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    /**
     * @return the pokemon's name
     */
    public String getName()
    {
        try {
            return this.pkmnSpecies.getName();
        } catch (NullPointerException e) {
            return "null";
        }
    }

    /**
     *
     * @return the Pokemon's ID
     */
    public int getID()
    {
        return ID;
    }

    /**
     * @return the Pokemon's type
     */
    public String getType()
    {
        return this.pkmnSpecies.getType();
    }

    /**
     * @return a number that should indicate an average of the Pokemon's strength
     */
    public int getOdds()
    {
        int sum = 5 * level;
        for (int i = 0; i < stats.length; i++)
            sum += stats[i];
        return sum;
    }

    /**
     * @return the pokemon's maximum amount of health points
     */
    public int getMaxHP()
    {
        return stats[0];
    }

    /**
     * @return the pokemon's amount of health point
     */
    public int getHp()
    {
        return stats[0] + statsVariations[0];
    }

    /**
     * @return true if the Pokemon has health points left
     */
    public boolean isAlive()
    {
        return getHp() != 0;
    }

    /**
     * @return the pokemon's amount of attack points
     */
    public float getAtk()
    {
        if (statsVariations[1] == 0)
            return stats[1];
        if (statsVariations[1] > 0)
            return stats[1] * ((float) (2 + statsVariations[1]) / 2);
        return stats[1] * (2 / (float) (-statsVariations[1] + 2));
    }

    /**
     * @return the pokemon's amount of defense points
     */
    public float getDef()
    {
        if (statsVariations[2] == 0)
            return stats[2];
        if (statsVariations[2] > 0)
            return stats[2] * ((float) (2 + statsVariations[2]) / 2);
        return stats[2] * (2 / (float) (-statsVariations[2] + 2));
    }

    /**
     * @return the pokemon's amount of special points
     */
    public float getSpe()
    {
        if (statsVariations[3] == 0)
            return stats[3];
        if (statsVariations[3] > 0)
            return stats[3] * ((float) (2 + statsVariations[3]) / 2);
        return stats[3] * (2 / (float) (-statsVariations[3] + 2));
    }

    /**
     * @return the pokemon's amount of speed points
     */
    public float getSpeed()
    {
        if (statsVariations[4] == 0)
            return stats[4];
        if (statsVariations[4] > 0)
            return stats[4] * ((float) (2 + statsVariations[4]) / 2);
        return stats[4] * (2 / (float) (-statsVariations[4] + 2));
    }

    /**
     * @return the Pokemon's capture rate
     */
    public int getCaptureRate()
    {
        return pkmnSpecies.getCaptureRate();
    }

    /**
     * @return the pokemon's level
     */
    public int getLevel()
    {
        return level;
    }

    /**
     * @return the list of attacks the pokemon can do
     */
    public List<Attack> getAttacks()
    {
        return Arrays.asList(attacks)
                .stream().filter(e -> e != null).collect(Collectors.toList());
    }

    /**
     * @return the Pokemon's statistics
     */
    public int[] getStats()
    {
        return stats;
    }

    /**
     * @return the Pokemon's statistics variations
     */
    public int[] getStatsVariations()
    {
        return statsVariations;
    }

    /**
     * @return the species of the Pokemon
     */
    public PkmnStats getPkmnSpecies()
    {
        return pkmnSpecies;
    }

    /**
     * @return the pokemon's status
     */
    public Status getPkmnStatus()
    {
        return pkmnStatus;
    }

    /**
     * Set the pokemon's status
     *
     * @param newVar new Status to apply
     */
    public void setPkmnStatus(Status newVar)
    {
        pkmnStatus = newVar;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * Takes a Pokemon to its evolution
     * 
     * @param evolution name of the evolution
     * @throws ReaderException if evolution couldn't be found in files
     */
    protected void doEvolve(String evolution) throws ReaderException
    {
        pkmnSpecies = Game.getGame().getDatas().getLoadedPkmn(evolution);
        determinateStats();
        determinateAttacks();
    }

    private void determinateIVs()
    {
        for (int i = 0; i < 5; i++)
            this.IVs[i] = (new Random().nextInt(16));
    }

    /**
     * Computes the stats of the Pokemon
     */
    public final void determinateStats()
    {
        this.stats[0] = (int) ((IVs[0] + pkmnSpecies.getBasicHp() + Math.sqrt(EVs[0] / 8) + 50) * level / 50 + 10);
        for (int i = 1; i < 5; i++)
            stats[i] = (int) ((IVs[i] + pkmnSpecies.getBasicStats()[i] + Math.sqrt(EVs[i] / 8)) * level / 50 + 5);
    }

    protected final void determinateAttacks() throws ReaderException
    {
        int atkSlot = 0;
        String[] atksToLearn = {"", "", "", ""};
        for (String atk : pkmnSpecies.getLearnableAtks().keySet()) {
            atkSlot = atkSlot < 3 ? atkSlot : 0;
            if (pkmnSpecies.getLearnableAtks().get(atk) <= level)
                atksToLearn[atkSlot++] = atk;
        }
        for (int i = 0; i < 4; i++)
            if (!atksToLearn[i].isEmpty())
                this.attacks[i] = Game.getGame().getDatas().getLoadedAtk(atksToLearn[i]);
    }
    
    /**
     * Set the Health Points depending on an amount of damage
     *
     * @param damageAmount
     */
    public void loseHp(int damageAmount)
    {
        if (!pkmnStatus.equals(Status.KO))
            statsVariations[0] -= damageAmount;
        if (stats[0] + statsVariations[0] <= 0) {
            pkmnStatus = Status.KO;
            statsVariations[0] = -stats[0];
        }
        if (statsVariations[0] > 0)
            statsVariations[0] = 0;
    }

    /**
     * Restores EnnemyPkmn's health point
     */
    public void restoreHP()
    {
        if (!pkmnStatus.equals("K.O."))
            statsVariations[0] = 0;
    }

    /**
     * Increments the level of the Pokemon
     */
    public void incrementLevel()
    {
        level++;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Save to Json">
    /**
     * Converts the Fighting Pokemon into a Json Object
     * 
     * @return JsonObject containing all needed informations on the Pokemon
     */
    @Override
    public JsonObject save()
    {
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
                .add("hpLost", statsVariations[0])
                .add("pkmnStatus", pkmnStatus.name())
                .add("attacks", attacks);
    }
    // </editor-fold>
}
