
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

public class Game extends Application implements Savable<JsonObject> {

    private static Game game;

    public static Game getGame() {
        return game;
    }

    private DataStorage datas = new DataStorage();
    private Scenario scenario;
    private Zone currentZone;
    private int currentStoryEvent;
    private Player myPlayer = new Player("");
    private String rivalName;
    private GameView gameView;
    private Fight fight;
    private int pokemonMetInZone = 0;

    /**
     *
     * @return fight
     */
    public Fight getFight() {
        return fight;
    }

    /**
     * Sets the fight view
     *
     * @param fight
     */
    public void setFight(Fight fight) {
        this.fight = fight;
        this.gameView.setScene("FightView");
    }

    /**
     *
     * @return scenario
     */
    public Scenario getScenario() {
        return scenario;
    }

    /**
     * Sets the senario of the game
     *
     * @param scenario
     */
    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    /**
     *
     * @return datas
     */
    public DataStorage getDatas() {
        return datas;
    }

    /**
     *
     * @return currentZone
     */
    public Zone getCurrentZone() {
        return currentZone;
    }

    public void setCurrentZone(Zone newZone) {
        currentZone = newZone;
        lookForScenarioEvent(newZone);
    }

    /**
     * Moves the player
     *
     * @param newZone
     * @throws ReaderException
     */
    public void goToZone(Zone newZone) throws ReaderException {
        if (!possiblyLaunchFight()) {
            if (!lookForScenarioEvent(newZone)) {
                currentZone = newZone;
                pokemonMetInZone = 0;
                gameView.setScene("MainScreen");
            }
        }
    }

    private boolean lookForScenarioEvent(Zone newZone) {
        if (datas.getScenario().size() > currentStoryEvent
                && datas.getScenario().get(currentStoryEvent).getEventZone()
                        .equals(newZone.getName())) {
            scenario = datas.getScenario().get(currentStoryEvent);
            if (datas.getScenario().get(currentStoryEvent).isHasFXML()) {
                gameView.setScene(scenario.getEventFile());
            } else {
                currentZone = newZone;
                pokemonMetInZone = 0;
                gameView.setScene("Scenario");
            }
            currentStoryEvent++;
            return true;
        }
        return false;
    }

    /**
     *
     */
    public void fightRival() {
        try {
            String[] rivalStats = DataReader.readFileArray(
                    myPlayer.getStarter(), "" + (currentStoryEvent - 1));

            ArrayList<FightingPkmn> currentPkmns = new ArrayList<>();
            for (int i = 0; i < Integer.parseInt(rivalStats[1]); i += 2) {
                currentPkmns.add(new FightingPkmn(
                        rivalStats[3 + i], Integer.parseInt(rivalStats[4 + i]), i));
            }

            setFight(new DressorFight(
                    new Trainer(rivalName, currentPkmns,
                            Integer.parseInt(rivalStats[2]))));
        } catch (ReaderException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @return currentStoryEvent
     */
    public int getCurrentStoryEvent() {
        return currentStoryEvent;
    }

    /**
     * Sets the events of the game
     *
     * @param newValue
     */
    public void setCurrentStoryEvent(int newValue) {
        currentStoryEvent = newValue;
    }

    /**
     *
     * @return myPlayer
     */
    public Player getPlayer() {
        return myPlayer;
    }

    /**
     *
     *
     * @param myPlayer
     */
    public void setMyPlayer(Player myPlayer) {
        this.myPlayer = myPlayer;
    }

    /**
     *
     * @return rivalName
     */
    public String getRival() {
        return rivalName;
    }

    /**
     *
     * @param rivalName
     */
    public void setMyRival(String rivalName) {
        this.rivalName = rivalName;
    }

    /**
     *
     * @return gameView
     */
    public GameView getGameView() {
        return gameView;
    }

    /**
     * Says if we can start a fight
     *
     * @return boolean
     * @throws ReaderException
     */
    public boolean possiblyLaunchFight() throws ReaderException {
        if (Math.random() * currentZone.getMeetingTrainerProba() > 1) {
            setFight(new DressorFight(Trainer.generateEnnemy(myPlayer)));
            return true;
        } else if (pokemonMetInZone < currentZone.getMeetingPkmnProba()
                && Math.random() * currentZone.getMeetingPkmnProba() > 1) {
            pokemonMetInZone++;
            currentZone.searchWildPokemon();
            return true;
        }
        return false;
    }

    public void initialize() {
        currentStoryEvent = 0;
        try {
            currentZone = datas.getLoadedZone("BOURG PALETTE");
        } catch (ReaderException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void start(final Stage primaryStage) {
        game = this;

        gameView = new GameView(game, primaryStage);
        gameView.launch();

        primaryStage.setTitle("Pokemon Text Adventure !");
        primaryStage.setScene(gameView.getScene());
        primaryStage.show();
    }

    /**
     * @param args
     *
     * /*
     * We've choosen to use french dialogs because we know French names for
     * pokemons, towns, items... and the same goes for our classmates.
     *
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public JsonObject save() {
        return new JsonObject().add("myPlayer", myPlayer.save())
                .add("rivalName", rivalName)
                .add("currentZone", currentZone.getName())
                .add("currentStoryEvent", currentStoryEvent);
    }

    public boolean loadGame(JsonObject save) {
        try {
            myPlayer = new Player(save.get("myPlayer").asObject());
            rivalName = save.getString("rivalName", null);
            currentZone = datas.getLoadedZone(save.getString("currentZone", null));
            currentStoryEvent = save.getInt("currentStoryEvent", 0);
            scenario = (datas.getScenario().size() > currentStoryEvent)
                    ? datas.getScenario().get(currentStoryEvent)
                    : null;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
