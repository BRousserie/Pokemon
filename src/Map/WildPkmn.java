/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Map;

import java.util.Objects;

/**
 * Class for storing the informations of the wild Pokemon of a zone
 *
 * @author brousserie
 */
public class WildPkmn
{

    // <editor-fold defaultstate="collapsed" desc="Attributes">
    //Wild Pokemon's name
    private final String name;
    //Wild Pokemon's level - is 0 if its level is random in an interval
    private final int level;
    //Wild Pokemon's interval of possible levels - is null if its level is constant
    private final int[] levelInterval = new int[2];
    //Probability to meet that one wild Pokemon among all the wild pokemons of the zone
    private final int proba;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Contructor">
    /**
     * Constructor for creating WildPkmn instances using splitted infos from
     * file
     *
     * @param pkmnInfo splitted wild pokemon's datas, corresponding to
     * attributes
     */
    WildPkmn(String pkmnInfo)
    {
        int[] splits = {pkmnInfo.indexOf(":"), pkmnInfo.indexOf("-")};
        if (pkmnInfo.contains("~")) {
            int split = pkmnInfo.indexOf("~");
            levelInterval[0] = Integer.parseInt(pkmnInfo.substring(splits[0] + 1, split));
            levelInterval[1] = Integer.parseInt(pkmnInfo.substring(split + 1, splits[1]));
            level = 0;
        } else
            level = Integer.parseInt(pkmnInfo.substring(splits[0] + 1, splits[1]));
        name = pkmnInfo.substring(0, splits[0]);
        proba = Integer.parseInt(pkmnInfo.substring(splits[1] + 1));
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters / Setters">
    /**
     * Get wild pokemon's name
     *
     * @return wild pokemon's name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the wild pokemon's level as a single slot array, or the interval of
     * possible levels as a two slot array
     *
     * @return wild pokemon's level / interval of possible levels
     */
    public int[] getLevel()
    {
        return (level != 0) ? new int[]{level} : levelInterval;
    }

    /**
     * Get the probability to meet the wild pokemon, among all others wild
     * pokemons of the zone, as a percentage
     *
     * @return the probability to meet this wild pokemon
     */
    public int getProba()
    {
        return proba;
    }
    // </editor-fold>

    
    /**
     * 
     * @return 
     */
    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.name);
        hash = 79 * hash + this.level;
        hash = 79 * hash + this.proba;
        return hash;
    }

    /**
     * 
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final WildPkmn other = (WildPkmn) obj;
        if (this.level != other.level)
            return false;
        if (this.proba != other.proba)
            return false;
        if (!Objects.equals(this.name, other.name))
            return false;
        return true;
    }
}
