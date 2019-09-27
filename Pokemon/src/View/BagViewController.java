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
import Item.Item;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author tpedrero
 */
public class BagViewController implements Initializable {

    @FXML
    private Button goBack, previous, next;
    @FXML
    private GridPane chooseItemGrid;

    Game game = Game.getGame();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setButtons();
    }

    /**
     * Creates the buttons for each item in the players inventory
     */
    public void setButtons() {
        ArrayList<Item> Items = new ArrayList<>();
        int i = 0;
        int j = 0;
        game.getPlayer().getBag().getItems().forEach((item) -> {

            Items.add(item);
        });

        for (Item item : Items) {
            Button button = new Button();
            chooseItemGrid.add(button, i, j);
            button.setPrefWidth(200);
            button.setText(item.getName());
            System.out.println(item.getName());

            if (i <= 5) {
                j++;
            }
            if (j >= 10) {
                i++;
                j = 0;
            }
        }
    }

    /**
     * goes back to main screen
     */
    public void goToMainScreen() {
        game.getGameView().setScene("MainScreen");
    }

}
