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

import FileIO.DataReader;
import java.util.ArrayList;

public class Town extends Zone {

    protected final ArrayList<String> shop;
    protected final boolean hasArena;
    protected final boolean hasPokeCenter;

    private Town(String name, boolean hasArena, boolean hasPokeCenter,
            int meetingDressorProba, String description, ArrayList<String> shop,
            ArrayList<String> items, ArrayList<String> accessibleZones,
            ArrayList<WildPkmn> fishablePkmns) {
        super(name, meetingDressorProba, description, accessibleZones, items, fishablePkmns);
        this.shop = shop;
        this.hasArena = hasArena;
        this.hasPokeCenter = hasPokeCenter;
    }

    @Override
    public ArrayList<String> getShop() {
        return shop;
    }

    @Override
    public boolean hasArena() {
        return hasArena;
    }

    @Override
    public boolean hasPokeCenter() {
        return hasPokeCenter;
    }

    @Override
    public String getZoneType() {
        return "villes";
    }

    @Override
    public void searchWildPokemon() {
    }

    @Override
    public void searchWildFish() {
        //TODO
    }

    @Override
    public int getMeetingPkmnProba() {
        return 0;
    }

    public static Town loadZoneFromFile(String name) {
        try {
            String[] zoneInfos = DataReader.readFileArray("villes", name);
            ArrayList<String> items = new ArrayList<>();
            ArrayList<String> shop = new ArrayList<>();
            ArrayList<WildPkmn> fishablePkmns = new ArrayList<>();
            ArrayList<String> accessibleZones = new ArrayList<>();
            
            int part = 0;
            for (int i = 5; i < zoneInfos.length; i++) {
                switch (zoneInfos[i]) {
                    case "ITEM":
                        part = 1;
                        break;
                    case "LINK":
                        part = 2;
                        break;
                    case "PECHE":
                        part = 3;
                        break;
                    default:
                        switch (part) {
                            case 0:
                                shop.add(zoneInfos[i]);
                                break;
                            case 1:
                                items.add(zoneInfos[i]);
                                break;
                            case 2:
                                accessibleZones.add(zoneInfos[i]);
                                break;
//                            case 3 :
//                                fishablePkmns.add(new WildPkmn(zoneInfos[i]));
//                                break;
                            default:
                                break;
                        }
                }
            }
            return new Town(name, Boolean.parseBoolean(zoneInfos[1]),
                    Boolean.parseBoolean(zoneInfos[2]), Integer.parseInt(zoneInfos[3]),
                    zoneInfos[4], shop, items, accessibleZones, fishablePkmns);
        } catch (Exception e) {
            System.out.println("failed loading " + name);
            return null;
        }
    }

    public void fightArena() {
        System.out.println("not available yet");
    }
}
