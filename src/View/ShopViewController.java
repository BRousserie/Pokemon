/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

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
public class ShopViewController implements Initializable {

    // <editor-fold defaultstate="collapsed" desc="FXML elements">
    @FXML
    private Button goBack;
    @FXML
    private Button buy;
    @FXML
    private ChoiceBox itemChoice;
    @FXML
    private TextArea displayInfo;
    @FXML
    private TextArea displayItems;
    @FXML
    private TextArea displayPrice;
    @FXML
    private TextArea itemQuantity;
    @FXML
    private TextArea totalPrice;
    @FXML
    private TextArea money;
    // </editor-fold>

    Game game = Game.getGame();

    /**
     * Convert Integers into Strings
     *
     * @param number
     * @return
     */
    public String toString(int number) {
        return "" + number;
    }

    /**
     * Initialize the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    public void display() {
        displayInfo.setText("Bonjour, bienvenue dans notre magasin. \n Que souhaitez-vous acheter ? \n");
        displayMoney();
        displayItems();
        displayPrice();
    }

    /**
     * Displays the amount of money of the player.
     */
    public void displayMoney() {
        String playerMoney = "Vous avez : ";
        money.setText(playerMoney + toString(getMoney()) + "  $");
    }

    /**
     * Displays the items of the shop.
     */
    public void displayItems() {
        displayItems.setText("Voici nos produits : \n");
        game.getCurrentZone().getShop().forEach(item -> {
            displayItems.setText(displayItems.getText() + "\n" + item);
        });
    }

    /**
     * Displays the price of the items
     */
    public void displayPrice() {
        displayPrice.setText("Prix: \n");
        game.getCurrentZone().getShop().forEach(item -> {
            String price = String.valueOf(game.getDatas().getItem(item).getBuyPrice());
            displayPrice.setText(displayPrice.getText() + "\n" + price + "  $");
        });
    }

    /**
     * Sets the items in the list
     */
    public void itemList() {
        itemChoice.setItems(FXCollections.observableArrayList(game.getCurrentZone().getShop()));
    }

    /**
     *
     * @return the amount of momey of the player.
     */
    public int getMoney() {
        return game.getPlayer().getBag().getPokeDollars();
    }

    /**
     * Give the price of one selected item.
     *
     * @return the price of one item selected, 0 if no item selected
     */
    public int getItemPrice() {
        Object i = itemChoice.getValue();
        if (i == null) {
            return 0;
        } else {
            String item = itemChoice.getValue().toString();
            return Integer.parseInt(String.valueOf(game.getDatas().getItem(item).getBuyPrice()));
        }
    }

    /**
     * Returns the number of items the player wants to buy.
     *
     * @return IQuantity
     * @throws Exception
     */
    public int getItemQtt() throws Exception {
        String SQuantity = itemQuantity.getText();
        if ("".equals(SQuantity)){
            displayInfo.setText(displayInfo.getText() + "Entrez une valeur. \n");
            return 0;
        }
        int IQuantity = Integer.parseInt(SQuantity);
        if (IQuantity < 0) {
            displayInfo.setText(displayInfo.getText() + "Vous ne pouvez pas achetter une quantité négative. \n");
            return 0;
        }
        else {
            return IQuantity;
        }
    }

    /**
     * Returns the total price of the items selected.
     *
     * @return total
     * @throws Exception
     */
    public int getTotalPrice() throws Exception {
        int total;
        total = getItemPrice() * getItemQtt();
        totalPrice.setText(toString(total));
        return total;
    }

    /**
     * Actualises the informations
     *
     * @throws Exception
     */
    public void refresh() throws Exception {
        itemQuantity.setOnKeyReleased(event -> {
            try {
                getTotalPrice();
            } catch (Exception ex) {
            }
        });
        itemChoice.setOnAction(event -> {
            try {
                getTotalPrice();
            } catch (Exception ex) {
                Logger.getLogger(ShopViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    /**
     * Gives the items to the player and take his money.
     *
     * @throws Exception
     */
    public void buy() throws Exception {
        // Variables
        String player = game.getPlayer().getName();
        String name = itemChoice.getValue().toString();
        String description = game.getDatas().getItem(name).getDescription();
        String buyPrice = toString(getItemPrice());
        Item item = new Item(name, description, buyPrice) {
        };
        // Exchange System
        if (getTotalPrice() <= getMoney()) {
            game.getPlayer().getBag().exchangeMoney(-getTotalPrice());
            displayMoney();
            for (int i = 0; i < getItemQtt(); i++) {
                game.getPlayer().getBag().addItem(item);
                System.out.println(game.getPlayer().getBag());
            }
            displayInfo.setText(displayInfo.getText() + "Tenez! Merci infiniment. \n" 
            + player + " obtient : " + name + " x " + getItemQtt() + "\n");
            displayInfo.setScrollLeft(displayInfo.getScrollLeft()+1);
            
        } else {
            displayInfo.setText(displayInfo.getText() + "Oups vous n'avez pas assez d'argent. \n");
        }
    }

    /**
     *
     */
    public void goToMainView() {
        game.getGameView().setScene("MainScreen");
    }
}
