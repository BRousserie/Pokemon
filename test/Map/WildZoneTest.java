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
public class WildZoneTest {

    @Test
    public void testWildZone() {
        DataStorage data = new DataStorage();
        try {
            WildZone route1 = (WildZone)data.getLoadedZone("ROUTE1");
            Assert.assertEquals("ROUTE1", route1.name);
            Assert.assertEquals(3, route1.meetingPkmnProba);
            Assert.assertEquals(0, route1.meetingDressorProba);
            Assert.assertEquals(2, route1.accessibleZones.size());
            Assert.assertEquals("BOURG PALETTE", route1.accessibleZones.get(0));
            Assert.assertEquals(1, route1.items.size());
            Assert.assertEquals(7, route1.meetablePkmns.size());
            Assert.assertEquals("Roucool", route1.meetablePkmns.get(0).getName());
            
            WildZone route2 = (WildZone) data.getLoadedZone("ROUTE2");
            Assert.assertEquals("ROUTE2", route2.name);
            Assert.assertEquals(1, route2.meetingPkmnProba);
            Assert.assertEquals(0, route2.meetingDressorProba);
            Assert.assertEquals(4, route2.accessibleZones.size());
            Assert.assertEquals("JADIELLE", route2.accessibleZones.get(0));
            Assert.assertEquals(3, route2.items.size());
            Assert.assertEquals(12, route2.getMeetablePkmns().size());
            Assert.assertEquals("Roucool", route2.getMeetablePkmns().get(0).getName());
            
            for (int i = 3; i < 19; i++) {
                WildZone route = (WildZone) data.getLoadedZone("ROUTE"+i);
                System.out.println(route.getName());
                Assert.assertTrue(!route.getName().isEmpty());
            }
            for (int i = 19; i < 22; i++) {
                WildZone route = (WildZone) data.getLoadedZone("CHENAL"+i);
                Assert.assertTrue(!route.getName().isEmpty());
            }
            for (int i = 22; i < 26; i++) {
                WildZone route = (WildZone) data.getLoadedZone("ROUTE"+i);
                Assert.assertTrue(!route.getName().isEmpty());
            }
            
            WildZone route12 = (WildZone)data.getLoadedZone("ROUTE12");
            Assert.assertEquals(5, route12.getFishablePkmns().get(1).getLevel()[0]);
            Assert.assertEquals(15, route12.getFishablePkmns().get(1).getLevel()[1]);
        } catch (ReaderException ex) {
            Logger.getLogger(WildZoneTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
