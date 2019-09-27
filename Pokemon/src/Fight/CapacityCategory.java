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

/**
 *
 * @author brousserie
 */
public enum CapacityCategory
{
    //The capacity category of attacks is not implemented in fight yet
    // <editor-fold defaultstate="collapsed" desc="Enum values">
    /**
     * "Physique" attacks should use atk and def stats to compute damages
     */
    Physique,
    /**
     * "Special" attacks should use spe stat to compute damages
     */
    Special,
    /**
     * "Autre" attacks should not apply damage but apply varied effects
     */
    Autre;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Method">
    /**
     *
     * @return
     */
    public boolean inflictsDamages()
    {
        switch (this) {
            case Physique:
            case Special:
                return true;
            case Autre:
                return false;
        }
        return true;
    }
    // </editor-fold>
}
