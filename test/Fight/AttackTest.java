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

import FileIO.ReaderException;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Baptiste
 */
public class AttackTest
{

    @Test
    public void testLoadAtkFromFile() throws ReaderException
    {
        Attack riposte = Attack.loadAtkFromFile("Riposte");

        Assert.assertEquals("Riposte", riposte.getName());
        Assert.assertEquals("COMBAT", riposte.getType());
        Assert.assertEquals(CapacityCategory.Physique, riposte.getCategory());
        Assert.assertEquals(255, riposte.getPower());
        Assert.assertEquals(100, riposte.getAccuracy());
        Assert.assertEquals(20, riposte.getPPMAX());

        Attack pique = Attack.loadAtkFromFile("Pique");
        Assert.assertEquals("Pique", pique.getName());
        Assert.assertEquals("VOL", pique.getType());
        Assert.assertEquals(CapacityCategory.Physique, pique.getCategory());
        Assert.assertEquals(140, pique.getPower());
        Assert.assertEquals(90, pique.getAccuracy());
        Assert.assertEquals(5, pique.getPPMAX());

        Attack dardvenin = Fight.Attack.loadAtkFromFile("Dard venin");
        Attack meteores = Fight.Attack.loadAtkFromFile("Météores");
    }
}
