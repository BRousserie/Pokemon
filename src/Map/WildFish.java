/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Map;

/**
 *
 * @author Baptiste
 */
public class WildFish extends WildPkmn {

    private String canne;

    /**
     * Contructor for the Class WildFish
     *
     * @param pkmnInfo
     * @param canne
     */
    public WildFish(String pkmnInfo, String canne) {
        super(pkmnInfo);
        this.canne = canne;
    }
}
