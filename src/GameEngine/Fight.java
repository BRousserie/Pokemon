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
import java.util.List;
import java.util.Optional;
import javafx.event.EventHandler;

/**
 * Class allowing us to manage fights between FightingCharacters
 *
 */
public class Fight {

    // <editor-fold defaultstate="collapsed" desc="Attributes">
    // The player
    protected final Player me;

    // The list of captured pokemons
    protected CapturedPkmn myPkmn;

    //  The list of enemies
    protected FightingPkmn ennemy;

    protected boolean hasWinner;
    protected boolean playerWon;
    private int fleeTries = 0;

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Constructor allowing us to initialise fight
     *
     * @param ennemy
     */
    public Fight(FightingPkmn ennemy) {
        me = Game.getGame().getPlayer();
        this.ennemy = ennemy;
        myPkmn = me.getMyPokemons().get(me.getPkmns().get(0).getID());
        this.hasWinner = false;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters / Setters">
    /**
     * Gets the message with the encountered pokemon name
     *
     * @return teh message
     */
    public String getStartingMessage() {
        return "Un " + ennemy.getName() + " sauvage apparaît !";
    }

    /**
     * Gets the player
     *
     * @return me
     */
    public Player getMe() {
        return me;
    }

    /**
     * Gets the fighting pokemon
     *
     * @return myPkmn
     */
    public CapturedPkmn getMyPkmn() {
        return myPkmn;
    }

    /**
     * Gets the fighting ennemy
     *
     * @return enemy
     */
    public FightingPkmn getEnnemy() {
        return ennemy;
    }

    /**
     * Says if there is a winner
     *
     * @return true or false
     */
    public boolean hasWinner() {
        return hasWinner;
    }

    /**
     * Says if the player wons
     *
     * @return true or false
     */
    public boolean isPlayerWon() {
        return playerWon;
    }

    /**
     * Sets the fighting pokemon
     *
     * @param myPkmn
     */
    public void setMyPkmn(CapturedPkmn myPkmn) {
        this.myPkmn = myPkmn;
    }

    /**
     * Sets the fighting ennemy pokemon
     *
     * @param ennemy
     */
    public void setEnnemy(Optional<FightingPkmn> ennemy) {
        if (ennemy.isPresent()) {
            this.ennemy = ennemy.get();
        }
    }

    /**
     * Is defined in TrainerFight class
     *
     * @return the list of the ennemies pokemons
     */
    public List<FightingPkmn> getEnnemyPkmns() {
        return new ArrayList<>();
    }

    /**
     * Set the active pokemon according to the character's choice
     *
     * @param indexOfNextPkmn
     */
    public void setActivePkmn(int indexOfNextPkmn) {
        myPkmn = me.getMyPokemons().get(me.getPkmns().get(indexOfNextPkmn).getID());
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Other Mathods">
    /**
     * Execute the attacks according to the caractter's choice
     *
     * @param atk
     * @param myAtk
     * @param view
     * @param nextStep
     */
    public void useAttack(Attack atk, boolean myAtk, FightViewController view,
            EventHandler nextStep) {
        if (myAtk) {
            fleeTries = 0;
        }

        atk.decrementPP();
        FightingPkmn attacker = (myAtk) ? myPkmn : ennemy;
        FightingPkmn target = (myAtk) ? ennemy : myPkmn;
        int hpBefore = target.getHp();

        if (atk.getCategory().inflictsDamages()) {
            target.loseHp(calculateDamages(atk, attacker, target));
        }

        atk.useEffect(attacker, target);
        view.showUpdateBarMessage(attacker.getName() + " attaque " + atk.getName() + " !",
                nextStep, target, myAtk ? view.enmyPkmnHP : view.myPkmnHP,
                view.calculateDuration(hpBefore, target.getHp(), target.getMaxHP()));
    }

    /**
     * Algorithm for calculating the remaining HP of the pokemon aimed by an
     * attack
     */
    private int calculateDamages(Attack atk, FightingPkmn attacker, FightingPkmn target) {
        try {
            return (int) (2
                    + (attacker.getLevel() * 0.4 + 2)
                    * attacker.getAtk()
                    * atk.getPower()
                    // The damage rate is not implemented yet.
                    // Having difficulties with Pokemon that have 2 types
                    //* PkmnType.getDamageRate(atk.getType(), target.getType())
                    / (target.getDef() * 50));
        } catch (Exception e) {
            System.out.println(atk.getName() + " n'a pas fonctionné. Défense de " + target.getName() + " : " + target.getDef());
            return 0;
        }
    }

    /**
     * Checks if both players have pokemons available to fight. If not, prints
     * to the screen who won and ends the fighting loop
     *
     * @param ennemyDown
     */
    public void checkIfHasAWinner(boolean ennemyDown) {
        myPkmn.giveEVs(ennemy.getPkmnSpecies().getEVReward());
        if (ennemyDown) {
            giveRewards();
            giveExp(calculateEarnedXP(ennemy));
            playerWon = true;
            hasWinner = true;
        } else if (me.getAlivePkmns() == 0) {
            playerWon = false;
            hasWinner = true;
        }
    }

    /**
     * If you win against a dressor, you earn his inventory.
     */
    protected void giveRewards() {
    }

    /**
     * Gives money to the pokemon that fought. In the final version of the
     * program, the list of pokemon who fought will be stored for the experience
     * points to be shared between them.
     *
     * @param earnedXP
     */
    protected void giveExp(int earnedXP) {
        myPkmn.earnExp(earnedXP);
    }

    /**
     * In the final version of the program, an algorithm will determine this
     * value
     *
     * @param pkmn
     * @return the XP
     */
    protected int calculateEarnedXP(FightingPkmn pkmn) {
        return pkmn.getOdds();
    }

    /**
     * Says if the flee is a success
     * @return true or false
     */
    public boolean fleeSucceeds() {
        return Math.random() * 255
                < (myPkmn.getSpeed() * 32 / (ennemy.getSpeed() / 4))
                + 30 * ++fleeTries;
    }
    // </editor-fold>
}
