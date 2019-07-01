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
import GameEngine.DataStorage;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Baptiste
 */
public class PkmnStatsTest
{

    @Test
    public void testLoadPkmnStatsFromFile() throws ReaderException
    {
        DataStorage datas = new DataStorage();

        PkmnStats Bulbizarre = datas.getLoadedPkmn("Bulbizarre");
        Assert.assertTrue(Bulbizarre.getName().equals("Bulbizarre"));
        Assert.assertTrue(Bulbizarre.getType().equals("PLANTE-POISON"));
        int[] bulbizarreStats = {45, 49, 49, 65, 45};
        for (int i = 0; i < 5; i++)
            Assert.assertTrue(Bulbizarre.getBasicStats()[i] == bulbizarreStats[i]);
        Assert.assertTrue(Bulbizarre.getCaptureRate() == 0);
        Assert.assertTrue(Bulbizarre.getLearnableAtks().get("CHARGE") == 0);
        Assert.assertTrue(Bulbizarre.getLearnableAtks().get("FOUET LIANES") == 13);
        Assert.assertTrue(Bulbizarre.getEVReward()[0] == 0);
        Assert.assertTrue(Bulbizarre.getEVReward()[3] == 1);

        PkmnStats Taupiqueur = datas.getLoadedPkmn("Taupiqueur");
        Assert.assertTrue(Taupiqueur.getName().equals("Taupiqueur"));
        Assert.assertTrue(Taupiqueur.getType().equals("SOL"));
        int[] taupiqueurStats = {10, 55, 25, 45, 95};
        for (int i = 0; i < 5; i++)
            Assert.assertTrue(Taupiqueur.getBasicStats()[i] == taupiqueurStats[i]);
        Assert.assertTrue(Taupiqueur.getCaptureRate() == 255);
        Assert.assertTrue(Taupiqueur.getLearnableAtks().get("GRIFFE") == 0);
        Assert.assertTrue(Taupiqueur.getLearnableAtks().get("JET DE SABLE") == 15);
        Assert.assertTrue(Taupiqueur.getEVReward()[1] == 0);
        Assert.assertTrue(Taupiqueur.getEVReward()[4] == 1);

        for (String pkmnName : PkmnStats.getPOKEDEX()) {
            PkmnStats loadedPkmn = datas.getLoadedPkmn(pkmnName);
            Assert.assertEquals(pkmnName, loadedPkmn.getName());
        }
    }
}
