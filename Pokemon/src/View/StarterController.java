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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author Baptiste
 */
public class StarterController implements Initializable
{

    @FXML
    private Button next, skip, chooseBulbizarre, chooseCarapuce,
            chooseSalameche;
    @FXML
    private GridPane QCM1;

    @FXML
    private Label textOutput;

    Game game = Game.getGame();
    private final Scenario.ScenarioScript<String> script
                                                  = game.getDatas().getScenario().get(0).Instructions();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        textOutput.setWrapText(true);

        next.setOnMouseClicked(e -> executeScript(script.next()));

        skip.setOnMouseClicked(e -> {
            while (!script.current().startsWith("qcm")
                   && !script.current().startsWith("END")
                   && !script.current().startsWith("fight"))
                script.next();
            executeScript(script.next());
        });

        QCM1.getChildren().stream()
                .filter(btn -> btn instanceof Button)
                .map(btn -> (Button) btn)
                .forEach(button -> {
                    button.setOnMouseClicked(e
                            -> setPlayerStarter(button.textProperty().getValue()));
                });
        QCM1.setVisible(false);
        QCM1.setDisable(true);

        textOutput.setText(script.next());
    }

    /**
     * Sets the player name
     *
     * @param name
     */
    private void setPlayerStarter(String name)
    {
        textOutput.setText("");
        game.getPlayer().setStarter(name);
        QCM1.setVisible(false);
        QCM1.setDisable(true);
        next.setDisable(false);
        skip.setDisable(false);
        executeScript(script.next());
    }

    /**
     * Executes the event
     *
     * @param instruction
     */
    public void executeScript(String instruction)
    {

        if (instruction.startsWith("show")) {
            if (instruction.contains("@"))
                instruction = replaceVariables(instruction);
            textOutput.setText(textOutput.getText() + "\n" + instruction.substring(4));
        } else if (instruction.startsWith("qcm"))
            switch (instruction) {
                case "qcm":
                    QCM1.setDisable(false);
                    QCM1.setVisible(true);
                    next.setDisable(true);
                    skip.setDisable(true);
                    break;
            }
        else if (instruction.startsWith("//"))
            executeScript(script.next());
        else if (instruction.startsWith("fight"))
            game.fightRival();
        else if (instruction.contains("END"))
            game.getGameView().setScene("MainScreen");
    }

    /**
     * Changes viraible by values
     *
     * @param line
     * @return
     */
    private String replaceVariables(String line)
    {
        if (line.contains("@playerName"))
            line = line.replace("@playerName", game.getPlayer().getName());
        if (line.contains("@rivalName"))
            line = line.replace("@rivalName", game.getRivalName());
        if (line.contains("@playerStarterName"))
            line = line.replace("@playerStarterName", game.getPlayer().getStarter());
        if (line.contains("@rivalStarter"))
            line = line.replace("@rivalStarter",
                                game.getPlayer().getStarter().equals("Bulbizarre") ? "Salamèche"
                                : game.getPlayer().getStarter().equals("Carapuce") ? "Bulbizarre"
                                  : "Carapuce");
        return line;
    }
}
