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

    // <editor-fold defaultstate="collapsed" desc="Attributes">
    private int meetingPkmnProba;
    private ArrayList<String> wildPkmns;
    // </editor-fold>

    /**
     * Loadsthe special zone from the file
     *
     * @param name
     * @returnc a new special zone
     */
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
                            case 0:
                                accessibleZones.add(zoneInfos[i]);
                                break;
                            case 1:
                                items.add(zoneInfos[i]);
                                break;
                            case 2:
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
    

    // <editor-fold defaultstate="collapsed" desc="Contructor">
    /**
     * Contructor of the Class SpecialZone
     *
     * @param name
     * @param meetingDressorProba
     * @param meetingPkmnProba
     * @param description
     * @param accessibleZones
     * @param items
     * @param wildPkmns
     */
    public SpecialZone(String name, int meetingDressorProba, int meetingPkmnProba,
            String description, ArrayList<String> accessibleZones,
            ArrayList<String> items, ArrayList<String> wildPkmns) {
        super(name, meetingDressorProba, description, accessibleZones, items, new ArrayList<>());
        this.meetingPkmnProba = meetingPkmnProba;
        this.wildPkmns = wildPkmns;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * Find a pokemon that appears in the zone
     */
    @Override
    public void searchWildPokemon() {
        int dice = (int) (Math.random() * 100);
        float sum = 0;
        for (String pkmn : wildPkmns) {
            sum += 100 / wildPkmns.size();
            if (sum >= dice) {
                Game.getGame().setFight(new Fight(new FightingPkmn(
                        pkmn, Game.getGame().getPlayer().getAvgLvl(), 0)));
                break;
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters / Setters">
    /**
     * Gets the probability of meeting a pokemon
     *
     * @return the probability
     */
    @Override
    public int getMeetingPkmnProba() {
        return meetingPkmnProba;
    }

    /**
     * Gets the shops in the zone
     *
     * @return a new list
     */
    @Override
    public ArrayList<String> getShop() {
        return new ArrayList<>();
    }

    /**
     * Gets the type of the zone
     *
     * @return special
     */
    @Override
    public String getZoneType() {
        return "special";
    }

    /**
     * Says if the zone has an arena
     *
     * @return true or false
     */
    @Override
    public boolean hasArena() {
        return false;
    }

    /**
     * Says if the zone has a pokecenter
     *
     * @return true or false
     */
    @Override
    public boolean hasPokeCenter() {
        return false;
    }
    // </editor-fold>

}
