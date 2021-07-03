package GUI;

import Controller.DatabaseController.DatabaseController;
import Controller.DuelController.GameplayController;
import Database.User;
import Gameplay.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GameplayView extends Application {
    final static private int GAMEPLAY_WIDTH = 700;
    final static private int GAMEPLAY_HEIGHT = 600;
    private static long clickTime = 0;
    private BorderPane pane = new BorderPane();
    private ArrayList<MenuItem> monsterItems = new ArrayList<>();
    private ArrayList<MenuItem> spellItems = new ArrayList<>();

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
        select();
        deselect();
        changePosition();
        flipSummon();
        activateEffect();
        gameplay.getCurrentPlayer().getField().setAlignment(Pos.CENTER);
        gameplay.getOpponentPlayer().getField().setAlignment(Pos.CENTER);
        pane.setBottom(gameplay.getCurrentPlayer().getField());
        pane.setTop(gameplay.getOpponentPlayer().getField());
    }

    private void activateEffect() {
        MenuItem effectItem = new MenuItem("Activate effect");
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

    private void deselect() {
        Gameplay gameplay = GameplayController.getInstance().gameplay;
        MonsterFieldArea[] monsterFieldAreas = gameplay.getCurrentPlayer().getField().getMonstersField();
        SpellAndTrapFieldArea[] spellAndTrapFieldAreas = gameplay.getCurrentPlayer().getField().getSpellAndTrapField();
        for (MonsterFieldArea monsterFieldArea : monsterFieldAreas) {
            monsterFieldArea.setOnMouseExited(this::handleSelect);
        }
        for (SpellAndTrapFieldArea spellField :
                spellAndTrapFieldAreas) {
            spellField.setOnMouseExited(this::handleSelect);
        }
        monsterFieldAreas = gameplay.getOpponentPlayer().getField().getMonstersField();
        spellAndTrapFieldAreas = gameplay.getOpponentPlayer().getField().getSpellAndTrapField();
        for (MonsterFieldArea monsterFieldArea : monsterFieldAreas) {
            monsterFieldArea.setOnMouseExited(this::handleSelect);
        }
        for (SpellAndTrapFieldArea spellField :
                spellAndTrapFieldAreas) {
            spellField.setOnMouseExited(this::handleSelect);
        }
    }

    private void handleSelect(MouseEvent mouseEvent) {
        try {
            GameplayController.getInstance().deselectCard();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //TODO: pop-up or sth
        }
    }

    private void changePosition() {
        MenuItem changePositionItem = new MenuItem("Change position");
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

    private void flipSummon() {
        MenuItem flipItem = new MenuItem("Flip-Summon");
        flipItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FieldArea fieldArea;
                if ((fieldArea = GameplayController.getInstance().gameplay.getSelectedField()) == null) return;
                if (!(fieldArea instanceof MonsterFieldArea)) return;
                try {
                    GameplayController.getInstance().flipSummon();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    //TODO: popup or sth
                }
            }
        });
        monsterItems.add(flipItem);
    }

    private void select() {
        Gameplay gameplay = GameplayController.getInstance().gameplay;
        MonsterFieldArea[] monsterFieldAreas = gameplay.getCurrentPlayer().getField().getMonstersField();
        SpellAndTrapFieldArea[] spellAndTrapFieldAreas = gameplay.getCurrentPlayer().getField().getSpellAndTrapField();
        selectMonsterFieldArea(monsterFieldAreas);
        selectSpellAndTrap(spellAndTrapFieldAreas);
        monsterFieldAreas = gameplay.getOpponentPlayer().getField().getMonstersField();
        spellAndTrapFieldAreas = gameplay.getOpponentPlayer().getField().getSpellAndTrapField();
        selectMonsterFieldArea(monsterFieldAreas);
        selectSpellAndTrap(spellAndTrapFieldAreas);
    }

    private void selectSpellAndTrap(SpellAndTrapFieldArea[] spellAndTrapFieldAreas) {
        for (int i = 0; i < spellAndTrapFieldAreas.length; i++) {
            int id = i + 1;
            spellAndTrapFieldAreas[i].setOnContextMenuRequested(contextMenuEvent -> {
                if (GameplayController.getInstance().gameplay.getSelectedField() == null) return;
                if (GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getSpellAndTrapFieldById(id).equals(spellAndTrapFieldAreas[id - 1]))
                    return;
                ContextMenu contextMenu = new ContextMenu();
                contextMenu.getItems().addAll(spellItems);
                contextMenu.show(spellAndTrapFieldAreas[id - 1], contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
            });
            spellAndTrapFieldAreas[i].setOnMouseClicked(mouseEvent -> {
                try {
                    if (mouseEvent.getButton() != MouseButton.PRIMARY) return;
//                        if (GameplayController.getInstance().gameplay.getSelectedField() == null) return;
                    GameplayController.getInstance().selectCard(String.valueOf(id), "-s", !GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getSpellAndTrapFieldById(id).equals(spellAndTrapFieldAreas[id - 1]));
                    //TODO: update show card panel
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    //TODO: pop-up or sth
                }
            });
        }
    }

    private void selectMonsterFieldArea(MonsterFieldArea[] monsterFieldAreas) {
        for (int i = 0; i < monsterFieldAreas.length; i++) {
            int id = i + 1;
            monsterFieldAreas[i].setOnContextMenuRequested(contextMenuEvent -> {
                if (GameplayController.getInstance().gameplay.getSelectedField() == null) return;
                if (GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getMonstersFieldById(id).equals(monsterFieldAreas[id - 1]))
                    return;
                ContextMenu contextMenu = new ContextMenu();
                contextMenu.getItems().addAll(monsterItems);
                contextMenu.show(monsterFieldAreas[id - 1], contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
            });
            monsterFieldAreas[i].setOnMouseClicked(mouseEvent -> {
                System.out.println(mouseEvent.getClickCount());
                if (mouseEvent.getButton() != MouseButton.PRIMARY) return;
                if (System.currentTimeMillis() - clickTime < 500) {
                    try {
                        GameplayController.getInstance().attack(String.valueOf(id));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                else try {
                    GameplayController.getInstance().selectCard(String.valueOf(id), "-m", !GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getMonstersFieldById(id).equals(monsterFieldAreas[id - 1]));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                clickTime = System.currentTimeMillis();
                //TODO: update show card panel
            });
        }
    }
}

class CardDisplayPanel extends GridPane {
    public CardDisplayPanel() {
        super();

    }
}
