/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameEngine;

import Character.Trainer;
import Fight.FightingPkmn;
import Item.Item;
import java.util.ArrayList;

/**
 *
 * @author brousserie
 */
public class DressorFight extends Fight {
    private final Trainer ennemyDressor;
    private final boolean isArena;
    
    public DressorFight(Trainer ennemyDressor) {
        super(ennemyDressor.getPkmns().get(0));
        this.ennemyDressor = ennemyDressor;
        this.isArena = false;
    }
    
    public DressorFight(Trainer ennemyDressor, boolean isArena) {
        super(ennemyDressor.getPkmns().get(0));
        this.ennemyDressor = ennemyDressor;
        this.isArena = isArena;
    }
    
    @Override
    public ArrayList<FightingPkmn> getEnnemyPkmns() {
        return ennemyDressor.getPkmns();
    }

    /**
     * Checks if both players have pokemons available to fight. If not, prints
     * to the screen who won and ends the fighting loop
     */
    @Override
    public void checkIfHasAWinner(boolean ennemyDown) {
        myPkmn.giveEVs(ennemy.getEVs());
        if (ennemyDown) {
            giveRewards();
            giveExp(super.calculateEarnedXP());
            playerWon = true;
            hasWinner = true;
            if (isArena) {
                me.wonAFightIn(Game.getGame().getCurrentZone().getName());
            }
        } else {
            if (me.getAlivePkmns() == 0) {
                playerWon = false;
                hasWinner = true;
            }
        }
    }
    
    /**
     * If you win, you earn the inventory of your ennemy.
     */
    @Override
    protected void giveRewards() {
        me.getBag().exchangeMoney(ennemyDressor.getBag().getPokeDollars());
        for(Item reward : ennemyDressor.getBag().getItems().keySet()) {
            me.getBag().getItems().put(reward, ennemyDressor.getBag().getItems().get(reward));
        }
    }
}
