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
public class CapturedPkmnTest {
    @Test
    public void testCapturedPkmn() throws ReaderException {
        CapturedPkmn Salameche = new CapturedPkmn("Salamèche", 5, 0);
        
        Assert.assertTrue("0,6".equals(Salameche.getSize()));
        Assert.assertTrue("8,5".equals(Salameche.getWeight()));
        Assert.assertTrue("De récentes études prouvent que la flamme qui brûle sur la queue de Salamèche indique son état de santé, mais aussi son caractère. S'il se sent faible, la flamme sera toute petite. Et si par malheur sa flamme s'éteint, il meurt, car c'est le seul moyen pour lui de se refroidir.".equals(Salameche.getDescription()));
        Assert.assertTrue(3 == Salameche.getExpCurve());
        Assert.assertTrue("Reptincel".equals(Salameche.getEvolution().getName()));
        Assert.assertTrue("16".equals(Salameche.getEvolution().getCondition()));
        Assert.assertTrue(0 == Salameche.getExp());
        Assert.assertTrue(135 == Salameche.getLevelUpXP());
        Assert.assertTrue(null == Salameche.getItem());
        
        CapturedPkmn Ortide = new CapturedPkmn("Ortide", 15, 0);
        Assert.assertTrue("PIERRE PLANTE".equals(Ortide.getEvolution().getCondition()));
        
        Salameche.earnExp(10);
        Assert.assertTrue(10 == Salameche.getExp());
        Salameche.earnExp(Salameche.getLevelUpXP());
        Assert.assertTrue(6 == Salameche.getLevel());
        Assert.assertTrue(10 == Salameche.getExp());
        
        CapturedPkmn evolution = new CapturedPkmn("Salamèche", 15, 0);
        evolution.earnExp(evolution.getLevelUpXP());
        PkmnStats reptincel = Game.getGame().getDatas().getLoadedPkmn("Reptincel");
        Assert.assertTrue(reptincel.getName().equals(evolution.getName()));
        Assert.assertTrue(58 == evolution.getPkmnSpecies().getBasicHp());
        
        for(String pkmnName : PkmnStats.getPOKEDEX()) {
            new CapturedPkmn(pkmnName, 50, 0);
        }
    }
}
