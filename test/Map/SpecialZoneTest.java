/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Map;

import FileIO.ReaderException;
import GameEngine.DataStorage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Baptiste
 */
public class SpecialZoneTest
{

    @Test
    public void testSpecialZone()
    {
        DataStorage data = new DataStorage();

        try {
            SpecialZone Centrale = (SpecialZone) data.getLoadedZone("CENTRALE");
            Assert.assertTrue(Centrale.meetingTrainerProba == 0);
            Assert.assertTrue(Centrale.getMeetingPkmnProba() == 4);
            Assert.assertTrue(Centrale.getAccessibleZones().size() == 1);
            Assert.assertTrue(Centrale.accessibleZones.get(0).equals("ROUTE 10"));
            Assert.assertTrue(Centrale.items.size() == 7);

            List<String> specials = new ArrayList<>();
            Collections.addAll(specials, "CENTRALE", "FORET DE JADE", "GROTTE", "GROTTE INCONNUE", "ILES ECUME", "MANOIR DE CRAMOIS'ILE", "L'OCEANE", "PARC SAFARI", "REPERE ROCKET DU CASINO", "ROUTE VICTOIRE", "SIEGE SOCIAL DE LA SYLPHE SARL", "SOUTERRAIN EST-OUEST", "SOUTERRAIN NORD-SUD", "TOUR POKEMON", "TUNNEL TAUPIQUEUR", "VILLA DE LEO");
            SpecialZone special;
            for (String s : specials) {
                special = (SpecialZone) data.getLoadedZone(s);
                Assert.assertTrue(s.equals(special.getName()));
            }

        } catch (ReaderException ex) {
            Logger.getLogger(SpecialZoneTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
