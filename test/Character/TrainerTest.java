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

import Fight.FightingPkmn;
import GameEngine.Game;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Baptiste
 */
public class TrainerTest
{

    @Test
    public void testGenerateEnnemy() throws Exception
    {
        Game.initializeGame();
        
        Player myPlayer = new Player("Moi");
        myPlayer.addNewPokemon("Dracaufeu", 50);

        Trainer generatedEnnemy = Trainer.generateEnnemy(myPlayer);
        System.out.println(generatedEnnemy.getName());
        int nbOfEnnemyPkmns = 0;
        for (FightingPkmn pkmn : generatedEnnemy.getPkmns())
            nbOfEnnemyPkmns++;
        
        Assert.assertTrue(nbOfEnnemyPkmns == 1);
        Assert.assertTrue(generatedEnnemy.getPkmns().get(0).getLevel() >= 50 * 9 / 10);
        Assert.assertTrue(generatedEnnemy.getPkmns().get(0).getLevel() <= 50 * 11 / 10);

        myPlayer.addNewPokemon("Tortank", 60);
        myPlayer.addNewPokemon("Florizarre", 41);

        Assert.assertTrue(nbOfEnnemyPkmns >= 1);
        Assert.assertTrue(nbOfEnnemyPkmns <= 3);
        Assert.assertTrue(generatedEnnemy.getPkmns().get(0).getLevel() >= 50 * 9 / 10);
        Assert.assertTrue(generatedEnnemy.getPkmns().get(0).getLevel() <= 50 * 11 / 10);
    }
}
