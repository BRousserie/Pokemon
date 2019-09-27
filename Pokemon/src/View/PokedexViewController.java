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

import FileIO.ReaderException;
import GameEngine.Game;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author tpedrero
 */
public class PokedexViewController implements Initializable
{

    @FXML
    private Button goBack, previous, next;
    @FXML
    private Label page;
    @FXML
    private TextArea name, size, weight, description, type;
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
    public void initialize(URL url, ResourceBundle rb)
    {
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
    private String toString(int i)
    {
        return "" + i;
    }

    /**
     * displays on the screen the next page of the pokedex and increase numPage
     */
    public void nextPage()
    {
        next.setOnMouseClicked(event -> {
            if (numPage >= MAXPAGE)
                numPage = MAXPAGE;
            else
                numPage++;
            refreshPage();
        });
    }

    /**
     * displays on the screen the previous page of the pokedex and decrease
     * numPage
     */
    public void previousPage()
    {
        previous.setOnMouseClicked(event -> {
            if (numPage <= MINPAGE)
                numPage = MINPAGE;
            else
                numPage--;
            refreshPage();
        });
    }

    /**
     * Sets all th buttons for all the pokemons of a page
     */
    public void refreshPage()
    {
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
    public void refreshPageNum()
    {
        page.setText((numPage + 1) + "/19");
    }

    /**
     * Sets the Main Screen
     */
    public void goToMainScreen()
    {
        game.getGameView().setScene("MainScreen");
    }
}
