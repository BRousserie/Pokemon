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

import Fight.Attack;
import Fight.FightingPkmn;
import FileIO.ReaderException;
import GameEngine.Fight;
import GameEngine.Game;
import Item.Ball;
import Item.Item;
import Item.Potion;
import Pokemons.CapturedPkmn;
import Pokemons.PkmnType;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.reactfx.util.FxTimer;

/**
 * FXML Controller class
 *
 * @author tpedrero
 */
public class FightViewController implements Initializable
{

    // <editor-fold defaultstate="collapsed" desc="FXML elements">
    @FXML
    private GridPane chooseTypeGrid, chooseAtkGrid, choosePkmnGrid,
            itemsContainer, attackNotifierBack;
    @FXML
    private Label myPkmnName, enmyPkmnName, myPkmnLvl,
            enmyPkmnLvl, myPkmnHealth, playerName,
            pokemon1, pokemon2, pokemon3, pokemon4, pokemon5, pokemon6, message;

    /**
     *
     */
    @FXML
    public ProgressBar myPkmnHP,
            /**
             *
             */
            enmyPkmnHP,
            /**
             *
             */
            myPkmnXP;
    @FXML
    private Circle myHead, enmyHead;
    @FXML
    private Arc myCorpse, enmyCorpse;
    @FXML
    private Button cancelTypeChoice, selectAttack, selectPokemon, selectItem,
            fleeFromFight, atk1, atk2, atk3, atk4,
            pkmn1, pkmn2, pkmn3, pkmn4, pkmn5, pkmn6;

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Variables">
    private final Game game = Game.getGame();
    private final Fight fight = game.getFight();
    private final Timeline messageAppear = new Timeline();
    private final Timeline healthBarUpdates = new Timeline();
    private final Timeline messageDisappear = new Timeline();
    private int row = 0;
    private int col = 0;

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Initialization methods">
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        playerName.setText(game.getPlayer().getName());
        game.getGameView().setPkmnLabel(pokemon1, 0);
        game.getGameView().setPkmnLabel(pokemon2, 1);
        game.getGameView().setPkmnLabel(pokemon3, 2);
        game.getGameView().setPkmnLabel(pokemon4, 3);
        game.getGameView().setPkmnLabel(pokemon5, 4);
        game.getGameView().setPkmnLabel(pokemon6, 5);
        message.setOpacity(0);
        attackNotifierBack.opacityProperty().bind(message.opacityProperty());
        setMyPkmnInfos();
        setEnmyPkmnInfos();
        initializeButtons();
        initializeTimeLines();
        showMessage(fight.getStartingMessage(), e -> showChooseTypeGrid(), 1);
    }

    private void refresh()
    {
        game.getGameView().setPkmnLabel(pokemon1, 0);
        game.getGameView().setPkmnLabel(pokemon2, 1);
        game.getGameView().setPkmnLabel(pokemon3, 2);
        game.getGameView().setPkmnLabel(pokemon4, 3);
        game.getGameView().setPkmnLabel(pokemon5, 4);
        game.getGameView().setPkmnLabel(pokemon6, 5);
        setMyPkmnInfos();
        setEnmyPkmnInfos();
        initializeButtons();
        initializeTimeLines();
    }

    // <editor-fold defaultstate="collapsed" desc="Buttons Initialization">
    private void initializeButtons()
    {
        selectAttack.setOnMouseClicked(e -> showChooseAtkGrid());
        selectPokemon.setOnMouseClicked(e -> showChoosePkmnGrid());
        selectItem.setOnMouseClicked(e -> showChooseItemGrid());
        fleeFromFight.setOnMouseClicked(e -> flee());
        cancelTypeChoice.setOnMouseClicked(e -> showChooseTypeGrid());
        initializeAtkButtons();
        initializePkmnsButtonsChangePkmn();
    }

    private void initializeAtkButtons()
    {
        setAtkButton(atk1, 0);
        setAtkButton(atk2, 1);
        setAtkButton(atk3, 2);
        setAtkButton(atk4, 3);
    }

    private void initializePkmnsButtonsChangePkmn()
    {
        setPkmnButtonChangePkmn(pkmn1, 0);
        setPkmnButtonChangePkmn(pkmn2, 1);
        setPkmnButtonChangePkmn(pkmn3, 2);
        setPkmnButtonChangePkmn(pkmn4, 3);
        setPkmnButtonChangePkmn(pkmn5, 4);
        setPkmnButtonChangePkmn(pkmn6, 5);
    }

    private void initializePkmnsButtonsUseItem(Potion p)
    {
        setPkmnButtonUseItem(pkmn1, 0, p);
        setPkmnButtonUseItem(pkmn2, 1, p);
        setPkmnButtonUseItem(pkmn3, 2, p);
        setPkmnButtonUseItem(pkmn4, 3, p);
        setPkmnButtonUseItem(pkmn5, 4, p);
        setPkmnButtonUseItem(pkmn6, 5, p);
    }

    private void setAtkButton(Button attackButton, int attackIndex)
    {
        if (fight.getMyPkmn().getAttacks().size() > attackIndex) {
            Attack attack = fight.getMyPkmn().getAttacks().get(attackIndex);
            attackButton.setText(attack.getName() + "\n"
                                 + attack.getPP() + "/" + attack.getPPMAX());
            attackButton.setOnMouseClicked(e -> doAttack(attack));
        } else {
            attackButton.setText("");
            attackButton.setDisable(true);
        }
    }

    private void setPkmnButtonChangePkmn(Button pkmnButton, int pkmnIndex)
    {
        try {
            CapturedPkmn pkmn = game.getPlayer().getCapturedCurrentPkmns().get(pkmnIndex);
            pkmnButton.setText(pkmn.toString());
            pkmnButton.setOnMouseClicked(e -> choosePokemon(pkmn));
        } catch (IndexOutOfBoundsException e) {
            pkmnButton.setDisable(true);
        }
    }

    private void setPkmnButtonUseItem(Button pkmnButton, int pkmnIndex, Potion p)
    {
        try {
            CapturedPkmn pkmn = game.getPlayer().getCapturedCurrentPkmns().get(pkmnIndex);
            pkmnButton.setText(pkmn.toString());
            pkmnButton.setOnMouseClicked(e -> p.use(pkmn, this));
        } catch (IndexOutOfBoundsException e) {
            pkmnButton.setDisable(true);
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Labels Initialization">
    private void setMyPkmnInfos()
    {
        myPkmnName.setText(fight.getMyPkmn().getName());
        myPkmnLvl.setText("Niv. " + fight.getMyPkmn().getLevel());
        setMyPkmnHealth();
        setMyPkmnXP();
        String fillByMyColor = "-fx-fill: " + setShapeColor(fight.getMyPkmn().getType()) + ";";
        myHead.setStyle(fillByMyColor);
        myCorpse.setStyle(fillByMyColor);
    }

    private void setEnmyPkmnInfos()
    {
        enmyPkmnName.setText(fight.getEnnemy().getName());
        enmyPkmnLvl.setText("Niv. " + fight.getEnnemy().getLevel());
        setEnmyPkmnHealth();
        String fillByEnmyColor = "-fx-fill: " + setShapeColor(fight.getEnnemy().getType()) + ";";
        enmyHead.setStyle(fillByEnmyColor);
        enmyCorpse.setStyle(fillByEnmyColor);
    }

    private void setMyPkmnHealth()
    {
        myPkmnHealth.setText(fight.getMyPkmn().getHp()
                             + "/" + fight.getMyPkmn().getMaxHP());
        myPkmnHP.progressProperty().addListener(c
                -> myPkmnHP.setStyle(setBarColor(myPkmnHP.getProgress())));
        myPkmnHP.setProgress(getPkmnHealthPercentage(fight.getMyPkmn()));
    }

    private void setEnmyPkmnHealth()
    {
        enmyPkmnHP.progressProperty().addListener(c
                -> enmyPkmnHP.setStyle(setBarColor(enmyPkmnHP.getProgress())));
        enmyPkmnHP.setProgress(getPkmnHealthPercentage(fight.getEnnemy()));
    }

    private void setMyPkmnXP()
    {
        myPkmnXP.setProgress((float) fight.getMyPkmn().getExp()
                             / (float) fight.getMyPkmn().getLevelUpXP());
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Animations Initialization">
    private void initializeTimeLines()
    {
        KeyValue showMessage = new KeyValue(
                message.opacityProperty(), 1, Interpolator.EASE_OUT);
        KeyFrame showAttackmessage = new KeyFrame(Duration.seconds(1), showMessage);
        messageAppear.getKeyFrames().add(showAttackmessage);

        KeyValue hideMessage = new KeyValue(
                message.opacityProperty(), 0, Interpolator.EASE_IN);
        KeyFrame showAttackMessage = new KeyFrame(Duration.millis(1), hideMessage);
        messageDisappear.getKeyFrames().add(showAttackMessage);
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Initialization Methods">
    private double getPkmnHealthPercentage(FightingPkmn pkmn)
    {
        return (double) pkmn.getHp() / (double) pkmn.getMaxHP();
    }

    private String setBarColor(double pkmnHealth)
    {
        return (pkmnHealth > 0.4) ? "-fx-accent: green;"
               : (pkmnHealth > 0.1) ? "-fx-accent: orange;"
                 : "-fx-accent: red;";
    }

    private String setShapeColor(String pkmnType)
    {
        String[] colors = {"#d9d9d9", "#cc0000", "#007acc",
                           "#e6e600", "#00cc00", "#80ffff", "#800000", "#6600cc", "#bb9900",
                           "#99ccff", "#ff33cc", "#ccff66", "#996633", "#333399", "#2200c0"};
        if (!pkmnType.contains("-"))
            return colors[PkmnType.indexOf(PkmnType.getType(pkmnType)) - 1];
        else
            return "linear-gradient(to right, "
                   + setShapeColor(pkmnType
                            .substring(0, pkmnType.indexOf("-")))
                   + " 30%, "
                   + setShapeColor(pkmnType
                            .substring(pkmnType.indexOf("-") + 1))
                   + " 70%)";
    }

    // </editor-fold>
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Managing visibility">
    private void showChooseTypeGrid()
    {
        refresh();
        hide(cancelTypeChoice);
        hide(chooseAtkGrid);
        hide(choosePkmnGrid);
        hide(itemsContainer);
        show(chooseTypeGrid);
        if (fight.getMyPkmn().getHp() == 0) {
            showChoosePkmnGrid();
            hide(cancelTypeChoice);
        }
    }

    private void showChooseAtkGrid()
    {
        show(cancelTypeChoice);
        show(chooseAtkGrid);
        hide(chooseTypeGrid);
    }

    private void showChoosePkmnGrid()
    {
        show(cancelTypeChoice);
        show(choosePkmnGrid);
        hide(chooseTypeGrid);
    }

    private void showChooseItemGrid()
    {
        show(cancelTypeChoice);
        show(itemsContainer);
        hide(chooseTypeGrid);
        itemsContainer.getChildren().clear();
        row = 0;
        col = 0;
        Map<Item, Long> groupedBag = fight.getMe().getBag().regroup();
        groupedBag.keySet().stream()
                .filter(i -> i.getClass().equals(Ball.class)).forEach(i -> {
            addItemsToGrid((Item) i, (int) (long) groupedBag.get(i));
        });
        row = 0;
        col = 1;
        groupedBag.keySet().stream()
                .filter(i -> i.getClass().equals(Potion.class)).forEach(i -> {
            addItemsToGrid((Item) i, (int) (long) groupedBag.get(i));
        });
    }

    /**
     *
     * @param output
     * @param nextStep
     * @param pkmn
     * @param bar
     * @param duration
     */
    public void showUpdateBarMessage(String output, EventHandler nextStep,
                                     FightingPkmn pkmn, ProgressBar bar, double duration)
    {
        hide(chooseAtkGrid);
        hide(chooseTypeGrid);
        hide(choosePkmnGrid);
        hide(itemsContainer);
        hide(cancelTypeChoice);
        message.setText(output);
        KeyValue progress = new KeyValue(bar.progressProperty(),
                                         getPkmnHealthPercentage(pkmn));
        KeyFrame updateBar = new KeyFrame(Duration.seconds(duration), progress);
        healthBarUpdates.getKeyFrames().add(updateBar);

        messageAppear.playFromStart();
        messageAppear.setOnFinished(e -> healthBarUpdates.playFromStart());
        healthBarUpdates.setOnFinished(e -> {
            if (fight.getEnnemy().getHp() == 0)
                showMessage(fight.getEnnemy().getName() + " est K.O. !",
                            whenShown -> lookForWinner(true), 1);
            else if (fight.getMyPkmn().getHp() == 0)
                showMessage(fight.getMyPkmn().getName() + " est K.O. !",
                            whenShown -> lookForWinner(false), 1);
            else {
                messageDisappear.playFromStart();
                healthBarUpdates.getKeyFrames().remove(updateBar);
            }
        });
        messageDisappear.setOnFinished(nextStep);
    }

    /**
     *
     * @param output
     * @param nextStep
     * @param delay
     */
    public void showMessage(String output, EventHandler nextStep, int delay)
    {
        hide(chooseTypeGrid);
        hide(chooseAtkGrid);
        hide(choosePkmnGrid);
        hide(itemsContainer);
        hide(cancelTypeChoice);

        message.setText(output);
        messageAppear.playFromStart();
        messageAppear.setOnFinished(e
                -> FxTimer.runLater(java.time.Duration.ofSeconds(delay),
                                    () -> messageDisappear.playFromStart()));
        messageDisappear.setOnFinished(nextStep);
    }

//    private void showMessage(String output, EventHandler nextStep) {
//        hide(chooseTypeGrid);
//        hide(chooseAtkGrid);
//        hide(choosePkmnGrid);
//        hide(itemsContainer);
//        hide(cancelTypeChoice);
//
//        message.setText(output);
//        messageAppear.playFromStart();
//        messageAppear.setOnFinished(e -> messageDisappear.playFromStart());
//        messageDisappear.setOnFinished(nextStep);
//    }
    private void chooseRandomEnnemy()
    {
        fight.setEnnemy(fight.getEnnemyPkmns().stream()
                .filter(e -> e.getHp() > 0)
                .findAny());
    }

    private void hide(Node n)
    {
        n.setVisible(false);
        n.setDisable(true);
    }

    private void show(Node n)
    {
        n.setVisible(true);
        n.setDisable(false);
    }

    /**
     *
     * @param before
     * @param after
     * @param max
     * @return
     */
    public double calculateDuration(int before, int after, int max)
    {
        return (before > after)
               ? (double) 5 * (before - after) / max + 1
               : (double) 5 * (after - before) / max + 1;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Actions">
    private void flee()
    {
        if (fight.getClass().toString().contains("Dressor"))
            showMessage("On ne peut pas s'enfuir d'un combat de dresseurs !",
                        e -> showChooseTypeGrid(), 1);
        else if (fight.fleeSucceeds())
            showMessage("Vous prenez la fuite !",
                        e -> game.getGameView().setScene("MainScreen"), 1);
        else
            showMessage("Impossible de fuir !",
                        e -> fight.useAttack(randomAttack(), false, this,
                                             then -> this.showChooseTypeGrid()), 1);
    }

    private void choosePokemon(CapturedPkmn newPkmn)
    {
        if (!newPkmn.equals(fight.getMyPkmn())) {
            showMessage(fight.getMyPkmn().getName() + ", reviens !!\n"
                        + "A toi, " + newPkmn.getName(),
                        e -> fight.useAttack(randomAttack(), false, this,
                                             then -> this.showChooseTypeGrid()), 2);
            fight.getMyPkmn().clearVariations();
            fight.setMyPkmn(newPkmn);
            game.getPlayer().swapPokemon(newPkmn);
            refresh();
        }
    }

    private void doAttack(Attack attack)
    {
        Attack ennemyAtk = randomAttack();
        if (fight.getMyPkmn().getSpeed() > fight.getEnnemy().getSpeed())
            fight.useAttack(attack, true, this, e -> secondAttack(ennemyAtk, false));
        else
            fight.useAttack(ennemyAtk, false, this, e -> secondAttack(attack, true));
    }

    /**
     *
     * @param attack
     * @param myPkmn
     */
    public void secondAttack(Attack attack, boolean myPkmn)
    {
        FightingPkmn attacker = (myPkmn) ? fight.getMyPkmn() : fight.getEnnemy();
        if (attacker.getHp() > 0)
            fight.useAttack(attack, myPkmn, this, e -> showChooseTypeGrid());
    }

    /**
     * Used for the ennemy to attack randomly since we don't have actual IA yet
     *
     * @return
     */
    public Attack randomAttack()
    {
        List<Attack> attacks =  fight.getEnnemy().getAttacks();
        Attack a = attacks.get(new Random().nextInt(attacks.size()));
        return a;
    }

    /**
     * Algorithm to choose the action the IA should do Returns numbers
     * corresponding to the choices made Real algorithm will only be implemented
     * in the final version of the program
     *
     * @return
     */
    public Attack chooseEnnemyAction()
    {
        //TODO check if enmy pokemon is weak to my type, changes pokemons if
        //he has a more resistant pokemon to my attacks type
        return randomAttack();
    }

    private void lookForWinner(boolean ennemyDown)
    {
        fight.checkIfHasAWinner(ennemyDown);
        if (!fight.hasWinner()) {
            if (ennemyDown)
                chooseRandomEnnemy();
        } else {
            if (Game.getGame().getScenario() != null)
                if (Game.getGame().getScenario().Instructions().next().equals("heal"))
                    Game.getGame().getPlayer().healPokemon();
            if (ennemyDown)
                showMessage(fight.getMe().getName() + " a gagné !",
                            e -> {
                                game.getGameView().setScene("MainScreen");
                            }, 1);
            else
                showMessage(fight.getMe().getName() + " n'a plus de POKéMON !\n"
                            + fight.getMe().getName() + " est Hors-Jeu !",
                            e -> {
                                game.setCurrentZone(fight.getMe().getLastPokeCenter());
                                fight.getMe().healPokemon();
                                fight.getMe().getBag().exchangeMoney(
                                        -fight.getMe().getBag().getPokeDollars() / 2);
                                game.getGameView().setScene("MainScreen");
                            }, 1);

        }
    }
    // </editor-fold>

    private void addItemsToGrid(Item item, int amount)
    {
        Button useItem = new Button(item.getName() + "   " + amount + "x");
        useItem.setOnMouseClicked(e -> this.useItem(item));
        useItem.setPrefWidth(235);
        useItem.setAlignment(Pos.CENTER_LEFT);
        useItem.setStyle("-fx-font-size: 14;");
        itemsContainer.add(useItem, col, row);

        row++;
        if (row == 5) {
            row = 0;
            col++;
        }
    }

    private void useItem(Item item)
    {
        fight.getMe().getBag().remove(item);
        if (item.getClass().equals(Ball.class))
            if (fight.getClass().toString().contains("Dressor"))
                showMessage("Le dresseur dévie la BALL! Voler, c'est mal!", e -> secondAttack(randomAttack(), false), 1);
            else
                showMessage(fight.getMe().getName() + " utilise: " + item.getName(), e -> useBall((Ball) item), 1);
        else
            choosePkmn((Potion) item);
    }

    private void useBall(Ball ball)
    {
        switch (ball.use(fight.getEnnemy())) {
            case 0:
                showMessage("La balle a râté le Pokémon!",
                            e -> secondAttack(randomAttack(), false), 1);
            case 1:
                showMessage("Zut de flûte! Il s'est libéré!",
                            e -> secondAttack(randomAttack(), false), 1);
            case 2:
                showMessage("Ah! Il avait l'air d'être pris!",
                            e -> secondAttack(randomAttack(), false), 1);
            case 3:
                showMessage("Oh! Presque!",
                            e -> secondAttack(randomAttack(), false), 1);
            case 4:
                showMessage("Top cool! " + fight.getEnnemy().getName() + " est capturé!",
                            e -> {
                                capturePkmn();
                                game.getGameView().setScene("MainScreen");
                            }, 2);
        }
    }

    private void capturePkmn()
    {
        try {
            fight.getMe().addNewPokemon(fight.getEnnemy());
        } catch (ReaderException ex) {
            Logger.getLogger(FightViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void choosePkmn(Potion p)
    {
        hide(itemsContainer);
        initializePkmnsButtonsUseItem(p);
        showChoosePkmnGrid();
    }
}
