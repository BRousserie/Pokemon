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
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Baptiste
 */
public class PkmnTypeTest {
    @Test
    public void testGetDamageRate() throws ReaderException {
        Assert.assertTrue(0 == PkmnType.getDamageRate(PkmnType.NORMAL, PkmnType.SPECTR, true));
        Assert.assertTrue(0.5 == PkmnType.getDamageRate(PkmnType.INSECT, PkmnType.FEU, true));
        Assert.assertTrue(1 == PkmnType.getDamageRate(PkmnType.NORMAL, PkmnType.NORMAL, true));
        Assert.assertTrue(2 == PkmnType.getDamageRate(PkmnType.DRAGON, PkmnType.DRAGON, true));
    }
}
