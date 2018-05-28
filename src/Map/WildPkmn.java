/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Map;

import java.util.Objects;

/**
 *
 * @author brousserie
 */
public class WildPkmn {
    private String name;
    private int level;
    private int[] levelInterval = new int[2];
    private int proba;

    WildPkmn(String pkmnInfo) {
        int[] splits = {pkmnInfo.indexOf(":"), pkmnInfo.indexOf("-")};
        if (pkmnInfo.contains("~")) {
            int split = pkmnInfo.indexOf("~");
            levelInterval[0] = Integer.parseInt(pkmnInfo.substring(splits[0]+1, split));
            levelInterval[1] = Integer.parseInt(pkmnInfo.substring(split+1, splits[1]));
            level = 0;
        } else {
            level = Integer.parseInt(pkmnInfo.substring(splits[0]+1, splits[1]));
        }
        name = pkmnInfo.substring(0, splits[0]);
        proba = Integer.parseInt(pkmnInfo.substring(splits[1]+1));
    }

    public String getName() {
        return name;
    }

    public int[] getLevel() {
        if (level != 0) {
        return new int[] {level};
        } else {
            return levelInterval;
        }
    }

    public int getProba() {
        return proba;
    }

    
    
    
    
    
    
    
    
    
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.name);
        hash = 79 * hash + this.level;
        hash = 79 * hash + this.proba;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WildPkmn other = (WildPkmn) obj;
        if (this.level != other.level) {
            return false;
        }
        if (this.proba != other.proba) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
}
