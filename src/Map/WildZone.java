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

import Fight.FightingPkmn;
import FileIO.DataReader;
import FileIO.ReaderException;
import GameEngine.Fight;
import GameEngine.Game;
import java.util.ArrayList;

public class WildZone extends Zone {

    protected final ArrayList<WildPkmn> wildPkmns;
    protected final ArrayList<WildPkmn> surfingPkmns;
    protected final int meetingPkmnProba;

    public WildZone(ArrayList<WildPkmn> meetablePkmns, int meetingPkmnProba,String description,
            ArrayList<WildPkmn> fishablePkmns, ArrayList<WildPkmn> surfingPkmns, 
            String name, ArrayList<String> accessibleZones, ArrayList<String> items,
            int meetingDressorProba ) {
        super(name, meetingDressorProba, description, accessibleZones, items, fishablePkmns);
        this.wildPkmns = meetablePkmns;
        this.surfingPkmns = surfingPkmns;
        this.meetingPkmnProba = meetingPkmnProba;
    }

    @Override
    public void searchWildPokemon() {
        int dice = (int) (Math.random() * 100);
        int sum = 0;
        for (WildPkmn pkmn : wildPkmns) {
            sum += pkmn.getProba();
            if (sum >= dice) {
                Game.getGame().setFight(new Fight(
                        new FightingPkmn(pkmn)));
                break;
            }
        }
    }

    @Override
    public String getZoneType() {
        return "routes";
    }

    @Override
    public int getMeetingPkmnProba() {
        return meetingPkmnProba;
    }

    public ArrayList<WildPkmn> getMeetablePkmns() {
        return wildPkmns;
    }

    public ArrayList<WildPkmn> getFishablePkmns() {
        return fishablePkmns;
    }

    public ArrayList<WildPkmn> getSurfingPkmns() {
        return surfingPkmns;
    }
    
    public static WildZone loadZoneFromFile(String name) throws ReaderException {
            String[] zoneInfos = DataReader.readFileArray("routes", name);
            ArrayList<String> items = new ArrayList<>();
            ArrayList<WildPkmn> meetablePkmns = new ArrayList<>();
            ArrayList<WildPkmn> fishablePkmns = new ArrayList<>();
            ArrayList<WildPkmn> surfingPkmns = new ArrayList<>();
            ArrayList<String> accessibleZones = new ArrayList<>();
            int part = 0;
            for (int i = 4; i < zoneInfos.length; i++) {
                switch (zoneInfos[i]) {
                    case "ITEM":
                        part = 1;
                        break;
                    case "HERBE":
                        part = 2;
                        break;
                    case "PECHES":
                        part = 3;
                        break;
                    case "SURF":
                        part = 4;
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
                                meetablePkmns.add(new WildPkmn(zoneInfos[i]));
                                break;
                            case 3 :
                                fishablePkmns.add(new WildFish(
                                        zoneInfos[i].substring(0, zoneInfos[i].indexOf("/")),
                                        zoneInfos[i].substring(zoneInfos[i].indexOf("/")+1)));
                                break;
                            case 4 :
                                surfingPkmns.add(new WildPkmn(zoneInfos[i]));
                                break;
                        }
                }
            }
            return new WildZone(meetablePkmns, Integer.parseInt(zoneInfos[1]), zoneInfos[3],
                    fishablePkmns, surfingPkmns,  name, accessibleZones,
                    items, Integer.parseInt(zoneInfos[2]));
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
}
