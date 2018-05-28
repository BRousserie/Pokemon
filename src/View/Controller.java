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

import FileIO.DataWriter;
import FileIO.ReaderException;
import GameEngine.Game;
import Map.Town;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author tpedrero
 */
public class Controller implements Initializable {

    // <editor-fold defaultstate="collapsed" desc="FXML elements">
    @FXML
    private AnchorPane root;
    @FXML
    private TextArea displayScenario;
    @FXML
    private VBox displayTeam;
    @FXML
    private Button PC;
    @FXML
    private Button centrePokemon;
    @FXML
    private Button shop;
    @FXML
    private Button arena;
    @FXML
    private Button pokedex;
    @FXML
    private Button pokemon;
    @FXML
    private Button sac;
    @FXML
    private Button sauvegarder;
    @FXML
    private Button searchItems;
    @FXML
    private Button fish;
    @FXML
    private ChoiceBox selectDestination;
    @FXML
    private Button goTo;
    @FXML
    private Label playerName;
    @FXML
    private Label pokemon1;
    @FXML
    private Label pokemon2;
    @FXML
    private Label pokemon3;
    @FXML
    private Label pokemon4;
    @FXML
    private Label pokemon5;
    @FXML
    private Label pokemon6;
    @FXML
    private Label badge1;
    @FXML
    private Label badge2;
    @FXML
    private Label badge3;
    @FXML
    private Label badge4;
    @FXML
    private Label badge5;
    @FXML
    private Label badge6;
    @FXML
    private Label badge7;
    @FXML
    private Label badge8;
    // </editor-fold>

    Game game = Game.getGame();

    // <editor-fold defaultstate="collapsed" desc="Initialization methods">
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        displayZone();
        displayZoneDesc();
        if (game.getCurrentZone().getZoneType().equals("villes")) {
            initializeTownButtons();
        } else {
            initializeWildZoneButtons();
        }
        initializePlayerInfos();
        initializePermanentButtons();
    }

    private void initializeTownButtons() {
        if (game.getCurrentZone().hasPokeCenter()) {
            PC.setDisable(false);
            centrePokemon.setDisable(false);
            sac.setOnMouseClicked(e -> displayScenario.setText("SALUT" + showBag()));
            PC.setOnMouseClicked(e -> game.getGameView().setScene("PCView"));
            centrePokemon.setOnMouseClicked(e -> {
                game.getPlayer().healPokemon();
                displayScenario.setText("Vos POKéMON ont été soignés !");
                setTeam();
            });
        } else {
            PC.setDisable(true);
            centrePokemon.setDisable(true);
        }
        if (!game.getCurrentZone().getShop().isEmpty()) {
            shop.setOnMouseClicked(e -> game.getGameView().setScene("ShopView"));
            shop.setDisable(false);
        } else {
            shop.setDisable(true);
        }
        if (game.getCurrentZone().hasArena()) {
            arena.setOnMouseClicked(e -> ((Town) game.getCurrentZone()).fightArena());
            arena.setDisable(false);
        } else {
            arena.setDisable(true);
        }
    }

    private void initializeWildZoneButtons() {
        PC.setText("Chercher des POKéMON");
        PC.setStyle("-fx-font-size: 14;");
        PC.setOnMouseClicked(e -> game.getCurrentZone().searchWildPokemon());
        PC.setDisable(false);
        hide(centrePokemon);
        hide(shop);
        hide(arena);
    }

    /**
     * Displays the informations about the player.
     */
    private void initializePlayerInfos() {
        playerName.setText(game.getPlayer().getName());
        setTeam();
        setBadges();
    }

    private void setTeam() {
        game.getGameView().setPkmnLabel(pokemon1, 0);
        game.getGameView().setPkmnLabel(pokemon2, 1);
        game.getGameView().setPkmnLabel(pokemon3, 2);
        game.getGameView().setPkmnLabel(pokemon4, 3);
        game.getGameView().setPkmnLabel(pokemon5, 4);
        game.getGameView().setPkmnLabel(pokemon6, 5);
    }

    private void setBadges() {
        badge1.setText((game.getPlayer().getBag().contains("Badge Roche")) ? "Badge Roche" : "Aucun");
        badge2.setText((game.getPlayer().getBag().contains("Badge Cascade")) ? "Badge Cascade" : "");
        badge3.setText((game.getPlayer().getBag().contains("Badge Foudre")) ? "Badge Foudre" : "");
        badge4.setText((game.getPlayer().getBag().contains("Badge Prisme")) ? "Badge Prisme" : "");
        badge5.setText((game.getPlayer().getBag().contains("Badge Âme")) ? "Badge Âme" : "");
        badge6.setText((game.getPlayer().getBag().contains("Badge Marais")) ? "Badge Marais" : "");
        badge7.setText((game.getPlayer().getBag().contains("Badge Volcan")) ? "Badge Volcan" : "");
        badge8.setText((game.getPlayer().getBag().contains("Badge Terre")) ? "Badge Terre" : "");
    }

    /**
     * Displays the zone where the player is.
     */
    public void displayZone(){
      displayScenario.setText(displayScenario.getText() + "Vous vous trouvez à " + game.getCurrentZone().getName() + "\n");
    }
    
    /**
     * Displays the decription of the zone.
     */
    public void displayZoneDesc(){
      displayScenario.setText(displayScenario.getText() + game.getCurrentZone().getDescription() + "\n \n");
    }
    
    private void initializePermanentButtons() {
        pokedex.setOnMouseClicked(e -> game.getGameView().setScene("PokedexView"));
        
        pokemon.setOnMouseClicked(e -> showTeam());
        
        sac.setOnMouseClicked(e -> showBag());
        
        sauvegarder.setOnMouseClicked(e -> DataWriter.saveGame(game));
        
        if (game.getPlayer().getCondition("Canne")) {
            fish.setOnMouseClicked(e -> game.getCurrentZone().searchWildFish());
        }
        searchItems.setOnMouseClicked(e -> displayScenario.setText(
                game.getCurrentZone().searchItems()));
        
        selectDestination.setItems(FXCollections
                .observableList(game.getCurrentZone().getAccessibleZones()));
        
        goTo.disableProperty().bind(selectDestination.valueProperty().isNull());
        goTo.setOnMouseClicked(e -> 
            {
            try {
                game.setCurrentZone(
                        game.getDatas().getLoadedZone(
                                selectDestination.getValue().toString()));
            } catch (ReaderException ex) {
                ex.printStackTrace();
            }
        });

    }
    // </editor-fold>
    
    private void hide(Node n) {
        n.setVisible(false);
        n.setDisable(true);
    }
    private void show(Node n) {
        n.setVisible(true);
        n.setDisable(false);
    }

    private void showTeam() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String showBag() {
        return game.getPlayer().getBag().getItems().toString();
    }
}
