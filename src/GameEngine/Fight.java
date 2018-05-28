/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <brousserie@iut.u-bordeaux.fr> and <tpedrero@iut.u-bordeaux.fr>
 * wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy us a beer in return Baptiste and Tony
 * ----------------------------------------------------------------------------
 */
package GameEngine;

import Character.Player;
import Fight.Attack;
import Fight.FightingPkmn;
import Pokemons.CapturedPkmn;
import View.FightViewController;
import java.util.ArrayList;
import java.util.Optional;
import javafx.event.EventHandler;

/**
 * Class allowing us to manage fights between FightingCharacters
 *
 */
public class Fight {

    protected final Player me;
    protected CapturedPkmn myPkmn;
    protected FightingPkmn ennemy;
    protected boolean hasWinner;
    protected boolean playerWon;
    private int fleetries = 0;

    /**
     * Constructor allowing us to initialise fight
     */
    public Fight(FightingPkmn ennemy) {
        me = Game.getGame().getPlayer();
        this.ennemy = ennemy;
        myPkmn = me.getMyPokemons().get(me.getPkmns().get(0).getID());
        this.hasWinner = false;
    }

    public Player getMe() {
        return me;
    }

    public CapturedPkmn getMyPkmn() {
        return myPkmn;
    }

    public FightingPkmn getEnnemy() {
        return ennemy;
    }

    public boolean hasWinner() {
        return hasWinner;
    }

    public boolean isPlayerWon() {
        return playerWon;
    }

    public void setMyPkmn(CapturedPkmn myPkmn) {
        this.myPkmn = myPkmn;
    }

    public void setEnnemy(Optional<FightingPkmn> ennemy) {
        if (ennemy.isPresent()) {
            this.ennemy = ennemy.get();
        }
    }

    public ArrayList<FightingPkmn> getEnnemyPkmns() {
        return new ArrayList<>();
    }

    /**
     * Set the active pokemon according to the character's choice
     */
    public void setActivePkmn(int indexOfNextPkmn) {
        myPkmn = me.getMyPokemons().get(me.getPkmns().get(indexOfNextPkmn).getID());
    }

    public void useAttack(Attack atk, boolean myAtk, FightViewController view,
                EventHandler nextStep) {
        if (myAtk) fleetries = 0;
        atk.decrementPP();
        FightingPkmn attacker = (myAtk) ? myPkmn : ennemy;
        FightingPkmn target = (myAtk) ? ennemy : myPkmn;
        int hpBefore = target.getHp();
        if (atk.getCategory().inflictsDamages()) {
            target.loseHp(calculateDamages(atk, attacker, target));
        }
        atk.useEffect(attacker, target);
        view.showUpdateBarMessage(attacker.getName()+" attaque "+atk.getName()+" !",
                nextStep, target, myAtk ? view.enmyPkmnHP : view.myPkmnHP,
                view.calculateDuration(hpBefore, target.getHp(), target.getMaxHP()));
    }

    /**
     * Algorithm for calculating the remaining HP of the pokemon aimed by an
     * attack
     */
    private int calculateDamages(Attack atk, FightingPkmn attacker, FightingPkmn target) {
        try {
            return (int) (2 +
                    ( attacker.getLevel() * 0.4 + 2) 
                    * attacker.getAtk()
                    * atk.getPower()
                    / (target.getDef() * 50));
        } catch (Exception e) {
            System.out.println(atk.getName() + " n'a pas fonctionné. Défense de "+target.getName()+" : "+target.getDef());
            return 0;
        }
    }

    /**
     * Checks if both players have pokemons available to fight. If not, prints
     * to the screen who won and ends the fighting loop
     */
    public void checkIfHasAWinner(boolean ennemyDown) {
        myPkmn.giveEVs(ennemy.getEVs());
        if (ennemyDown) {
            giveRewards();
            giveExp(calculateEarnedXP());
            playerWon = true;
            hasWinner = true;
        } else {
            if (me.getAlivePkmns() == 0) {
                playerWon = false;
                hasWinner = true;
            }
        }
    }

    /**
     * If you win against a dressor, you earn his inventory. 
     */
    protected void giveRewards() {}

    /**
     * Gives money to the pokemon that fought. In the final version of the
     * program, the list of pokemon who fought will be stored for the experience
     * points to be shared between them.
     */
    protected void giveExp(int earnedXP) {
        myPkmn.earnExp(earnedXP);
    }

    /**
     * In the final version of the program, an algorithm will determine this
     * value
     */
    protected int calculateEarnedXP() {
        return 80;
    }

    public boolean fleeSucceeds() {
        return Math.random() * 255 < 
                (myPkmn.getSpeed()*32 / (ennemy.getSpeed()/4))
                + 30*++fleetries;
    }
}
