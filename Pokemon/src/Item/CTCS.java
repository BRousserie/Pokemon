/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <brousserie@iut.u-bordeaux.fr> and <tpedrero@iut.u-bordeaux.fr>
 * wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy us a beer in return Baptiste and Tony
 * ----------------------------------------------------------------------------
 */
package Item;

/**
 *
 * @author Baptiste
 */
public class CTCS extends Item
{

    /**
     * Constructor of the Class CTCS
     * @param infos
     */
    public CTCS(String[] infos)
    {
        super(infos[0] +" "+ infos[1], "Capacité " + infos[1], "500");
    }
}
