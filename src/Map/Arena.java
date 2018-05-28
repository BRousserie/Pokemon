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

import Character.Dressor;
import GameEngine.DressorFight;
import GameEngine.Fight;
import java.util.HashMap;

/**
 *
 * @author brousserie
 */
public class Arena {
    private final String name;
    private final HashMap<Dressor, Boolean> dressors;

    public Arena(HashMap<Dressor, Boolean> dressors, String name) {
        this.name = name;
        this.dressors = dressors;
    }
    
    private Fight challengeNextDressor() {
        for (Dressor dressor : dressors.keySet()) {
            if (dressors.get(dressor)) {
                DressorFight fight = new DressorFight(dressor);
            }
        }
        return null;
    }
}
