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

import Character.Inventory;
import GameEngine.Game;
import Item.Item;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

/**
 *
 * @author tony
 */
public class ShopViewController implements Initializable
{

    // <editor-fold defaultstate="collapsed" desc="FXML elements">
    @FXML
    private Button goBack, buy;
    @FXML
    private ChoiceBox itemChoice;
    @FXML
    private TextArea displayInfo, displayItems, displayPrice, itemQuantity,
            totalPrice, money;
    // </editor-fold>

    Game game = Game.getGame();
    Inventory myBag = game.getPlayer().getBag();
    Item selectedItem = null;

    /**
     * Initialize the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        display();
        itemList();
        try {
            refresh();
            getTotalPrice();
        } catch (Exception ex) {
            Logger.getLogger(ShopViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Displays the informations.
     */
    public void display()
    {
        displayInfo.setText("Bonjour, bienvenue dans notre magasin. \n Que souhaitez-vous acheter ? \n");
        displayMoney();
        displayItems();
        displayPrice();
    }

    /**
     * Displays the amount of money of the player.
     */
    public void displayMoney()
    {
        money.setText("Vous avez : " + myBag.getPokeDollars() + "$");
    }

    /**
     * Displays the items of the shop.
     */
    public void displayItems()
    {
        displayItems.setText("Voici nos produits : \n");
        game.getCurrentZone().getShop().forEach(item -> {
            displayItems.setText(displayItems.getText() + "\n" + item);
        });
    }

    /**
     * Displays the price of the items
     */
    public void displayPrice()
    {
        displayPrice.setText("Prix: \n");
        game.getCurrentZone().getShop().forEach(item -> {
            String price = String.valueOf(game.getDatas().getItem(item).getBuyPrice());
            displayPrice.setText(displayPrice.getText() + "\n" + price + "  $");
        });
    }

    /**
     * Sets the items in the list
     */
    public void itemList()
    {
        itemChoice.setItems(FXCollections.observableArrayList(game.getCurrentZone().getShop()));
    }

    /**
     * Returns the number of items the player wants to buy.
     *
     * @return IQuantity
     * @throws Exception
     */
    public int getItemQtt() throws Exception
    {
        String SQuantity = itemQuantity.getText();
        if ("".equals(SQuantity)) {
            displayInfo.setText(displayInfo.getText() + "Entrez une valeur.\n");
            return 0;
        }
        try {
            int IQuantity = Integer.parseInt(SQuantity);
            if (IQuantity < 0) {
                displayInfo.setText(displayInfo.getText()
                                    + "Vous ne pouvez pas achetter une quantité négative.\n");
                return 0;
            } else
                return IQuantity;
        } catch (NumberFormatException e) {
            displayInfo.setText(displayInfo.getText() + "Veuillez entrer un nombre entre 0 et 99");
            return 0;
        }
    }

    /**
     * Returns the total price of the items selected.
     *
     * @return total
     * @throws Exception
     */
    public int getTotalPrice() throws Exception
    {
        int total;
        total = (selectedItem != null)
                ? selectedItem.getBuyPrice() * getItemQtt()
                : 0;
        totalPrice.setText(total + "");
        return total;
    }

    /**
     * Actualises the informations
     *
     * @throws Exception
     */
    public void refresh() throws Exception
    {
        itemQuantity.setOnKeyReleased(event -> {
            try {
                if (Integer.parseInt(itemQuantity.getText())
                    + (myBag.contains(selectedItem.getName())
                       ? myBag.regroup().get(selectedItem) : 0)
                    > 99)
                    itemQuantity.setText("" + (99 - myBag.regroup().get(selectedItem)));
                getTotalPrice();
            } catch (Exception ex) {
            }
        });
        itemChoice.valueProperty().addListener(c -> {
            try {
                if (itemChoice.getValue() != null)
                    selectedItem = game.getDatas()
                            .getItem(itemChoice.getValue().toString());
                if (Integer.parseInt(itemQuantity.getText())
                    + (myBag.contains(selectedItem.getName())
                       ? myBag.regroup().get(selectedItem) : 0)
                    > 99)
                    itemQuantity.setText("" + (99 - myBag.regroup().get(selectedItem)));
                getTotalPrice();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * Gives the items to the player and take his money.
     *
     * @throws Exception
     */
    public void buy() throws Exception
    {
        if (selectedItem != null && getItemQtt() > 0) {
            // Variables
            String player = game.getPlayer().getName();
            String description = selectedItem.getDescription();
            int buyPrice = selectedItem.getBuyPrice();
            // Exchange System
            if (getTotalPrice() <= myBag.getPokeDollars()) {
                myBag.exchangeMoney(-getTotalPrice());
                displayMoney();
                for (int i = 0; i < getItemQtt(); i++)
                    myBag.addItem(selectedItem);
                displayInfo.setText(displayInfo.getText() + "Tenez! Merci infiniment. \n"
                                    + player + " obtient : " + selectedItem.getName() + " x " + getItemQtt() + "\n");
                displayInfo.setScrollLeft(displayInfo.getScrollLeft() + 1);
                itemChoice.setValue(null);
                selectedItem = null;
            } else
                displayInfo.setText(displayInfo.getText() + "Oups vous n'avez pas assez d'argent. \n");
        }
    }

    /**
     *
     */
    public void goToMainView()
    {
        game.getGameView().setScene("MainScreen");
    }
}
