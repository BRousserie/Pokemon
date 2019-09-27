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

import View.FightViewController;

/**
 *
 * @author Baptiste
 */
public enum Status
{
    //The Statuses are not implemented in fight yet
    // <editor-fold defaultstate="collapsed" desc="Enum values">
    /**
     * Damages the Pokemon each turn
     */
    BRULURE,
    /**
     * Damages the Pokemon each turn
     */
    POISON,
    /**
     * May stop the Pokemon from attacking
     */
    PARALYSIE,
    /**
     * Stops the Pokemon from attacking
     */
    SOMMEIL,
    /**
     * Stops the Pokemon from attacking
     */
    GEL,
    /**
     * The Pokemon cannot fight until healed
     */
    KO,
    /**
     * The Pokemon is fine
     */
    OK;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * Tells if the Pokemon can move or not, considering his status
     * 
     * @param pkmnName Name of the Pokemon, in order to display it
     * @param view Game view in which the message should be displayed
     * @return true if the Pokemon can attack, or else false
     */
    public boolean pkmnCanAttack(String pkmnName, FightViewController view)
    {   //view.showMessage(getStatusMessage(pkmnName));
        switch (this) {
            case SOMMEIL:
                return false;
            case GEL:
              return false;
            case PARALYSIE:
                //should implement a test using random() to determine wheter he
                //attack or not
              return false;
            default:
                return true;
        }
    }

    /**
     * Gets the message to show corresponding the Status
     * 
     * @param pkmnName name of the Pokemon
     * @return the message to show corresponding the Status
     */
    public String getStatusMessage(String pkmnName)
    {
        String output = pkmnName + " souffre de " + this + " ! ";

        switch (this) {
            case SOMMEIL:
                return output + pkmnName + " dort, il ne peut pas attaquer.";
            case GEL:
                return output + pkmnName + " est gelé, il ne peut pas attaquer.";
            case PARALYSIE:
                return output + pkmnName + " est paralysé, il ne peut pas attaquer.";
            default:
                return output;
        }

    }

    /**
     * Gets the bonus of chance that the status provides for capturing a Pokemon
     * 
     * @return the benus chance for capturing a Pokemon with this status
     */
    public int getCaptureBonus()
    {
        switch (this) {
            case BRULURE:
            case POISON:
            case PARALYSIE:
                return 12;
            case SOMMEIL:
            case GEL:
                return 25;
            default:
                return 0;
        }
    }
    // </editor-fold>
}
