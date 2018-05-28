/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <brousserie@iut.u-bordeaux.fr> and <tpedrero@iut.u-bordeaux.fr>
 * wrote this file. As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy us a beer in return Baptiste and Tony
 * ----------------------------------------------------------------------------
 */
package View;

import FileIO.DataReader;
import GameEngine.Game;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/**
 * Author tony
 */
public class GameView {

    private final Game game;

    private Scene scene;
    private Group group = new Group();

    public GameView(Game main, Stage primaryStage) {
        this.game = main;
        
        Font.loadFont(this.getClass().getResource("../resources/PokemonHollow.ttf").toString(), 24);
        Font.loadFont(this.getClass().getResource("../resources/PokemonSolid.ttf").toString(), 24);
        Font.loadFont(this.getClass().getResource("../resources/PokemonClassic.ttf").toString(), 16);
    }
    
    public void launch() {
        setScene((game.loadGame(DataReader.loadGame())) ? "MainScreen" : "NewGame");
        scene = new Scene(group, 1280, 720);
    }

    public Scene getScene() {
        return scene;
    }

    public Game getGame() {
        return game;
    }

    public Group getGroup() {
        return group;
    }

    public void setScene(String filename) {
        this.group.getChildren().clear();
        try {
            this.group.getChildren().addAll((Parent) new FXMLLoader(
                    getClass().getResource("../resources/" + filename + ".fxml")).load());
        } catch (Exception e) {
            System.out.println("Can't load " + filename + " : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setPkmnLabel(Label label, int pkmnIndex) {
        try {
            label.setText(game.getPlayer()
                    .getCapturedCurrentPkmns().get(pkmnIndex).toString());
        } catch (IndexOutOfBoundsException e) {
            label.setVisible(false);
        }
    }
}
