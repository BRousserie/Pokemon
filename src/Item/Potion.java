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
import Fight.Status;
import GameEngine.Game;
import View.FightViewController;

public class Potion extends Item {

    public Potion(String[] infos) {
        super(infos[0], infos[1], infos[2]);
    }

    public void use(FightingPkmn pkmn, FightViewController view) {
        if (description.contains("ranime") 
                && pkmn.getPkmnStatus().equals(Status.KO)) {
            pkmn.setPkmnStatus(Status.OK);
        }
        if (description.contains("PV")) {
            int hpBefore = pkmn.getHp();
            if (description.contains("tous")) {
                pkmn.restoreHP();
            } else if (description.contains("moitié")) {
                pkmn.loseHp(-pkmn.getMaxHP() / 2);
            } else {
                pkmn.loseHp(-Integer.parseInt(description.substring(
                        description.indexOf("estaure") + 8,
                        description.indexOf("PV") - 1)));
                
                if(Game.getGame().getFight().getMyPkmn().getID() !=pkmn.getID()) {
                    view.showMessage(
                        pkmn.getName()+" gagne "+(pkmn.getHp()-hpBefore)+" PV!",
                        (e -> {view.secondAttack(view.randomAttack(), false);}), 2);
                } else {
                    view.showUpdateBarMessage(
                        pkmn.getName()+" gagne "+(pkmn.getHp()-hpBefore)+" PV!",
                        e -> view.secondAttack(view.randomAttack(), false),pkmn,
                        view.myPkmnHP, view.calculateDuration(
                                hpBefore, pkmn.getHp(), pkmn.getMaxHP()));
                }
            }
        }
    }
}
