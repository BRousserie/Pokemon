/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameEngine;

import Character.Trainer;
import Fight.FightingPkmn;
import Item.Item;
import java.util.Collections;
import java.util.List;

/**
 * Class for fighting against trainers
 *
 * @author brousserie
 */
public class TrainerFight extends Fight
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    //The trainer you fight against
    private final Trainer ennemyTrainer;
    //True if this fight is taking part in an arena, which means
    //player's achievements should progress
    private final boolean isArena;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Constructor for fighting against trainers out of arenas
     *
     * @param ennemyTrainer trainer the player is going to fight against
     */
    public TrainerFight(Trainer ennemyTrainer)
    {
        this(ennemyTrainer, false);
    }

    /**
     * Constructor for fighting against trainers
     *
     * @param ennemyDressor trainer the player is going to fight against
     * @param isArena true if the trainer is part of an arena
     */
    public TrainerFight(Trainer ennemyDressor, boolean isArena)
    {
        super(ennemyDressor.getPkmns().get(0));
        this.ennemyTrainer = ennemyDressor;
        this.isArena = isArena;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters / Setters">
    /**
     * Method for getting a string telling the player who he fights against
     *
     * @return the message
     */
    @Override
    public String getStartingMessage()
    {
        return (isArena) ? "Vous affrontez " + ennemyTrainer.getName() + "!"
               : "Dresseur " + ennemyTrainer.getName() + " veut se battre!";
    }

    /**
     * Gets the list of ennemy pokemon
     * @return unmodifiable list
     */
    @Override
    public List<FightingPkmn> getEnnemyPkmns()
    {
        return Collections.unmodifiableList(ennemyTrainer.getPkmns());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Other methods">
    /**
     * Checks if both players have pokemons available to fight. If not, prints
     * to the screen who won and ends the fighting loop
     *
     * @param ennemyDown
     */
    @Override
    public void checkIfHasAWinner(boolean ennemyDown)
    {
        myPkmn.giveEVs(ennemy.getPkmnSpecies().getEVReward());
        if (ennemyDown && ennemyTrainer.getAlivePkmns() == 0) {
            giveRewards();
            //Pokemons owned by a trainer reward you with more experience
            giveExp(super.calculateEarnedXP(ennemy) + 60 + ennemy.getLevel() * 8);
            playerWon = true;
            hasWinner = true;
            if (isArena)
                me.wonAFightIn(Game.getGame().getCurrentZone().getName());
        } else
            if (me.getAlivePkmns() == 0) {
                playerWon = false;
                hasWinner = true;
            }
    }

    /**
     * If you win, you earn the inventory of your ennemy.
     */
    @Override
    protected void giveRewards()
    {
        me.getBag().exchangeMoney(ennemyTrainer.getBag().getPokeDollars());
        for (Item reward : ennemyTrainer.getBag().getItems())
            me.getBag().addItem(reward);
    }
    // </editor-fold>
}
