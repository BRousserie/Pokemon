/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <brousserie@iut.u-bordeaux.fr> and <tpedrero@iut.u-bordeaux.fr>
 * wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy us a beer in return Baptiste and Tony
 * ----------------------------------------------------------------------------
 */

package Character;

import Item.Item;
import java.util.ArrayList;


public class RareItems extends Item {
    public RareItems(ArrayList<String> infos) {
        super(infos.get(0), infos.get(1), infos.get(2));
    }
}
