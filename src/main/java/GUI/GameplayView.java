package GUI;

import Controller.DatabaseController.DatabaseController;
import Controller.DuelController.GameplayController;
import Database.Cards.Card;
import Database.Cards.SpellAndTrap;
import Database.User;
import Gameplay.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GameplayView extends Application {
    final private static int GAMEPLAY_WIDTH = 700;
    final private static int GAMEPLAY_HEIGHT = 600;
    public ArrayList<MenuItem> monsterItems = new ArrayList<>();
    public ArrayList<MenuItem> spellItems = new ArrayList<>();
    public ArrayList<MenuItem> handItems = new ArrayList<>();
    private static GameplayView gameplayView;
    private MenuItem effectItem = new MenuItem("Activate effect");
    private MenuItem summonItem = new MenuItem("Summon");
    private MenuItem setItem = new MenuItem("Set");
    private MenuItem directAttackItem = new MenuItem("Direct attack");
    private MenuItem attackItem = new MenuItem("Attack");
    private MenuItem flipItem = new MenuItem("Flip summon");
    private MenuItem changePositionItem = new MenuItem("Change position");
    private Button nextPhaseButton = new Button("Next phase");
    private Button settingsButton = new Button("Settings");
    private Alert addedCardsAlert = new Alert(Alert.AlertType.INFORMATION);
    private Alert newPhaseAlert = new Alert(Alert.AlertType.INFORMATION);
    private static BorderPane pane = new BorderPane();

    public static GameplayView getInstance() {
        if (gameplayView == null) gameplayView = new GameplayView();
        return gameplayView;
    }
    public void checkItems() {
        Phase currentPhase = GameplayController.getInstance().gameplay.getCurrentPhase();
        GameState gameState = GameplayController.getInstance().gameState;
        FieldArea selectedField = GameplayController.getInstance().gameplay.getSelectedField();
        summonItem.setDisable(true);
        setItem.setDisable(true);
        flipItem.setDisable(true);
        effectItem.setDisable(true);
        directAttackItem.setDisable(true);
        attackItem.setDisable(true);
        switch (currentPhase) {
            case BATTLE_PHASE:
                if (gameState == GameState.CHAIN_MODE)
                    effectItem.setDisable(false);
                if (gameState == GameState.ATTACK_MODE &&
                    selectedField instanceof MonsterFieldArea &&
                    !((MonsterFieldArea) selectedField).hasAttacked() &&
                    ((MonsterFieldArea) selectedField).isAttack())
                    attackItem.setDisable(false);
                if (GameplayController.getInstance().isOpponentFieldEmpty())
                    directAttackItem.setDisable(false);
                break;
            case MAIN_PHASE_ONE:
            case MAIN_PHASE_TW0:
                if (selectedField.getCard() instanceof SpellAndTrap)
                    effectItem.setDisable(false);
                if (!GameplayController.getInstance().gameplay.hasPlacedMonster()) {
                    summonItem.setDisable(false);
                    setItem.setDisable(false);
                }
                if (selectedField instanceof MonsterFieldArea &&
                    !((MonsterFieldArea) selectedField).hasSwitchedMode())
                    changePositionItem.setDisable(false);
                //TODO: flip summon
                break;
        }
    }

    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid action");
        alert.setHeaderText(message);
        alert.show();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(pane);
        pane.setPrefHeight(GAMEPLAY_HEIGHT);
        pane.setPrefWidth(GAMEPLAY_WIDTH);
        createBoard();
        stage.setScene(scene);
        stage.show();
        addedCardsAlert.setTitle("New Card!");
        newPhaseAlert.setTitle("New Phase!");
        addedCardsAlert.setHeaderText(GameplayController.getInstance().doPhaseAction());
        addedCardsAlert.show();
    }

    public void createBoard() {
        DatabaseController.getInstance();
        Gameplay gameplay = new Gameplay(new Player(User.getUserByName("DanDan")), new Player(User.getUserByName("KiaKia")), 1);
        GameplayController.getInstance().setGameplay(gameplay);
        GameplayController.getInstance().setStartingPlayer();
        GameplayController.getInstance().dealCardsAtBeginning();
        gameplay.getOpponentPlayer().getField().setRotate(180);
        changePosition();
        flipSummon();
        activateEffect();
        attack();
        directAttack();
        createHandItems();
        nextPhase();
        gameplay.getCurrentPlayer().getField().setAlignment(Pos.CENTER);
        gameplay.getOpponentPlayer().getField().setAlignment(Pos.CENTER);
//        pane.setBackground(new Background(new BackgroundFill(Card.NORMAL_FIELD)));
        pane.setBottom(gameplay.getCurrentPlayer().getField());
        pane.setTop(gameplay.getOpponentPlayer().getField());
        nextPhaseButton.setAlignment(Pos.BOTTOM_RIGHT);
        pane.setRight(nextPhaseButton);
        hideOpponentHands();
    }

    private void nextPhase() {
        nextPhaseButton.setOnAction(actionEvent -> {
            if (GameplayController.getInstance().gameState != GameState.NORMAL_MODE) return;
            String newPhaseMessage = GameplayController.getInstance().goToNextPhase();
            String addedCards = GameplayController.getInstance().doPhaseAction();
            if (addedCards != null) {
                addedCardsAlert.setHeaderText(newPhaseMessage + "\n" + addedCards);
                addedCardsAlert.show();
            } else {
                newPhaseAlert.setHeaderText(newPhaseMessage);
                newPhaseAlert.show();
            }
        });
    }

    private void attack() {
        attackItem.setOnAction(actionEvent -> GameplayController.getInstance().gameState = GameState.ATTACK_MODE);
        monsterItems.add(attackItem);
    }

    private void activateEffect() {
        effectItem.setOnAction(actionEvent -> {
            try {
                GameplayController.getInstance().activateEffect(SpellAndTrapActivationType.NORMAL);
                //TODO: chain doesn't work
            } catch (Exception e) {
                showAlert(e.getMessage());
            }
        });
        spellItems.add(effectItem);
    }

    private void changePosition() {
        changePositionItem.setOnAction(actionEvent -> {
            FieldArea fieldArea;
            if ((fieldArea = GameplayController.getInstance().gameplay.getSelectedField()) == null) return;
            if (!(fieldArea instanceof MonsterFieldArea)) return;
            MonsterFieldArea selectedMonsterField = (MonsterFieldArea) fieldArea;
            try {
                GameplayController.getInstance().changePosition(selectedMonsterField.isAttack());
            } catch (Exception e) {
                showAlert(e.getMessage());
            }
        });
        monsterItems.add(changePositionItem);
    }

    private void directAttack() {
        directAttackItem.setOnAction(actionEvent -> {
            try {
                String message = GameplayController.getInstance().directAttack();
                System.out.println(message);
            } catch (Exception e) {
                showAlert(e.getMessage());
            }
        });
        monsterItems.add(directAttackItem);
    }

    private void flipSummon() {
        flipItem.setOnAction(actionEvent -> {
            FieldArea fieldArea;
            if ((fieldArea = GameplayController.getInstance().gameplay.getSelectedField()) == null) return;
            if (!(fieldArea instanceof MonsterFieldArea)) return;
            try {
                GameplayController.getInstance().flipSummon();
            } catch (Exception e) {
                showAlert(e.getMessage());
            }
        });
        monsterItems.add(flipItem);
    }

    private void createHandItems() {
        if (!handItems.isEmpty()) return;
        setItem.setOnAction(actionEvent -> {
            try {
                GameplayController.getInstance().set();
            } catch (Exception e) {
                showAlert(e.getMessage());
            }
        });
        summonItem.setOnAction(actionEvent -> {
            try {
                GameplayController.getInstance().summon();
            } catch (Exception e) {
                showAlert(e.getMessage());
            }
        });
        effectItem.setOnAction(actionEvent -> {
            try {
                GameplayController.getInstance().activateEffect(SpellAndTrapActivationType.NORMAL);
            } catch (Exception e) {
                showAlert(e.getMessage());
            }
        });
        handItems.add(setItem);
        handItems.add(summonItem);
        handItems.add(effectItem);
    }

    public void hideOpponentHands() {
        //TODO: implement in switch turn
        Gameplay gameplay = GameplayController.getInstance().gameplay;
        for (Node node :
                gameplay.getCurrentPlayer().getField().getHandFieldArea().getChildren()) {
            HandFieldArea hand = (HandFieldArea) node;
            hand.setFill(hand.getCard().getFill());
        }
        for (Node node :
                gameplay.getOpponentPlayer().getField().getHandFieldArea().getChildren()) {
            HandFieldArea hand = (HandFieldArea) node;
            hand.setFill(new ImagePattern(Card.UNKNOWN_CARD));
        }
        gameplay.getCurrentPlayer().getField().getHandScrollPane().setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        gameplay.getOpponentPlayer().getField().getHandScrollPane().setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }
}

class CardDisplayPanel extends GridPane {
    public CardDisplayPanel() {
        super();
    }
}
