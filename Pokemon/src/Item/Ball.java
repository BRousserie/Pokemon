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

import Fight.FightingPkmn;

/**
 *
 * @author Baptiste
 */
public class Ball extends Item
{

    
    private int randomRate;

    // <editor-fold defaultstate="collapsed" desc="Contructor">
    /**
     * Constructor of the class Ball
     * @param infos
     */
    public Ball(String[] infos)
    {
        super(infos[0], infos[1], infos[2]);
        this.randomRate = Integer.parseInt(infos[3]);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methhods">
    /**
     * Methods to use a ball 
     * 
     * @param target
     * @return the capture rate
     */
    public int use(FightingPkmn target)
    {
        int num = (int) (Math.random() * randomRate
                         - target.getPkmnStatus().getCaptureBonus());
        if (target.getCaptureRate() < num)
            return 0;
        if (num <= 0)
            return 4;

        int hpFactor = target.getMaxHP() * 255
                       / ((this.name.equals("Super Ball") ? 8 : 12)
                          * ((target.getHp() / 4 > 0) ? target.getHp() / 4 : 1));

        if (Math.random() * 255 <= hpFactor)
            return 4;

        int wobble = (hpFactor < 255) ? hpFactor : 255 * target.getCaptureRate() * 100
                                                   / (randomRate * 255);
        if (target.getPkmnStatus().getCaptureBonus() != 0)
            wobble += (target.getPkmnStatus().getCaptureBonus() == 25 ? 10 : 5);

        return wobble < 10 ? 0
               : wobble < 30 ? 1
                 : wobble < 70 ? 2
                   : 3;
    }
    // </editor-fold>
}
