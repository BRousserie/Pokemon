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
import FileIO.ReaderException;

/**
 * Class allowing us to manage attacks during fights
 */
public class Attack {

    private final String name;
    private final String type;
    private final CapacityCategory category;
    private final int power;
    private final int accuracy;
    private final int PPMAX;
    private int pp;
    private final String[] effect;

    public Attack(String name, String type, String category, int power, int accuracy, int PPMAX, String effect) {
        this.name = name;
        this.type = type;
        this.category = CapacityCategory.valueOf(category);
        this.power = power;
        this.accuracy = accuracy;
        this.PPMAX = PPMAX;
        this.pp = PPMAX;
        this.effect = effect.split("/");
    }

    /**
     * @return the attack's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the attack's type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the attack's category
     */
    public CapacityCategory getCategory() {
        return category;
    }

    /**
     * @return the attack's power
     */
    public int getPower() {
        return power;
    }

    /**
     * @return the attack's accuracy
     */
    public int getAccuracy() {
        return accuracy;
    }

    /**
     * @return the attack's PPMAX
     */
    public int getPPMAX() {
        return PPMAX;
    }

    /**
     * @return the attack's pp
     */
    public int getPP() {
        return pp;
    }

    /**
     * Decrements PP, as when you use an attack
     */
    public void decrementPP() {
        pp--;
    }

    /**
     * Restores PP
     *
     * @param restoredPoints amount of points restored
     */
    public void restorePP(int restoredPoints) {
        pp += restoredPoints;
        pp = pp > PPMAX ? PPMAX : pp;
    }

    /**
     * @return the attack's effect
     */
    public String[] getEffect() {
        return effect;
    }

    public void useEffect(FightingPkmn attacker, FightingPkmn ennemy) {
        if (effect[0].equals("stat")) {
            alterStats((effect[2].equals("attacker") ? attacker
                      : effect[2].equals("ennemy") ? ennemy : null),
                    effect[1], Integer.parseInt(effect[3]));

        }
    }

    private void alterStats(FightingPkmn target, String stat, int coef) {
        int statIndex = stat.equals("atk") ? 1
                : stat.equals("def") ? 2
                : stat.equals("spe") ? 3
                : stat.equals("speed") ? 4
                : 5;
        if (statIndex < 5)
            target.statsVariations[statIndex] += coef;
    }

    /**
     * Loads one attack from Txt file
     *
     * @param name
     * @param firstTry
     * @return
     * @throws ReaderException
     */
    public static Attack loadAtkFromFile(String name) {
        try {
            String[] atk = DataReader.readFileArray("attacks", name);
            return new Attack(name, atk[1], atk[2],
                    Integer.parseInt(atk[3]), Integer.parseInt(atk[4]),
                    Integer.parseInt(atk[5]), atk[6]);
        } catch (Exception e) {
            return null;
        }
    }
}
