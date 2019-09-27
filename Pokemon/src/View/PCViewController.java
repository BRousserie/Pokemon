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
import Pokemons.CapturedPkmn;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author tpedrero
 */
public class PCViewController implements Initializable
{

    @FXML
    private Button goBack, Pkmn1, Pkmn2, Pkmn3, Pkmn4, Pkmn5, Pkmn6, exchange;

    @FXML
    private GridPane PCPkmnGrid;
    @FXML
    private VBox currentPkmnGrid;

    Game game = Game.getGame();
    boolean isSelected1 = false;
    boolean isSelected2 = false;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        setButtonSize();
        addCurrentPokemonsGrid();
        getCurrent(Pkmn1, Pkmn2);
        getCurrent(Pkmn2, Pkmn1);

    }

    /**
     *
     */
    public void exchange()
    {
        if (isSelected1 && isSelected2)
            System.out.println("exchange");
        else
            System.out.println("Impossible");
    }

    /**
     *
     */
    public void setButtonSize()
    {
        Pkmn1.setPrefWidth(420);
        Pkmn2.setPrefWidth(420);
        Pkmn3.setPrefWidth(420);
        Pkmn4.setPrefWidth(420);
        Pkmn5.setPrefWidth(420);
        Pkmn6.setPrefWidth(420);
    }

    /**
     *
     */
    public void addCurrentPokemonsGrid()
    {

        try {
            Pkmn1.setText(game.getPlayer().getCapturedCurrentPkmns().get(0).toString());
            Pkmn2.setText(game.getPlayer().getCapturedCurrentPkmns().get(1).toString());
            Pkmn3.setText(game.getPlayer().getCapturedCurrentPkmns().get(2).toString());
            Pkmn4.setText(game.getPlayer().getCapturedCurrentPkmns().get(3).toString());
            Pkmn5.setText(game.getPlayer().getCapturedCurrentPkmns().get(4).toString());
            Pkmn6.setText(game.getPlayer().getCapturedCurrentPkmns().get(5).toString());
        } catch (Exception e) {

        }
    }

    /**
     *
     */
    public void addPCPkmnsGrid()
    {
        ArrayList<CapturedPkmn> pkmns = new ArrayList<>();
        game.getPlayer().getMyPokemons().forEach(pkmn -> {
            pkmns.add(pkmn);
        });
    }

    /**
     *
     * @param b
     * @return
     */
    public Button isSelected1(Button b)
    {
        if (isSelected1 == true)
            b.setStyle("-fx-background-color : #98FB98;");
        return b;
    }

    /**
     *
     * @param b1
     * @param b2
     */
    public void newSelection1(Button b1, Button b2)
    {
        if (isSelected1(b1) == isSelected1(b2)) {
            isSelected1 = false;
            isSelected1(b1);
            System.out.println("GOOD");
        } else {
            System.out.println("OK");
            isSelected2 = true;
            b2.setStyle("-fx-background-color : linear-gradient(#ffb700, #cc9c00);");

        }
    }

    /**
     *
     */
    public void refreshSelect()
    {
        isSelected1 = false;
        isSelected2 = false;
    }

    /**
     *
     * @param b1
     * @param b2
     * @return
     */
    public Button getCurrent(Button b1, Button b2)
    {
        b1.setOnMouseClicked(event -> {
            if (isSelected1)
                newSelection1(b1, b2);
            else {
                isSelected1 = true;

                isSelected1(b1);
            }
        });
        return b1;
    }

    /**
     *
     */
    public void goToMainScreen()
    {
        game.getGameView().setScene("MainScreen");
    }

}
