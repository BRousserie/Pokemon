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

import FileIO.DataReader;
import FileIO.ReaderException;

/**
 *
 * @author Baptiste
 */
public enum PkmnType {
    // The different types
    NORMAL,
    FEU,
    EAU,
    ELECTR,
    PLANTE,
    GLACE,
    COMBAT,
    POISON,
    SOL,
    VOL,
    PSY,
    INSECT,
    ROCHE,
    SPECTR,
    DRAGON;

    /**
     * Calculates the damages fuction of the types
     *
     * @param atkType
     * @param defType
     * @return
     */
    public static float getDamageRate(PkmnType atkType, PkmnType defType) {
        try {
            return Float.parseFloat(
                    DataReader.readFileArray(
                            "types", atkType.name())[indexOf(defType)]);
        } catch (ReaderException ex) {
            return 1;
        }
    }

    /**
     * The index of the type
     *
     * @param t
     * @return
     */
    public static int indexOf(PkmnType t) {
        for (int i = 0; i < PkmnType.values().length; i++) {
            if (PkmnType.values()[i].equals(t)) {
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * Gets the pokmon type
     *
     * @param pkmnType
     * @return
     */
    public static PkmnType getType(String pkmnType) {
        for (int i = 0; i < PkmnType.values().length; i++) {
            if (PkmnType.values()[i].toString().equals(pkmnType.toUpperCase())) {
                return PkmnType.values()[i];
            }
        }
        return null;
    }
}
