package GUI;

import Controller.DatabaseController.DatabaseController;
import Controller.DuelController.GameplayController;
import Database.User;
import Gameplay.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GameplayView extends Application {
    final private static int GAMEPLAY_WIDTH = 700;
    final private static int GAMEPLAY_HEIGHT = 600;
    public static ArrayList<MenuItem> monsterItems = new ArrayList<>();
    public static ArrayList<MenuItem> spellItems = new ArrayList<>();
    public static ArrayList<MenuItem> handItems = new ArrayList<>();
    private MenuItem effectItem = new MenuItem("Activate effect") {
    };
    private MenuItem summonItem = new MenuItem("Summon");
    private MenuItem setItem = new MenuItem("Set");
    private MenuItem directAttackItem = new MenuItem("Direct attack");
    private MenuItem flipItem = new MenuItem("Flip summon");
    private MenuItem changePositionItem = new MenuItem("Change position");
    private BorderPane pane = new BorderPane();

    public static int getGameplayHeight() {
        return GAMEPLAY_HEIGHT;
    }

    public static int getGameplayWidth() {
        return GAMEPLAY_WIDTH;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(pane);
        pane.setPrefHeight(GAMEPLAY_HEIGHT);
        pane.setPrefWidth(GAMEPLAY_WIDTH);
        createBoard();
        stage.setScene(scene);
        stage.show();
    }

    public void createBoard() {
        DatabaseController.getInstance();
        Gameplay gameplay = new Gameplay(new Player(User.getUserByName("DanDan")), new Player(User.getUserByName("KiaKia")), 1);
        GameplayController.getInstance().setGameplay(gameplay);
        GameplayController.getInstance().setStartingPlayer();
        GameplayController.getInstance().dealCardsAtBeginning();
        GameplayController.getInstance().doPhaseAction();
        gameplay.getOpponentPlayer().getField().setRotate(180);
        changePosition();
        flipSummon();
        activateEffect();
        directAttack();
        createHandItems();
        gameplay.getCurrentPlayer().getField().setAlignment(Pos.CENTER);
        gameplay.getOpponentPlayer().getField().setAlignment(Pos.CENTER);
        pane.setBottom(gameplay.getCurrentPlayer().getField());
        pane.setTop(gameplay.getOpponentPlayer().getField());
    }

    private void activateEffect() {
        effectItem.setOnAction(actionEvent -> {
            try {
                GameplayController.getInstance().activateEffect(SpellAndTrapActivationType.NORMAL);
                //TODO: chain doesn't work
            } catch (Exception e) {
                System.out.println(e.getMessage());
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
                System.out.println(e.getMessage());
                //TODO: popup or sth
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
                System.out.println(e.getMessage());
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
                System.out.println(e.getMessage());
                //TODO: popup or sth
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
                System.out.println(e.getMessage());
            }
        });
        summonItem.setOnAction(actionEvent -> {
            try {
                GameplayController.getInstance().summon();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        effectItem.setOnAction(actionEvent -> {
            try {
                GameplayController.getInstance().activateEffect(SpellAndTrapActivationType.NORMAL);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        handItems.add(setItem);
        handItems.add(summonItem);
        handItems.add(effectItem);
    }
}

class CardDisplayPanel extends GridPane {
    public CardDisplayPanel() {
        super();

    }
}
