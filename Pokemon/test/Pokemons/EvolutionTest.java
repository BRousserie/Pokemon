/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <brousserie@iut.u-bordeaux.fr> and <tpedrero@iut.u-bordeaux.fr>
 * wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy us a beer in return Baptiste and Tony
 * ----------------------------------------------------------------------------
 */
package Pokemons;

import FileIO.ReaderException;
import GameEngine.Game;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Baptiste
 */
public class EvolutionTest
{

    @Test
    public void testEvolve() throws ReaderException
    {
        Game.initializeGame();
        
        CapturedPkmn Salameche = new CapturedPkmn("Salamèche", 15, 0);

        Assert.assertTrue("Salamèche".equals(Salameche.getName()));
        Assert.assertTrue("Reptincel".equals(Salameche.getEvolution().getName()));
        Assert.assertTrue("16".equals(Salameche.getEvolution().getCondition()));

        Assert.assertTrue("".equals(Salameche.getEvolution().evolve(Salameche.getLevel(), Salameche.getItem())));
        Salameche.levelUp();
        Assert.assertTrue("Reptincel".equals(Salameche.getName()));
    }
}
