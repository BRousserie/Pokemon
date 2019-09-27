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
import GameEngine.Game;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Baptiste
 */
public class ArenaTest
{

    @Test
    public void testArena() throws ReaderException
    {
        Game.initializeGame();
        Game game = Game.getGame();
        game.initialize();

        game.setCurrentZone(game.getDatas().getLoadedZone("ARGENTA"));
        Arena Argenta = game.getDatas().getLoadedArena();
        Assert.assertTrue(Argenta.getPrerequisites().equals("Carte"));
        Assert.assertTrue(Argenta.dressors.size() == 2);
        Assert.assertTrue(Argenta.dressors.get(0).getName().equals("Dresseur Jr M"));
        Assert.assertTrue(Argenta.dressors.get(1).getName().equals("Pierre"));
        Assert.assertTrue(Argenta.dressors.get(1).getPkmns().size() == 2);
        Assert.assertTrue(Argenta.dressors.get(1).getPkmns().get(0).getName().equals("Racaillou"));
        Assert.assertTrue(Argenta.dressors.get(1).getPkmns().get(0).getLevel() == 12);
        Assert.assertTrue(Argenta.dressors.get(1).getPkmns().get(1).getName().equals("Onix"));
        Assert.assertTrue(Argenta.dressors.get(1).getPkmns().get(1).getLevel() == 14);
        Assert.assertTrue(Argenta.dressors.get(1).getBag().contains("Patience"));
        Assert.assertTrue(Argenta.dressors.get(1).getBag().contains("Badge Roche"));

        game.setCurrentZone(game.getDatas().getLoadedZone("AZURIA"));
        Arena Azuria = game.getDatas().getLoadedArena();
        Assert.assertTrue(Azuria.getPrerequisites().equals("Badge Roche"));

        game.setCurrentZone(game.getDatas().getLoadedZone("CARMIN-SUR-MER"));
        Arena CarminSurMer = game.getDatas().getLoadedArena();
        Assert.assertTrue(CarminSurMer.getPrerequisites().equals("Badge Cascade"));

        game.setCurrentZone(game.getDatas().getLoadedZone("CELADOPOLE"));
        Arena Céladopole = game.getDatas().getLoadedArena();
        Assert.assertTrue(Céladopole.getPrerequisites().equals("Badge Foudre"));

        game.setCurrentZone(game.getDatas().getLoadedZone("PARMANIE"));
        Arena Parmanie = game.getDatas().getLoadedArena();
        Assert.assertTrue(Parmanie.getPrerequisites().equals("Badge Prisme"));

        game.setCurrentZone(game.getDatas().getLoadedZone("SAFRANIA"));
        Arena Safrania = game.getDatas().getLoadedArena();
        Assert.assertTrue(Safrania.getPrerequisites().equals("Badge Âme"));

        game.setCurrentZone(game.getDatas().getLoadedZone("CRAMOIS'ILE"));
        Arena CramoisIle = game.getDatas().getLoadedArena();
        Assert.assertTrue(CramoisIle.getPrerequisites().equals("Badge Marais"));

        game.setCurrentZone(game.getDatas().getLoadedZone("JADIELLE"));
        Arena Jadielle = game.getDatas().getLoadedArena();
        Assert.assertTrue(Jadielle.getPrerequisites().equals("Badge Volcan"));

        game.setCurrentZone(game.getDatas().getLoadedZone("PLATEAU INDIGO"));
        Arena PlateauIndigo = game.getDatas().getLoadedArena();
        Assert.assertTrue(PlateauIndigo.getPrerequisites().equals("Badge Terre"));
    }
}
