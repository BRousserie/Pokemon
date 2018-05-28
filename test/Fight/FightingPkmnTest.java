/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <brousserie@iut.u-bordeaux.fr> and <tpedrero@iut.u-bordeaux.fr>
 * wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy us a beer in return Baptiste and Tony
 * ----------------------------------------------------------------------------
 */
package Fight;

import Fight.Attack;
import FileIO.ReaderException;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Baptiste
 */
public class FightingPkmnTest {

    @Test
    public void testFightingPkmn() throws ReaderException {
        /* We shall generate a large amount of times a new pokemon to check if
        * methods determinateIVs/Stats always succeed despite Math.random() call
         */
//        for (int n = 0; n < 500; n++) {
//            FightingPkmn Sablaireau = new FightingPkmn("Sablaireau", 36, 0);
//            Attack dardvenin = Fight.Attack.loadAtkFromFile("DARD VENIN", true);
//            Attack meteores = Fight.Attack.loadAtkFromFile("METEORES", true);
//            Attack[] attacks = {dardvenin, meteores};
//
//            Assert.assertTrue(Sablaireau.getAttacks()[0].getName().equals(attacks[0].getName()));
//            Assert.assertTrue(Sablaireau.getAttacks()[1].getName().equals(attacks[1].getName()));
//            Assert.assertTrue(Sablaireau.getPkmnSpecies().getName().equals("Sablaireau"));
//
//            for (int i = 0; i < 5; i++) {
//                Assert.assertTrue(Sablaireau.getIVs()[i] >= 0);
//                Assert.assertTrue(Sablaireau.getIVs()[i] < 16);
//                Assert.assertTrue(Sablaireau.getEVs()[i] == 0);
//                Assert.assertTrue(Sablaireau.getStatsVariations()[i] == 0);
//                if (i == 0) {
//                    Assert.assertTrue(Sablaireau.getStats()[i] >= (75 + 50) * 36 / 50 + 10);
//                    Assert.assertTrue(Sablaireau.getStats()[i] < (16 + 75 + 50) * 36 / 50 + 10);
//                } else {
//                    Assert.assertTrue(Sablaireau.getStats()[i] >= (Sablaireau.getPkmnSpecies().getBasicStats()[i]) * 36 / 50 + 5);
//                    Assert.assertTrue(Sablaireau.getStats()[i] < (16 + Sablaireau.getPkmnSpecies().getBasicStats()[i]) * 36 / 50 + 10);
//                }
//            }
//        }
//        FightingPkmn Preloaded = new FightingPkmn("Sablaireau", 36, 0);
//        Assert.assertTrue("REGARDE SI IL EST ECRIT DANS LA CONSOLE QUE SABLAIREAU ETAIT DEJA CHARGE".equals(Preloaded.getName()));
//        Preloaded.loseHp(50);
//        Assert.assertTrue(Preloaded.getMaxHP()-50 == Preloaded.getHp());
//        Preloaded.loseHp(-20);
//        Assert.assertTrue(Preloaded.getMaxHP()-30 == Preloaded.getHp());
//        Preloaded.restoreHP();
//        Assert.assertTrue(Preloaded.getHp() == Preloaded.getMaxHP());
//        Preloaded.loseHp(Preloaded.getHp()+10);
//        Assert.assertTrue(0 == Preloaded.getHp());
//        Assert.assertTrue("REGARDE SI IL EST ECRIT DANS LA CONSOLE QUE SABLAIREAU EST K.O.".equals(Preloaded.getName()));
    }
}
