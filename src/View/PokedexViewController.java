/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import FileIO.ReaderException;
import GameEngine.Game;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author tpedrero
 */
public class PokedexViewController implements Initializable {

    @FXML
    private Button retour;
    @FXML
    private Button previous;
    @FXML
    private Button next;
    @FXML
    private Label page;
    @FXML
    private TextArea displayInfo;
    @FXML
    private VBox pokeBox;

    Game game = Game.getGame();
    private int numPage = 0;
    private final int MAXPAGE = 18;
    private final int MINPAGE = 0;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nextPage();
        previousPage();
        refreshPage();
    }

    private String toString(int i) {
        return "" + i;
    }

    public void nextPage() {
        next.setOnMouseClicked(event -> {
            if (numPage >= MAXPAGE) {
                numPage = MAXPAGE;
            } else {
                numPage++;
            }
            refreshPage();
        });
    }

    public void previousPage() {
        previous.setOnMouseClicked(event -> {
            if (numPage <= MINPAGE) {
                numPage = MINPAGE;
            } else {
                numPage--;
            }
            refreshPage();
        });
    }

    public void refreshPage() {
        page.setText((numPage + 1) + "/19");
        pokeBox.getChildren().clear();
        for (int i = 0; i < 8; i++) {
            Button pokemon = new Button();
            pokeBox.getChildren().add(pokemon);
            pokemon.setPrefWidth(300);
            String PkmnName = Pokemons.PkmnStats.getPOKEDEX().get((8 * numPage) + i);
            pokemon.setText(PkmnName);
            pokemon.setOnAction(e -> {
                
                displayInfo.setText("Nom : " + PkmnName);
            });

        }

    }

    public void goToMainView() {
        game.getGameView().setScene("MainScreen");
    }

}
