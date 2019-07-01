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
import FileIO.DataWriter;
import GameEngine.Game;
import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Author tony
 */
public class GameView
{

    private final Game game;

    private Scene scene;
    private final Group group = new Group();

    private double movingWindowX;
    private double movingWindowY;

    /**
     * GameView Controller
     *
     * @param main
     * @param primaryStage
     */
    public GameView(Game main, Stage primaryStage)
    {
        this.game = main;

        //Load fonts
        Font.loadFont(this.getClass().getResource("../resources/PokemonHollow.ttf").toString(), 24);
        Font.loadFont(this.getClass().getResource("../resources/PokemonSolid.ttf").toString(), 24);
        Font.loadFont(this.getClass().getResource("../resources/PokemonClassic.ttf").toString(), 16);

        // Set stage
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);

        group.setOnMousePressed(e -> {
            movingWindowX = e.getScreenX() - primaryStage.getX();
            movingWindowY = e.getScreenY() - primaryStage.getY();
        });

        group.setOnMouseDragged(e -> {
            primaryStage.setX(e.getScreenX() - movingWindowX);
            primaryStage.setY(e.getScreenY() - movingWindowY);
        });
    }

    /**
     * Launch the scene
     */
    public void launch()
    {
        setScene((game.loadGame(DataReader.loadGame())) ? "MainScreen" : "NewGame");
        scene = new Scene(group, 1280, 720);
    }

    /**
     * get the scene
     *
     * @return
     */
    public Scene getScene()
    {
        return scene;
    }

    /**
     * gets the game
     *
     * @return
     */
    public Game getGame()
    {
        return game;
    }

    /**
     * gets the group
     *
     * @return
     */
    public Group getGroup()
    {
        return group;
    }

    /**
     * Sets the scene
     *
     * @param filename name of the fxml to load
     */
    public void setScene(String filename)
    {
        this.group.getChildren().clear();
        try {
            this.group.getChildren().addAll((Parent) new FXMLLoader(
                    getClass().getResource("../resources/" + filename + ".fxml")).load());
        } catch (Exception e) {
            System.out.println("Can't load " + filename + " : " + e.getMessage());
            e.printStackTrace();
        }
        if (filename.equals("MainScreen")) {
            Button exit = new Button("X");
            exit.setTranslateX(1220);
            exit.setTranslateY(20);
            exit.setPrefSize(40, 20);
            exit.setStyle("-fx-background-color: red;");
            exit.setOnMouseClicked(e -> showQuitMessage());
            this.group.getChildren().add(exit);
        }
    }

    /**
     * Displays a message when game quit
     */
    private void showQuitMessage()
    {
        if (game.getCurrentStoryEvent() > 0
            && !DataReader.loadGame().toString().equals(game.save().toString())) {
            Rectangle back = new Rectangle(0, 0, 1280, 720);
            back.setOpacity(0.5);
            this.group.getChildren().add(back);

            Label message = new Label("Voulez-vous sauvegarder avant de quitter ?");
            message.setLayoutX(430);
            message.setLayoutY(250);
            message.setStyle("-fx-font-family: \"Pokemon Classic Regular\";"
                             + "-fx-text-fill: snow;");
            this.group.getChildren().add(message);

            ArrayList<Button> buttons = new ArrayList<>();
            Button save = new Button("Oui");
            buttons.add(save);
            setButtonAttributes(save, 425);
            save.setOnMouseClicked(e -> {
                DataWriter.saveGame(game);
                System.exit(0);
            });

            Button leave = new Button("Non");
            buttons.add(leave);
            setButtonAttributes(leave, 615);
            leave.setOnMouseClicked(e -> System.exit(0));

            Button cancel = new Button("Annuler");
            buttons.add(cancel);
            setButtonAttributes(cancel, 805);
            cancel.setOnMouseClicked(e -> {
                this.group.getChildren().remove(back);
                this.group.getChildren().remove(message);
                this.group.getChildren().removeAll(buttons);
            });

            this.group.getChildren().addAll(buttons);
        } else
            System.exit(0);
    }

    /**
     * Set the attributes of the buttons
     *
     * @param button
     * @param layoutX
     */
    private void setButtonAttributes(Button button, int layoutX)
    {
        button.setLayoutX(layoutX);
        button.setLayoutY(300);
        button.setPrefSize(100, 50);
        button.setStyle("-fx-font-family: \"Pokemon Classic Regular\";\n"
                        + "-fx-background-color : linear-gradient(#ffb700, #cc9c00);\n"
                        + "-fx-effect: dropshadow( three-pass-box, rgba(0,0,0,0.4) , 15, 0.0 , 0 , 0 );");
    }

    /**
     * sets the pokemon label
     *
     * @param label
     * @param pkmnIndex
     */
    public void setPkmnLabel(Label label, int pkmnIndex)
    {
        try {
            label.setText(game.getPlayer()
                    .getCapturedCurrentPkmns().get(pkmnIndex).toString());
        } catch (IndexOutOfBoundsException e) {
            label.setVisible(false);
        }
    }
}
