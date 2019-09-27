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

import FileIO.DataReader;
import FileIO.DataWriter;
import FileIO.ReaderException;
import GameEngine.Game;
import Map.Arena;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * FXML Controller class
 *
 * @author tpedrero
 */
public class Controller implements Initializable
{

    // <editor-fold defaultstate="collapsed" desc="FXML elements">
    @FXML
    private AnchorPane root;
    @FXML
    private TextArea displayScenario;
    @FXML
    private VBox displayTeam;
    @FXML
    private ChoiceBox selectDestination;
    @FXML
    private Group kanto;
    @FXML
    private Button PC, centrePokemon, shop, arena, pokedex, pokemon, sac,
            sauvegarder, searchItems, fish, goTo;
    @FXML
    private Label playerName,
            pokemon1, pokemon2, pokemon3, pokemon4, pokemon5, pokemon6,
            badge1, badge2, badge3, badge4, badge5, badge6, badge7, badge8;

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
    public void initialize(URL url, ResourceBundle rb)
    {
        displayZone();
        if (game.getCurrentZone().getZoneType().equals("villes"))
            initializeTownButtons();
        else
            initializeWildZoneButtons();
        initializePlayerInfos();
        initializePermanentButtons();
        if (!game.getPlayer().getCondition("Carte"))
            hide(kanto);
        if (!game.getPlayer().getCondition("Pokédex"))
            hide(pokedex);
        fish.setDisable(true);
    }

    /**
     * Initialises the buttons able in towns
     */
    private void initializeTownButtons()
    {
        if (!game.getCurrentZone().hasPokeCenter()) {
            hide(PC);
            hide(centrePokemon);
        } else {
            PC.setOnMouseClicked(e -> game.getGameView().setScene("PCView"));
            centrePokemon.setOnMouseClicked(e -> {
                game.getPlayer().healPokemon();
                displayScenario.setText(displayScenario.getText() + " \n \nVos POKéMON ont été soignés !");
                setTeam();
                refreshSaveButton();
                centrePokemon.setDisable(true);
            });
        }

        if (!game.getCurrentZone().getShop().isEmpty())
            shop.setOnMouseClicked(e -> game.getGameView().setScene("ShopView"));
        else
            hide(shop);
        if (game.getCurrentZone().hasArena())
            arena.setOnMouseClicked(e -> {
                try {
                    Arena arena = Game.getGame().getDatas().getLoadedArena();
                    if (arena.isNextArena())
                        arena.fight();
                    else {
                        displayScenario.setText(displayScenario.getText() + " \n \nVous ne pouvez pas encore "
                                                + "combattre dans cette Arène. Vous avez besoin du "
                                                + arena.getPrerequisites());
                        this.arena.setDisable(true);
                    }
                } catch (ReaderException ex) {
                    ex.printStackTrace();
                }
            });
        else
            hide(arena);
    }

    /**
     * Initialises the buttons able in wild zones
     */
    private void initializeWildZoneButtons()
    {
        PC.setText("Chercher des POKéMON");
        PC.setStyle("-fx-font-size: 14;");
        PC.setOnMouseClicked(e -> game.getCurrentZone().searchWildPokemon());
        PC.setPrefHeight(100);
        PC.setWrapText(true);
        PC.setTextAlignment(TextAlignment.CENTER);
        hide(centrePokemon);
        hide(shop);
        hide(arena);
    }

    /**
     * Displays the informations about the player.
     */
    private void initializePlayerInfos()
    {
        playerName.setText(game.getPlayer().getName());
        setTeam();
        setBadges();
    }

    /**
     * Displays the informations about the player team.
     */
    private void setTeam()
    {
        game.getGameView().setPkmnLabel(pokemon1, 0);
        game.getGameView().setPkmnLabel(pokemon2, 1);
        game.getGameView().setPkmnLabel(pokemon3, 2);
        game.getGameView().setPkmnLabel(pokemon4, 3);
        game.getGameView().setPkmnLabel(pokemon5, 4);
        game.getGameView().setPkmnLabel(pokemon6, 5);
    }

    /**
     * Displays the informations about the player badges.
     */
    private void setBadges()
    {
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
    public void displayZone()
    {
        displayScenario.setText("Vous vous trouvez à "
                                + game.getCurrentZone().getName() + "\n"
                                + game.getCurrentZone().getDescription());
    }

    /**
     * Initialises permanent button on MainView
     */
    private void initializePermanentButtons()
    {
        pokedex.setOnMouseClicked(e -> game.getGameView().setScene("PokedexView"));

        //pokemon.setOnMouseClicked(e -> );
        sac.setOnMouseClicked(e -> game.getGameView().setScene("BagView"));

        refreshSaveButton();
        sauvegarder.setOnMouseClicked(e -> {
            DataWriter.saveGame(game);
            refreshSaveButton();
        });

        if (game.getPlayer().getCondition("Canne"))
            fish.setOnMouseClicked(e -> game.getCurrentZone().searchWildFish());
        searchItems.setOnMouseClicked(e -> {
            try {
                if (!game.possiblyLaunchFight()) {
                    displayScenario.setText(
                            displayScenario.getText() + "\n"
                            + game.getCurrentZone().searchItems());
                    refreshSaveButton();
                }
            } catch (ReaderException ex) {
                System.out.println("Tried to launch fight but failed");
                displayScenario.setText(
                        displayScenario.getText() + "\n"
                        + game.getCurrentZone().searchItems());
                refreshSaveButton();
            }
        });

        selectDestination.setItems(FXCollections
                .observableList(game.getCurrentZone().getAccessibleZones()));

        goTo.disableProperty().bind(selectDestination.valueProperty().isNull());
        goTo.setOnMouseClicked(e -> {
            try {
                game.goToZone(
                        game.getDatas().getLoadedZone(
                                selectDestination.getValue().toString()));
            } catch (ReaderException ex) {
                ex.printStackTrace();
            }
        });

    }
    // </editor-fold>

    /**
     * Refreshes the button for save
     */
    private void refreshSaveButton()
    {
        if (game.getCurrentStoryEvent() > 0
            && !DataReader.loadGame().toString().equals(game.save().toString()))
            sauvegarder.setDisable(false);
        else
            sauvegarder.setDisable(true);
    }

    /**
     * Set property hidden
     *
     * @param n
     */
    private void hide(Node n)
    {
        n.setVisible(false);
        n.setDisable(true);
    }

    /**
     * Set property showed
     *
     * @param n
     */
    private void show(Node n)
    {
        n.setVisible(true);
        n.setDisable(false);
    }
}
