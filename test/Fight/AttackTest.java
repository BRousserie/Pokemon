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
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Baptiste
 */
public class AttackTest {
    @Test
    public void testLoadAtkFromFile() throws ReaderException {
        Attack riposte = Attack.loadAtkFromFile("Riposte");
        
        Assert.assertEquals("Riposte", riposte.getName());
        Assert.assertEquals("COMBAT", riposte.getType());
        Assert.assertEquals("Physique", riposte.getCategory());
        Assert.assertEquals(255, riposte.getPower());
        Assert.assertEquals(100, riposte.getAccuracy());
        Assert.assertEquals(20, riposte.getPPMAX());
        Assert.assertEquals("Inflige le double des degats subis par une attaque de type Normal ou Combat durant le tour, echoue sinon", riposte.getEffect());
        System.out.println("Riposte succesfully loaded");
        
        Attack pique = Attack.loadAtkFromFile("Pique");
        Assert.assertEquals("Pique", pique.getName());
        Assert.assertEquals("VOL", pique.getType());
        Assert.assertEquals("Physique", pique.getCategory());
        Assert.assertEquals(140, pique.getPower());
        Assert.assertEquals(90, pique.getAccuracy());
        Assert.assertEquals(5, pique.getPPMAX());
        Assert.assertEquals("Attaque en deux tours, n'agit pas au premier", pique.getEffect());
        System.out.println("Pique succesfully loaded");
        
        
        Attack findsNotFittingCases = Attack.loadAtkFromFile("pique");
        Assert.assertEquals("Pique", findsNotFittingCases.getName());
        System.out.println("pique succesfully loaded");
        
        
        Attack dardvenin = Fight.Attack.loadAtkFromFile("DARD VENIN");
        Attack meteores = Fight.Attack.loadAtkFromFile("METEORES");
    }
}
