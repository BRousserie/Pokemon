/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Map;

import Fight.FightingPkmn;
import FileIO.DataReader;
import GameEngine.Fight;
import GameEngine.Game;
import java.util.ArrayList;

/**
 *
 * @author Baptiste
 */
public class SpecialZone extends Zone {
    private int meetingPkmnProba;
    private ArrayList<String> wildPkmns;

    public static Zone loadZoneFromFile(String name) {
        try {
            String[] zoneInfos = DataReader.readFileArray("specials", name);
            ArrayList<String> items = new ArrayList<>();
            ArrayList<String> wildPkmns = new ArrayList<>();
            ArrayList<String> accessibleZones = new ArrayList<>();
            int part = 0;
            for (int i = 3; i < zoneInfos.length; i++) {
                switch (zoneInfos[i]) {
                    case "ITEM":
                        part = 1;
                        break;
                    case "HERBE":
                        part = 2;
                        break;
                    default:
                        switch (part) {
                            case 0 :
                                accessibleZones.add(zoneInfos[i]);
                                break;
                            case 1 :
                                items.add(zoneInfos[i]);
                                break;
                            case 2 :
                                wildPkmns.add(zoneInfos[i]);
                                break;
                        }
                }
            }
            return new SpecialZone(name, Integer.parseInt(zoneInfos[1]),
                    Integer.parseInt(zoneInfos[2]), name,
                    accessibleZones, items, wildPkmns);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SpecialZone(String name, int meetingDressorProba, int meetingPkmnProba,
            String description, ArrayList<String> accessibleZones,
            ArrayList<String> items, ArrayList<String> wildPkmns) {
        super(name, meetingDressorProba, description, accessibleZones, items, new ArrayList<>());
        this.meetingPkmnProba = meetingPkmnProba;
        this.wildPkmns = wildPkmns;
    }

    @Override
    public void searchWildPokemon() {
        int dice = (int) (Math.random() * 100);
        float sum = 0;
        for (String pkmn : wildPkmns) {
            sum += 100/wildPkmns.size();
            if (sum >= dice) {
                Game.getGame().setFight(new Fight(new FightingPkmn(
                        pkmn, Game.getGame().getPlayer().getAvgLvl(), 0)));
                break;
            }
        }
    }

    @Override
    public int getMeetingPkmnProba() {
        return meetingPkmnProba;
    }

    @Override
    public ArrayList<String> getShop() {
        return new ArrayList<>();
    }

    @Override
    public boolean hasArena() {
        return false;
    }

    @Override
    public boolean hasPokeCenter() {
        return false;
    }

    @Override
    public String getZoneType() {
        return "special";
    }
}
