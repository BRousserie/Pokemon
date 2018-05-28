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

import Character.Player;
import GameEngine.Game;
import GameEngine.Scenario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Baptiste
 */
public class NewGameController implements Initializable {

    @FXML
    private VBox textOutput;
    @FXML
    private Button next;
    @FXML
    private Button skip;
    @FXML
    private GridPane QCM1;
    @FXML
    private TextField UserInput;
    @FXML
    private Button chooseUserInput;
    @FXML
    private Button chooseRed;
    @FXML
    private Button chooseSacha;
    @FXML
    private Button choosePaul;
    @FXML
    private GridPane QCM2;
    @FXML
    private TextField UserInputRival;
    @FXML
    private Button chooseUserInputRival;
    @FXML
    private Button chooseBlue;
    @FXML
    private Button chooseRegis;
    @FXML
    private Button chooseJean;

    Game game = Game.getGame();
    private Scenario.ScenarioScript<String> script
            = new Scenario("Intro").Instructions();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chooseUserInput.setDisable(true);
        chooseUserInput.setText("Entrez un nom ci-dessus");
        UserInput.textProperty().addListener(c -> {
            chooseUserInput.setText(UserInput.getText());
                chooseUserInput.setDisable(
                        UserInput.textProperty().length().get() < 3
                    ||  UserInput.textProperty().length().get() > 12);});
        
        chooseUserInputRival.setDisable(true);
        chooseUserInputRival.setText("Entrez un nom ci-dessus");
        UserInputRival.textProperty().addListener(c -> {
                chooseUserInputRival.setText(UserInputRival.getText());
                chooseUserInputRival.setDisable(
                        UserInputRival.textProperty().length().get() < 3
                    ||  UserInputRival.textProperty().length().get() > 12);});

        executeScript(script.next());

        next.setOnMouseClicked(e -> executeScript(script.next()));
        
        skip.setOnMouseClicked(e -> {
            while (!script.current().startsWith("qcm")
                && !script.current().startsWith("END")) {
                script.next();
            }
            executeScript(script.next());
        });

        QCM1.getChildren().stream()
                .filter(btn -> btn instanceof Button)
                .map(btn -> (Button) btn)
                .forEach(button -> {
                    button.setOnMouseClicked(e -> 
                            setPlayerName(button.textProperty().getValue()));
                });

        QCM2.getChildren().stream()
                .filter(btn -> btn instanceof Button)
                .map(btn -> (Button) btn)
                .forEach(button -> {
                    button.setOnMouseClicked(e -> 
                            setRivalName(button.textProperty().getValue()));
                });
    }

    private void setPlayerName(String name) {
        game.setMyPlayer(new Player(name));
        QCM1.setVisible(false);
        QCM1.setDisable(true);
        next.setDisable(false);
        skip.setDisable(false);
        executeScript(script.next());
    }
    private void setRivalName(String name) {
        game.setMyRival(name);
        QCM2.setVisible(false);
        QCM2.setDisable(true);
        next.setDisable(false);
        skip.setDisable(false);
        executeScript(script.next());
    }
    public void executeScript(String instruction) {
        if (instruction.startsWith("show")) {
            if (instruction.contains("@playerName")) {
                instruction = instruction.replace("@playerName", game.getPlayer().getName());
            }
            if (instruction.contains("@rivalName")) {
                instruction = instruction.replace("@rivalName", game.getRival());
            }
            Label newLine = new Label(instruction.substring(4));
            newLine.setWrapText(true);
            newLine.setPadding(new Insets(0, 0, 10, 0));
            textOutput.getChildren().add(newLine);
        } 
        else if (instruction.startsWith("qcm")) {
            switch (instruction) {
                case "qcm1":
                    QCM1.setDisable(false);
                    QCM1.setVisible(true);
                    next.setDisable(true);
                    skip.setDisable(true);
                    break;
                case "qcm2":
                    QCM2.setDisable(false);
                    QCM2.setVisible(true);
                    next.setDisable(true);
                    skip.setDisable(true);
                    break;
            }
            executeScript(script.next());
        } else if (instruction.startsWith("//")) {
            executeScript(script.next());
        } else if (instruction.contains("END")) {
            game.initialize();
            game.getGameView().setScene("MainScreen");
        }
    }
}
