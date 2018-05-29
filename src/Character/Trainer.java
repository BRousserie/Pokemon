/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <brousserie@iut.u-bordeaux.fr> and <tpedrero@iut.u-bordeaux.fr>
 * wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy us a beer in return Baptiste and Tony
 * ----------------------------------------------------------------------------
 */
package Character;

import Fight.FightingPkmn;
import FileIO.ReaderException;
import FileIO.Savable;
import Item.Item;
import Pokemons.CapturedPkmn;
import Pokemons.PkmnStats;
import com.eclipsesource.json.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Class allowing to create ready to fight characters
 */
public class Trainer implements Savable<JsonObject> {

    //Dressor's name
    protected String name;
    //Dressor's current pokemons
    protected ArrayList<FightingPkmn> currentPkmns = new ArrayList<>();
    public final static int MAX_CURRENTPKMNS = 6;
    //Dressor's inventory
    protected Inventory bag;

    /**
     * Constructor for creating Dressors that reward you with one Item
     */
    public Trainer(String name, ArrayList<FightingPkmn> currentPkmns, Item item, int money) {
        this.name = name;
        this.currentPkmns = currentPkmns;
        this.bag = new Inventory(money, item);
    }

    /**
     * Constructor for creating Dressors that reward you with multiple Items
     */
    public Trainer(String name, ArrayList<FightingPkmn> currentPkmns, HashMap<Item, Integer> items, int money) {
        this.name = name;
        this.currentPkmns = currentPkmns;
        this.bag = new Inventory(money, items);
    }

    /**
     * Constructor for creating Dressors that reward you with money only
     */
    public Trainer(String name, ArrayList<FightingPkmn> currentPkmns, int money) {
        this.name = name;
        this.currentPkmns = currentPkmns;
        this.bag = new Inventory(money);
    }
    
    public Trainer(JsonObject dressor) throws ReaderException {
        name = dressor.getString("name", null);
        bag = new Inventory(dressor.get("bag").asObject());
    }

    /**
     * @return the dressor's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the dressor's amount of alive pokemons
     */
    public int getAlivePkmns() {
        int num = 0;
        for (FightingPkmn pkmn : currentPkmns) {
            if (pkmn.getHp() != 0) {
                num++;
            }
        }
        return num;
    }

    /**
     * @return the sum of the levels of each pokemons carried by the dressor
     */
    public int getAvgLvl() {
        int num = 0;
        for (FightingPkmn pkmn : currentPkmns) {
            num += pkmn.getLevel();
        }
        try {
            return (num / getAlivePkmns());
        } catch (ArithmeticException e) {
            return 0;
        }
    }

    /**
     * @return the List of pokemons the character carries
     */
    public ArrayList<FightingPkmn> getPkmns() {
        return currentPkmns;
    }
    
    public void swapPokemon(CapturedPkmn newPkmn) {
        FightingPkmn temporaryStoring = currentPkmns.get(0);
        currentPkmns.set(0, newPkmn);
        currentPkmns.set(currentPkmns.indexOf(newPkmn), temporaryStoring);
    }

    /**
     * @return the dressor's inventory
     */
    public Inventory getBag() {
        return bag;
    }

    /**
     * Method to randomly generate a dressor to fight against
     *
     * @param myPlayer will be used to know how strong the new ennemy can be
     * @return the generated Ennemy
     * @throws ReaderException if it fails in loading the Trainer's pokemon or
 their attack
     */
    public static Trainer generateEnnemy(Player myPlayer) throws ReaderException {
        String[] possibleNames = {"Fanny", "Nathan", "Tifanie", "Daphne", "Florentin", "Maxime", "Erwan", "Marwane", "Aurelien", "Cyril", "Guillaume",
            "Morgan", "Corentin", "Elie", "Pierre", "Gwenael", "Dawen", "Line", "Ivan", "Clement", "Aurelie", "Estelle", "Sarah", "Eliot", "Dylan",
            "Evan", "Mathieu", "Leo", "Laurie", "Yoann", "Aymeric", "Paul", "Emmanuel", "Agathe", "Vincent", "Martin", "Julian", "Yassine", "Nicolas",
            "Loup", "Alexandre", "Antoine", "Aurore", "Jeremie", "Lucas", "Lea", "Thomas", "Matteo", "Jerome", "Dorian", "Jean-Brice", "Theau", "Joel",
            "Loic", "Lucas", "Fabien", "Thibault", "Gaetan", "Marius", "Tony", "Adrian", "Eloi", "Bastien", "Mateo", "Melanie", "Luken", "Anthony",
            "Emilio", "Bettina", "Benjamin", "Baptiste", "Esteban", "Mathis", "Marianne", "Peio", "Gauthier", "Irvin", "Ange"};
        String name = possibleNames[(int) (Math.random() * possibleNames.length)];

        int nbOfPokemons = (int) (Math.random() * myPlayer.getAlivePkmns());
        if (nbOfPokemons == 0) {
            nbOfPokemons = 1;
        }
        ArrayList<FightingPkmn> ennemyPkmns = new ArrayList<>();
        for (int i = 0; i < nbOfPokemons; i++) {
            ennemyPkmns.add(new FightingPkmn(PkmnStats.getRandomPkmn(),
                    (int) (myPlayer.getAvgLvl() * 9 / 10 + Math.random()
                    * (myPlayer.getAvgLvl() / 10)), i));
        }

        int pokeDollars = (int) (Math.random() * 200 * nbOfPokemons);

        return new Trainer(name, ennemyPkmns, pokeDollars);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.name);
        hash = 89 * hash + Objects.hashCode(this.currentPkmns);
        hash = 89 * hash + Objects.hashCode(this.bag);
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
        final Trainer other = (Trainer) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.currentPkmns, other.currentPkmns)) {
            return false;
        }
        if (!Objects.equals(this.bag, other.bag)) {
            return false;
        }
        return true;
    }

    @Override
    public JsonObject save() {
        return new JsonObject().add("name", name)
                               .add("bag", bag.save());
    }
}
