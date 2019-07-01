/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameEngine;

import Character.Player;
import Character.Trainer;
import Fight.FightingPkmn;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Baptiste
 */
public class GameTest
{

    @Test
    public void testGetRival()
    {
        Game.initializeGame();
        
        Game game = Game.getGame();
        Player p = new Player("p");
        game.setMyPlayer(p);
        game.setMyRival("Regis");

        String[] starters = {"Bulbizarre", "Carapuce", "Salamèche"};
        for (String starter : starters) {
            System.out.println(starter);
            p.setStarter(starter);

            game.setCurrentStoryEvent(1);
            Trainer r = game.getRival();
            Assert.assertTrue(r.getName().equals("Regis"));
            Assert.assertTrue(r.getAlivePkmns() == 1);
            Assert.assertTrue(r.getPkmns().size() == 1);
            Assert.assertTrue(r.getPkmns().get(0).getLevel() == 5);
            for (FightingPkmn pkmn : r.getPkmns())
                Assert.assertTrue(pkmn.getAttacks().size() != 0);

            game.setCurrentStoryEvent(4);
            r = game.getRival();
            Assert.assertTrue(r.getAlivePkmns() == 2);
            Assert.assertTrue(r.getPkmns().size() == 2);
            Assert.assertTrue(r.getPkmns().get(0).getLevel() == 9);
            Assert.assertTrue(r.getPkmns().get(0).getName().equals("Roucool"));

            game.setCurrentStoryEvent(5);
            r = game.getRival();
            Assert.assertTrue(r.getPkmns().size() == 4);
            Assert.assertTrue(r.getPkmns().get(1).getLevel() == 15);
            Assert.assertTrue(r.getPkmns().get(1).getName().equals("Abra"));

            game.setCurrentStoryEvent(6);
            r = game.getRival();
            Assert.assertTrue(r.getPkmns().size() == 4);
            Assert.assertTrue(r.getPkmns().get(1).getLevel() == 16);
            Assert.assertTrue(r.getPkmns().get(1).getName().equals("Rattatac"));

            game.setCurrentStoryEvent(7);
            r = game.getRival();
            Assert.assertTrue(r.getPkmns().size() == 5);
            Assert.assertTrue(r.getPkmns().get(4).getLevel() == 25);
            Assert.assertTrue(r.getPkmns().get(3).getName().equals("Kadabra"));

            game.setCurrentStoryEvent(8);
            r = game.getRival();
            Assert.assertTrue(r.getPkmns().size() == 5);
            Assert.assertTrue(r.getPkmns().get(4).getLevel() == 40);
            Assert.assertTrue(r.getPkmns().get(3).getName().equals("Alakazam"));

            game.setCurrentStoryEvent(9);
            r = game.getRival();
            Assert.assertTrue(r.getPkmns().size() == 6);
            Assert.assertTrue(r.getPkmns().get(4).getLevel() == 50);
            Assert.assertTrue(r.getPkmns().get(1).getName().equals("Rhinocorne"));

            game.setCurrentStoryEvent(10);
            r = game.getRival();
            Assert.assertTrue(r.getPkmns().size() == 6);
            Assert.assertTrue(r.getPkmns().get(5).getLevel() == 65);
            Assert.assertTrue((r.getPkmns().get(5).getName().equals("Dracaufeu")
                               || r.getPkmns().get(5).getName().equals("Tortank")
                               || r.getPkmns().get(5).getName().equals("Florizarre")));

        }
    }
}
