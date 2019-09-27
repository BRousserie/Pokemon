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

import FileIO.DataReader;
import Pokemons.PkmnType;

/**
 * Class allowing us to manage attacks during fights
 */
public class Attack
{

    // <editor-fold defaultstate="collapsed" desc="Static method">
    /**
     * Loads the attack corresponding to the given name from data base
     *
     * @param name name of the Attack to load
     * @return The loaded attack if it's been found, or else null
     */
    public static Attack loadAtkFromFile(String name)
    {
        try {
            String[] atk = DataReader.readFileArray("attacks", name);
            return new Attack(name, atk[1], atk[2],
                              Integer.parseInt(atk[3]), Integer.parseInt(atk[4]),
                              Integer.parseInt(atk[5]), atk[6]);
        } catch (Exception e) {
            return null;
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Attributes">
    private final String name;
    private final PkmnType type;
    private final CapacityCategory category;
    private final int power;
    private final int accuracy;
    private final int PPMAX;
    private int pp;
    private final String[] effect;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Constructor for attack loaded from file, with all its attributes
     *
     * @param name name of the attack
     * @param type type of the attack, as a String. The enum value will be
     * assigned into the constructor
     * @param category category of the attack, as a String. The enum value will
     * be assigned into the constructor.
     * @param power power of the attack, directly impacting the damages
     * @param accuracy accuracy, not implemented in the fight yet
     * @param PPMAX maximum amount of PP (usages of the attack - you have to
     * restore some to use the attack again, if it goes to 0)
     * @param effect effect of the attack, not fully implemented yet. only
     * effects that modifies basic statistics are functionnal
     */
    public Attack(String name, String type, String category,
                  int power, int accuracy, int PPMAX, String effect)
    {
        this.name = name;
        this.type = PkmnType.valueOf(type);
        this.category = CapacityCategory.valueOf(category);
        this.power = power;
        this.accuracy = accuracy;
        this.PPMAX = PPMAX;
        this.pp = PPMAX;
        this.effect = effect.split("/");
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    /**
     * @return the attack's name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the attack's type
     */
    public PkmnType getType()
    {
        return type;
    }

    /**
     * @return the attack's category
     */
    public CapacityCategory getCategory()
    {
        return category;
    }

    /**
     * @return the attack's power
     */
    public int getPower()
    {
        return power;
    }

    /**
     * @return the attack's accuracy
     */
    public int getAccuracy()
    {
        return accuracy;
    }

    /**
     * @return the attack's PPMAX
     */
    public int getPPMAX()
    {
        return PPMAX;
    }

    /**
     * @return the attack's current pp
     */
    public int getPP()
    {
        return pp;
    }

    /**
     * @return the attack's effect
     */
    public String[] getEffect()
    {
        return effect;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * Decrements PP, as when you use an attack
     */
    public void decrementPP()
    {
        pp--;
    }

    /**
     * Restores PP
     *
     * @param restoredPoints amount of points restored
     */
    public void restorePP(int restoredPoints)
    {
        pp += restoredPoints;
        pp = pp > PPMAX ? PPMAX : pp;
    }

    /**
     * Applies the effect of the attack
     *
     * @param attacker
     * @param ennemy
     */
    public void useEffect(FightingPkmn attacker, FightingPkmn ennemy)
    {
        if (effect[0].equals("stat"))
            alterStats((effect[2].equals("attacker")
                        ? attacker : effect[2].equals("ennemy")
                        ? ennemy
                        : null),
                       effect[1], Integer.parseInt(effect[3]));
    }

    private void alterStats(FightingPkmn target, String stat, int coef)
    {
        int statIndex = stat.equals("atk") ? 1
                        : stat.equals("def") ? 2
                          : stat.equals("spe") ? 3
                            : stat.equals("speed") ? 4
                              : 5;
        if (statIndex < 5)
            target.statsVariations[statIndex] += coef;
    }
    // </editor-fold>
}
