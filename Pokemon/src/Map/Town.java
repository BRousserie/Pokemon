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

/**
 *
 * @author Baptiste
 */
public class Town extends Zone
{

    // <editor-fold defaultstate="collapsed" desc="Attributes">
    /**
     * The list of the shop
     */
    protected final ArrayList<String> shop;

    /**
     * The arena
     */
    protected final boolean hasArena;

    /**
     * The pokeCenter
     */
    protected final boolean hasPokeCenter;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Contructor">
    /**
     *  Contructor of the Class Town
     * @param name
     * @param hasArena
     * @param hasPokeCenter
     * @param meetingTrainerProba
     * @param description
     * @param shop
     * @param items
     * @param accessibleZones
     * @param fishablePkmns 
     */
    private Town(String name, boolean hasArena, boolean hasPokeCenter,
                 int meetingTrainerProba, String description, ArrayList<String> shop,
                 ArrayList<String> items, ArrayList<String> accessibleZones,
                 ArrayList<WildPkmn> fishablePkmns)
    {
        super(name, meetingTrainerProba, description, accessibleZones, items, fishablePkmns);
        this.shop = shop;
        this.hasArena = hasArena;
        this.hasPokeCenter = hasPokeCenter;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters / Setters">
    /**
     * Gets the shop
     * @return shop 
     */
    @Override
    public ArrayList<String> getShop()
    {
        return shop;
    }

    /**
     * The arena
     * @return true or false
     */
    @Override
    public boolean hasArena()
    {
        return hasArena;
    }

    /**
     * The pokeCenter
     * @return true / false
     */
    @Override
    public boolean hasPokeCenter()
    {
        return hasPokeCenter;
    }

    /**
     * Gets the zone type
     * @return ville
     */
    @Override
    public String getZoneType()
    {
        return "villes";
    }

    /**
     * No probability
     * @return
     */
    @Override
    public int getMeetingPkmnProba()
    {
        return 0;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * Not possible in towns
     */
    @Override
    public void searchWildPokemon()
    {
    }

    

    /**
     *
     * Loads the town from the file
     * @param name
     * @return a new town
     */
    public static Town loadZoneFromFile(String name)
    {
        try {
            String[] zoneInfos = DataReader.readFileArray("villes", name);
            ArrayList<String> items = new ArrayList<>();
            ArrayList<String> shop = new ArrayList<>();
            ArrayList<WildPkmn> fishablePkmns = new ArrayList<>();
            ArrayList<String> accessibleZones = new ArrayList<>();

            int part = 0;
            for (int i = 5; i < zoneInfos.length; i++)
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
            return new Town(name, Boolean.parseBoolean(zoneInfos[1]),
                            Boolean.parseBoolean(zoneInfos[2]), Integer.parseInt(zoneInfos[3]),
                            zoneInfos[4], shop, items, accessibleZones, fishablePkmns);
        } catch (Exception e) {
            System.out.println("failed loading " + name);
            return null;
        }
    }
    // </editor-fold>
}
