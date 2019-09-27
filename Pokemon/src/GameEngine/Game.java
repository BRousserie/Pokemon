
/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <brousserie@iut.u-bordeaux.fr> and <tpedrero@iut.u-bordeaux.fr>
 * wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy us a beer in return Baptiste and Tony
 * ----------------------------------------------------------------------------
 */
package GameEngine;

import Character.Player;
import Character.Trainer;
import Fight.FightingPkmn;
import FileIO.DataReader;
import FileIO.ReaderException;
import FileIO.Savable;
import Map.Zone;
import View.GameView;
import com.eclipsesource.json.JsonObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;

/**
 * Main class of the project, that puts things together.
 * It is a Singleton. You can access it through Game.getGame()
 * 
 * @author Baptiste
 */
public class Game extends Application implements Savable<JsonObject>
{
    
    // The instance of the singleton class
    private static Game game;
    
    public static void initializeGame() {
        game = new Game();
    }
    
    /**
     * Gets the actual game
     *
     * @return the game
     */
    public static Game getGame()
    {
        return game;
    }
    
    /**
     * @param args
     *
     * We've choosed to use french dialogs because we know French names for
     * Pokemon, towns, items... and the same goes for our classmates.
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    
    // <editor-fold defaultstate="collapsed" desc="Instances' attributes">
    // You can access Database using DataStorage
    private final DataStorage datas = new DataStorage();;
    
    // Scenario of the current event, containing Iterable instructions of its script
    private Scenario scenario;
    
    // Zone the player is located in
    private Zone currentZone;
    
    // Number of the next Event the player should see
    private int nextStoryEvent;
    
    // Player's character
    private Player myPlayer;
    
    // Name of the player's rival
    private String rivalName;
    
    // Class handling visual things
    private GameView gameView;
    
    // Current fight the player's engaged in
    private Fight fight;
    
    // Counter of the amounts of Pokemon that attacked the player in this zone
    private int pokemonMetInZone = 0;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    /**
     * Gets the fight the player's engaged in
     * 
     * @return the fight the player's engaged in
     */
    public Fight getFight()
    {
        return fight;
    }

    /**
     * Sets the current fight and changes the game scene
     *
     * @param fight the new fight
     */
    public void setFight(Fight fight)
    {
        this.fight = fight;
        this.gameView.setScene("FightView");
    }

    /**
     * Gets the current scenario event
     * 
     * @return the current scenario event
     */
    public Scenario getScenario()
    {
        return scenario;
    }

    /**
     * Sets the current senario event
     *
     * @param scenario the new Scenario event to execute
     */
    public void setScenario(Scenario scenario)
    {
        this.scenario = scenario;
    }

    /**
     * Gets the data storage of the game
     * 
     * @return the data storage of the game
     */
    public DataStorage getDatas()
    {
        return datas;
    }

    /**
     * Gets the zone where the player is currently
     * 
     * @return the zone where the player is currently
     */
    public Zone getCurrentZone()
    {
        return currentZone;
    }

    /**
     * Sets the zone where the player is, 
     * and looks for associated scenario event
     * 
     * @param newZone the zone where the player goes
     */
    public void setCurrentZone(Zone newZone)
    {
        currentZone = newZone;
        lookForScenarioEvent(newZone);
    }

    /**
     * Gets the number of the current story event
     * 
     * @return the number of the current story event
     */
    public int getCurrentStoryEvent()
    {
        return nextStoryEvent;
    }

    /**
     * Sets the number of the next story event to play to newValue
     *
     * @param newValue the value of the next event to play
     */
    public void setCurrentStoryEvent(int newValue)
    {
        nextStoryEvent = newValue;
    }

    /**
     *
     * @return myPlayer
     */
    public Player getPlayer()
    {
        return myPlayer;
    }

    /**
     *
     *
     * @param myPlayer
     */
    public void setMyPlayer(Player myPlayer)
    {
        this.myPlayer = myPlayer;
    }

    /**
     *
     * @return rivalName
     */
    public String getRivalName()
    {
        return rivalName;
    }

    /**
     *
     * @param rivalName
     */
    public void setMyRival(String rivalName)
    {
        this.rivalName = rivalName;
    }

    /**
     *
     * @return gameView
     */
    public GameView getGameView()
    {
        return gameView;
    }
    // </editor-fold>
    
    /**
     * Says if we can start a fight
     *
     * @return boolean
     * @throws ReaderException
     */
    public boolean possiblyLaunchFight() throws ReaderException
    {
        if (Math.random() * currentZone.getMeetingTrainerProba() > 1) {
            setFight(new TrainerFight(Trainer.generateEnnemy(myPlayer)));
            return true;
        } else if (pokemonMetInZone < currentZone.getMeetingPkmnProba()
                   && Math.random() * currentZone.getMeetingPkmnProba() > 1) {
            pokemonMetInZone++;
            currentZone.searchWildPokemon();
            return true;
        }
        return false;
    }

    /**
     * 
     */
    public void initialize()
    {
        nextStoryEvent = 0;
        try {
            currentZone = datas.getLoadedZone("BOURG PALETTE");
        } catch (ReaderException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Lauches the game view o fthe game
     * @param primaryStage the stage
     */
    @Override
    public void start(final Stage primaryStage)
    {
        game = this;
        
        gameView = new GameView(game, primaryStage);
        gameView.launch();

        primaryStage.setTitle("Pokemon Text Adventure !");
        primaryStage.setScene(gameView.getScene());
        primaryStage.show();
    }

    /**
     * Moves the player to another zone
     *
     * @param newZone
     * @throws ReaderException
     */
    public void goToZone(Zone newZone) throws ReaderException
    {
        if (!possiblyLaunchFight())
            if (!lookForScenarioEvent(newZone)) {
                currentZone = newZone;
                pokemonMetInZone = 0;
                gameView.setScene("MainScreen");
            }
    }

    /**
     * Says if the is a scenario event 
     * @param newZone
     * @return true or false
     */
    private boolean lookForScenarioEvent(Zone newZone)
    {
        if (datas.getScenario().size() > nextStoryEvent
            && datas.getScenario().get(nextStoryEvent).getEventZone()
                        .equals(newZone.getName())) {
            scenario = datas.getScenario().get(nextStoryEvent);
            if (datas.getScenario().get(nextStoryEvent).isHasFXML())
                gameView.setScene(scenario.getEventFile());
            else {
                currentZone = newZone;
                pokemonMetInZone = 0;
                gameView.setScene("Scenario");
            }
            nextStoryEvent++;
            return true;
        }
        return false;
    }

    /**
     * Sets the rival
     */
    public void fightRival()
    {
        setFight(new TrainerFight(getRival()));
    }

    /**
     * gets the rival
     * @return rival 
     */
    public Trainer getRival()
    {
        try {
            String[] rivalStats = DataReader.readFileArray(myPlayer.getStarter(), "" + (nextStoryEvent));

            ArrayList<FightingPkmn> currentPkmns = new ArrayList<>();
            for (int i = 0; i < Integer.parseInt(rivalStats[1]); i++)
                currentPkmns.add(new FightingPkmn(
                        rivalStats[2 + i * 2], Integer.parseInt(rivalStats[3 + i * 2]), i));
            return new Trainer(rivalName, currentPkmns, new ArrayList<>());
        } catch (ReaderException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }


    /**
     * Converts the Game into a JsonObject.
     *
     * @return JsonObject containing all needed informations on the game
     */
    @Override
    public JsonObject save()
    {
        return new JsonObject().add("myPlayer", myPlayer.save())
                .add("rivalName", rivalName)
                .add("currentZone", currentZone.getName())
                .add("currentStoryEvent", nextStoryEvent)
                .add("pkmnMet", pokemonMetInZone);
    }

    /**
     * Says if the game can be loaded
     * @param save
     * @return true or false
     */
    public boolean loadGame(JsonObject save)
    {
        try {
            myPlayer = new Player(save.get("myPlayer").asObject());
            rivalName = save.getString("rivalName", null);
            currentZone = datas.getLoadedZone(save.getString("currentZone", null));
            nextStoryEvent = save.getInt("currentStoryEvent", 0);
            scenario = (datas.getScenario().size() > nextStoryEvent)
                       ? datas.getScenario().get(nextStoryEvent)
                       : null;
            pokemonMetInZone = save.getInt("pkmnMet", 0);
            myPlayer.setLastPokeCenter(Game.getGame().getDatas()
                    .getLoadedZone(save.getString("pokecenter", "BOURG PALETTE")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
