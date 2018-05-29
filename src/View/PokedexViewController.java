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
    private TextArea name;
    @FXML
    private TextArea size;
    @FXML
    private TextArea weight;
    @FXML
    private TextArea description;
    @FXML
    private TextArea type;
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

    /**
     * Convert Integers into strings
     *
     * @param i
     * @return
     */
    private String toString(int i) {
        return "" + i;
    }

    /**
     * displays on the screen the next page of the pokedex and increase numPage
     */
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

    /**
     * displays on the screen the previous page of the pokedex and decrease
     * numPage
     */
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

    /**
     * Sets all th buttons for all the pokemons of a page
     */
    public void refreshPage() {
        refreshPageNum();
        // clear the buttons
        pokeBox.getChildren().clear();
        for (int i = 0; i < 8; i++) {
            // init new Button
            Button pokemon = new Button();
            try {
                pokeBox.getChildren().add(pokemon);
                pokemon.setPrefWidth(300);
                String PkmnName = Pokemons.PkmnStats.getPOKEDEX().get((8 * numPage) + i);
                pokemon.setText(PkmnName);
                pokemon.setOnAction(e -> {
                    try {
                        String[] datas = FileIO.DataReader.readFileArray("pokemonStats", PkmnName);
                        System.out.println(datas[0]);
                        name.setText(datas[0]);
                        type.setText(datas[1]);
                        description.setText(datas[12]);
                        size.setText(datas[13] + " m");
                        weight.setText(datas[14] + " kg");
                    } catch (ReaderException ex) {
                        Logger.getLogger(PokedexViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

            } catch (Exception e) {
                
            }

        }
    }

    /**
     * Actualise the page number
     */
    public void refreshPageNum() {
        page.setText((numPage + 1) + "/19");
    }

    /**
     * Sets the main scene
     */
    public void goToMainView() {
        game.getGameView().setScene("MainScreen");
    }

}
