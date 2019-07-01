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
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Class allowing to create ready to fight characters
 */
public class Trainer implements Savable<JsonObject>
{
    // <editor-fold defaultstate="collapsed" desc="Attributes">
    /**
     * Trainer's name
     */
    protected String name;

    /**
     * List of Pokemons the Trainer carries
     */
    protected List<FightingPkmn> currentPkmns = new ArrayList<>();

    /**
     * Maximum number of Pokemon a trainer can carry
     */
    public final static int MAX_CURRENTPKMNS = 6;

    /**
     * Tainer's inventory
     */
    protected Inventory bag;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Constructor for creating ennemy Dressors : you have to compute his money
     *
     * @param name Trainer's name
     * @param currentPkmns List of Pokemon the trainer carries
     * @param items List of items the trainer carries
     */
    public Trainer(String name, List<FightingPkmn> currentPkmns, List<Item> items)
    {
        this.name = name;
        this.currentPkmns = currentPkmns;
        this.bag = new Inventory(items, computeDressorMoney());
    }

    /**
     * Constructor for creating ennemy Dressors that reward you with one Item
     *
     * @param name Trainer's name
     * @param currentPkmns List of Pokemon the trainer carries
     * @param items List of items the trainer carries
     */
    public Trainer(String name, List<FightingPkmn> currentPkmns, Item item)
    {
        this(name, currentPkmns, item.asList());
    }

    private int computeDressorMoney()
    {
        int money = 0;
        for (FightingPkmn p : currentPkmns)
            money += p.getOdds();
        return money;
    }

    /**
     * Constructor for creating Player's dressor : inventory is complete
     *
     * @param name Trainer's name
     * @param currentPkmns List of Pokemon the trainer carries
     * @param bag Trainer's inventory
     */
    public Trainer(String name, List<FightingPkmn> currentPkmns, Inventory bag)
    {
        this.name = name;
        this.currentPkmns = currentPkmns;
        this.bag = bag;
    }

    /**
     * Constructor for creating new Player, (he normally starts game with 5000$)
     *
     * @param name Trainer's name
     * @param money Trainer's money
     */
    public Trainer(String name, int money)
    {
        this(name, new ArrayList<>(), new Inventory(money));
    }

    /**
     * Constructor for loading Dressor from Json save
     *
     * @param trainer previously saved trainer 
     * @throws ReaderException if items of his bad are not found in database
     */
    public Trainer(JsonObject trainer) throws ReaderException
    {
        this(trainer.getString("name", null), new ArrayList<>(),
             new Inventory(trainer.get("bag").asObject()));
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    /**
     * @return the trainer's name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the amount of alive Pokemon the trainer carries
     */
    public int getAlivePkmns()
    {
        return (int) currentPkmns.stream()
                .filter(FightingPkmn::isAlive)
                .count();
    }

    /**
     * @return the average level of the alive Pokemon carried by the dressor
     */
    public int getAvgLvl()
    {
        return (int) currentPkmns.stream()
                .filter(FightingPkmn::isAlive)
                .mapToInt(FightingPkmn::getLevel)
                .average()
                .orElse(0);
    }

    /**
     * @return the List of Pokemon the character carries, not modifiable
     */
    public List<FightingPkmn> getPkmns()
    {
        return Collections.unmodifiableList(currentPkmns);
    }

    /**
     * @return the trainer's inventory
     */
    public Inventory getBag()
    {
        return bag;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * Set a new Pokemon as first Pokemon of the team
     * (which is the one that goes first in fight)
     *
     * @param newPkmn the new Pokemon to set first
     */
    public void swapPokemon(CapturedPkmn newPkmn)
    {
        FightingPkmn temporaryStoring = currentPkmns.get(0);
        currentPkmns.set(0, newPkmn);
        currentPkmns.set(currentPkmns.indexOf(newPkmn), temporaryStoring);
    }

    /**
     * Method to randomly generate a trainer to fight against
     *
     * @param myPlayer will be used to know how strong the new ennemy can be
     * @return the generated Ennemy
     * @throws ReaderException if it fails in loading the Trainer's pokemon or
     *                         their attack
     */
    public static Trainer generateEnnemy(Player myPlayer) throws ReaderException
    {
        String[] possibleNames = {"Fanny", "Nathan", "Tifanie", "Daphne", "Florentin", "Maxime", "Erwan", "Marwane", "Aurelien", "Cyril", "Guillaume",
                                  "Morgan", "Corentin", "Elie", "Pierre", "Gwenael", "Dawen", "Line", "Ivan", "Clement", "Aurelie", "Estelle", "Sarah", "Eliot", "Dylan",
                                  "Evan", "Mathieu", "Leo", "Laurie", "Yoann", "Aymeric", "Paul", "Emmanuel", "Agathe", "Vincent", "Martin", "Julian", "Yassine", "Nicolas",
                                  "Loup", "Alexandre", "Antoine", "Aurore", "Jeremie", "Lucas", "Lea", "Thomas", "Matteo", "Jerome", "Dorian", "Jean-Brice", "Theau", "Joel",
                                  "Loic", "Lucas", "Fabien", "Thibault", "Gaetan", "Marius", "Tony", "Adrian", "Eloi", "Bastien", "Mateo", "Melanie", "Luken", "Anthony",
                                  "Emilio", "Bettina", "Benjamin", "Baptiste", "Esteban", "Mathis", "Marianne", "Peio", "Gauthier", "Irvin", "Ange"};
        String name = possibleNames[(int) (Math.random() * possibleNames.length)];

        int nbOfPokemons = (int) (Math.random() * (myPlayer.getAlivePkmns() - 1)) + 1;
        List<FightingPkmn> ennemyPkmns = new ArrayList<>();
        for (int i = 0; i < nbOfPokemons; i++)
            ennemyPkmns.add(new FightingPkmn(PkmnStats.getRandomPkmn(),
                                             (int) (myPlayer.getAvgLvl() * 9 / 10 + Math.random()
                                                                                    * (myPlayer.getAvgLvl() / 10)), i));

        return new Trainer(name, ennemyPkmns, new ArrayList<>());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Save to Json">
    /**
     * Converts the player into a JsonObject.
     *
     * @return JsonObject containing all needed informations on the trainer
     */
    @Override
    public JsonObject save()
    {
        return new JsonObject().add("name", name)
                .add("bag", bag.save());
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="HashCode and Equals">
    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.name);
        hash = 89 * hash + Objects.hashCode(this.currentPkmns);
        hash = 89 * hash + Objects.hashCode(this.bag);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Trainer other = (Trainer) obj;
        if (!Objects.equals(this.name, other.name))
            return false;
        if (!Objects.equals(this.currentPkmns, other.currentPkmns))
            return false;
        if (!Objects.equals(this.bag, other.bag))
            return false;
        return true;
    }
    // </editor-fold>
}
