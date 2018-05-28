/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <brousserie@iut.u-bordeaux.fr> and <tpedrero@iut.u-bordeaux.fr>
 * wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy us a beer in return Baptiste and Tony
 * ----------------------------------------------------------------------------
 */
package Pokemons;

import FileIO.DataReader;
import FileIO.ReaderException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Baptiste
 */
public class PkmnStats {

    private static final ArrayList<String> POKEDEX = new ArrayList<>();

    private final String name;
    private final String type;
    private final int[] basicStats;
    private final int captureRate;
    private final HashMap<String, Integer> attackLearningLevel;
    private final int[] EVReward;
    private final int XPReward = 100; //TODO

    public PkmnStats(String name, String type, int[] basicStats,
            HashMap<String, Integer> attackLearningLevel,
            HashMap<String, Integer> EVs, int captureRate) {
        this.name = name;
        this.type = type;
        this.basicStats = basicStats;
        this.captureRate = captureRate;
        this.attackLearningLevel = attackLearningLevel;
        EVReward = determinateEVs(EVs);
    }
    
    public static PkmnStats loadPkmnStatsFromFile(String pkmnName) throws ReaderException {
        try {
            String[] pkmnLine = DataReader.readFileArray("pokemonStats", pkmnName);
            int[] stats = new int[5];
            for (int i = 0; i < 5; i++) {
                stats[i] = Integer.parseInt(pkmnLine[i + 2]);
            }

            PkmnStats newPkmn = new PkmnStats(pkmnName, pkmnLine[1], stats,
                    DataReader.linkDataTXT(pkmnLine[9]),
                    DataReader.linkDataTXT(pkmnLine[11]),
                    Integer.parseInt(pkmnLine[7]));
            return newPkmn;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't load "+pkmnName);
            return null;
        }
    }

    private static int[] determinateEVs(HashMap<String, Integer> EVs) {
        int[] EVRewards = {0, 0, 0, 0, 0};
        for (String stat : EVs.keySet()) {
            switch (stat) {
                case ("hp"):
                    EVRewards[0] = EVs.get(stat);
                    break;
                case ("atk"):
                    EVRewards[1] = EVs.get(stat);
                    break;
                case ("def"):
                    EVRewards[2] = EVs.get(stat);
                    break;
                case ("spe"):
                    EVRewards[3] = EVs.get(stat);
                    break;
                case ("speed"):
                    EVRewards[4] = EVs.get(stat);
                    break;
            }
        }
        return EVRewards;
    }

    public static ArrayList<String> getPOKEDEX() {
        return POKEDEX;
    }

    public HashMap<String, Integer> getAttackLearningLevel() {
        return attackLearningLevel;
    }

    public static int getPkmnID(String pkmnName) {
        return POKEDEX.indexOf(pkmnName);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int[] getBasicStats() {
        return basicStats;
    }

    public int getBasicHp() {
        return basicStats[0];
    }

    public int getBasicAtk() {
        return basicStats[1];
    }

    public int getBasicDef() {
        return basicStats[2];
    }

    public int getBasicSpe() {
        return basicStats[3];
    }

    public int getBasicSpeed() {
        return basicStats[4];
    }

    public int getCaptureRate() {
        return captureRate;
    }

    public HashMap<String, Integer> getLearnableAtks() {
        return attackLearningLevel;
    }

    public int[] getEVReward() {
        return EVReward;
    }

    public int getXPReward() {
        return XPReward;
    }

    static {
        String[] pkmnList = {"Bulbizarre", "Herbizarre", "Florizarre", "Salamèche", "Reptincel", "Dracaufeu",
            "Carapuce", "Carabaffe", "Tortank", "Chenipan", "Chrysacier", "Papilusion", "Aspicot", "Coconfort",
            "Dardargnan", "Roucool", "Roucoups", "Roucarnage", "Rattata", "Rattatac", "Piafabec", "Rapasdepic",
            "Abo", "Arbok", "Pikachu", "Raichu", "Sabelette", "Sablaireau", "Nidoran f", "Nidorina", "Nidoqueen",
            "Nidoran m", "Nidorino", "Nidoking", "Mélofée", "Mélodelfe", "Goupix", "Feunard", "Rondoudou",
            "Grodoudou", "Nosferapti", "Nosferalto", "Mystherbe", "Ortide", "Rafflesia", "Paras", "Parasect",
            "Mimitoss", "Aéromite", "Taupiqueur", "Triopikeur", "Miaouss", "Persian", "Psykokwak", "Akwakwak",
            "Férosinge", "Colossinge", "Caninos", "Arcanin", "Ptitard", "Tétarte", "Tartard", "Abra", "Kadabra",
            "Alakazam", "Machoc", "Machopeur", "Mackogneur", "Chétiflor", "Boustiflor", "Empiflor", "Tentacool",
            "Tentacruel", "Racaillou", "Gravalanch", "Grolem", "Ponyta", "Galopa", "Ramoloss", "Flagadoss",
            "Magneti", "Magneton", "Canarticho", "Doduo", "Dodrio", "Otaria", "Lamantine", "Tadmorv", "Grotadmorv",
            "Kokiyas", "Crustabri", "Fantominus", "Spectrum", "Ectoplasma", "Onix", "Soporifik", "Hypnomade",
            "Krabby", "Krabboss", "Voltorbe", "Électrode", "Noeunoeuf", "Noadkoko", "Osselait", "Ossatueur",
            "Kicklee", "Tygnon", "Excelangue", "Smogo", "Smogogo", "Rhinocorne", "Rhinoféros", "Leveinard",
            "Saquedeneu", "Kangourex", "Hypotrempe", "Hypocéan", "Poissirène", "Poissoroy", "Stari", "Staross",
            "M. Mime", "Insécateur", "Lippoutou", "Élektek", "Magmar", "Scarabrute", "Tauros", "Magicarpe",
            "Léviator", "Lokhlass", "Métamorph", "Évoli", "Aquali", "Voltali", "Pyroli", "Porygon", "Amonita",
            "Amonistar", "Kabuto", "Kabutops", "Ptéra", "Ronflex", "Artikodin", "Électhor", "Sulfura", "Minidraco",
            "Draco", "Dracolosse", "Mewtwo", "Mew"};
        for (short i = 0; i < pkmnList.length; i++) {
            POKEDEX.add(pkmnList[i]);
        }
    }

    public static String getRandomPkmn() {
        return POKEDEX.get((int) (POKEDEX.size() * Math.random()));
    }
}
