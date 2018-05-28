/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <brousserie@iut.u-bordeaux.fr> and <tpedrero@iut.u-bordeaux.fr>
 * wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy us a beer in return Baptiste and Tony
 * ----------------------------------------------------------------------------
 */
package Map;

import FileIO.ReaderException;
import GameEngine.DataStorage;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Baptiste
 */
public class TownTest {
    @Test
    public void testTown() {
        DataStorage data = new DataStorage();
        try {
            Town BourgPalette = (Town)data.getLoadedZone("BOURG PALETTE");
            Assert.assertEquals("BOURG PALETTE", BourgPalette.name);
            Assert.assertFalse(BourgPalette.hasArena);
            Assert.assertFalse(BourgPalette.hasPokeCenter);
            Assert.assertEquals(0, BourgPalette.meetingDressorProba);
            Assert.assertEquals(0, BourgPalette.shop.size());
            Assert.assertEquals(1, BourgPalette.items.size());
            Assert.assertEquals(2, BourgPalette.accessibleZones.size());
            
            Town Safrania = (Town)data.getLoadedZone("SAFRANIA");
            Assert.assertEquals("SAFRANIA", Safrania.name);
            Assert.assertTrue(Safrania.hasArena);
            Assert.assertTrue(Safrania.hasPokeCenter);
            Assert.assertEquals(1, Safrania.meetingDressorProba);
            Assert.assertEquals(4, Safrania.shop.size());
            Assert.assertEquals(3, Safrania.items.size());
            Assert.assertEquals(5, Safrania.accessibleZones.size());
        } catch (ReaderException ex) {
            Logger.getLogger(TownTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
