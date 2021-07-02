package GUI;

import Controller.DatabaseController.DatabaseController;
import Controller.DuelController.GameplayController;
import Database.User;
import Gameplay.Gameplay;
import Gameplay.MonsterFieldArea;
import Gameplay.Player;
import Gameplay.SpellAndTrapFieldArea;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GameplayView extends Application {
    final static private int GAMEPLAY_WIDTH = 400;
    final static private int GAMEPLAY_HEIGHT = 300;
    private BorderPane pane = new BorderPane();
    private ArrayList<MenuItem> monsterItems = new ArrayList<>();
    private ArrayList<MenuItem> spellItems = new ArrayList<>();

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
        gameplay.getOpponentPlayer().getField().setRotate(180);
        select();
        monsterItems.add(new MenuItem("Flip-summon"));
        monsterItems.add(new MenuItem("Change position"));
        pane.getChildren().add(gameplay.getCurrentPlayer().getField());
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
            spellAndTrapFieldAreas[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        GameplayController.getInstance().selectCard(String.valueOf(id), "-s", !GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getSpellAndTrapFieldById(id).equals(spellAndTrapFieldAreas[id - 1]));
                        //TODO: update show card panel
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        //TODO: pop-up or sth
                    }
                }
            });
        }
    }

    private void selectMonsterFieldArea(MonsterFieldArea[] monsterFieldAreas) {
        for (int i = 0; i < monsterFieldAreas.length; i++) {
            int id = i + 1;
            monsterFieldAreas[i].setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent contextMenuEvent) {
                    ContextMenu contextMenu = new ContextMenu();
                    contextMenu.getItems().addAll(monsterItems);
                    contextMenu.show(monsterFieldAreas[id - 1],contextMenuEvent.getScreenX(),contextMenuEvent.getScreenY());
                }
            });
            monsterFieldAreas[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        GameplayController.getInstance().selectCard(String.valueOf(id), "-m", !GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getMonstersFieldById(id).equals(monsterFieldAreas[id - 1]));
                        //TODO: update show card panel
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        //TODO: pop-up or sth
                    }
                }
            });
        }
    }
}
