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

import GameEngine.Game;
import GameEngine.Scenario;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Baptiste
 */
public class ScenarioController implements Initializable {

    @FXML
    private VBox textOutput;
    @FXML
    private Button next;
    @FXML
    private Button skip;

    Game game = Game.getGame();
    Scenario.ScenarioScript<String> script = 
            game.getScenario().Instructions();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        executeScript(script.next());

        next.setOnMouseClicked(e -> executeScript(script.next()));
        
        skip.setOnMouseClicked(e -> {
            while (!script.current().startsWith("qcm")
                    && !script.current().startsWith("END")) {
                script.next();
            }
            if (script.current().startsWith("qcm")) {
                executeScript(script.previous());
            }
            executeScript(script.next());
        });
    }

    public void executeScript(String instruction) {
        if (instruction.startsWith("show")) {
            if (textOutput.getChildren().size() == 15) {
                textOutput.getChildren().remove(0);
            }
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
        } else if (instruction.startsWith("give")) {
            game.getPlayer().getBag().addItem(game.getDatas().getItem(instruction.substring(4)));
            executeScript(script.next());
        } else if (instruction.startsWith("rm")) {
            game.getPlayer().getBag().decrement(game.getDatas().getItem(instruction.substring(2)));
            executeScript(script.next());
        } else if (instruction.startsWith("//")) {
            executeScript(script.next());
        } else if (instruction.contains("END")) {
            game.getGameView().setScene("MainScreen");
        }
    }
}
