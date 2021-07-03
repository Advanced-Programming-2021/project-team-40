package Gameplay;

import Controller.DuelController.GameplayController;
import Database.Cards.Card;
import View.Exceptions.NoCardIsSelectedException;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;

public class HandFieldArea extends FieldArea {
    private static ArrayList<MenuItem> handItems = new ArrayList<>();

    public HandFieldArea(Card card) {
        putCard(card, false);
        canBePutOnBoard = true;
        HandFieldArea thisField = this;
        createMenuItems();
        this.setOnMouseExited(mouseEvent -> {
            try {
                GameplayController.getInstance().deselectCard();
            } catch (NoCardIsSelectedException e) {
                System.out.println(e.getMessage());
            }
        });
        this.setOnContextMenuRequested(contextMenuEvent -> {
            if (GameplayController.getInstance().gameplay.getSelectedField() == null) return;
            Gameplay gameplay = GameplayController.getInstance().gameplay;
            ArrayList<HandFieldArea> hand = gameplay.getCurrentPlayer().getPlayingHand();
            if (!hand.contains(thisField)) return;
            if (GameplayController.getInstance().gameplay.getSelectedField() != thisField) return;
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.getItems().addAll(handItems);
            contextMenu.show(thisField, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
        });
        this.setOnMouseClicked(mouseEvent -> {
            Gameplay gameplay = GameplayController.getInstance().gameplay;
            ArrayList<HandFieldArea> hand = gameplay.getCurrentPlayer().getPlayingHand();
            if (mouseEvent.getButton() != MouseButton.PRIMARY) return;
            if (!hand.contains(thisField)) return;
            int id = -1;
            for (int i = 0; i < hand.size(); i++) {
                if (hand.get(i).equals(thisField)) {
                    System.out.println(i);
                    id = i;
                    break;
                }
            }
            try {
                GameplayController.getInstance().selectCard(String.valueOf(id + 1), "-h", false);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    private static void createMenuItems() {
        if (!handItems.isEmpty()) return;
        MenuItem setItem = new MenuItem("Set");
        setItem.setOnAction(actionEvent -> {
            try {
                GameplayController.getInstance().set();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        MenuItem summonItem = new MenuItem("Summon");
        summonItem.setOnAction(actionEvent -> {
            try {
                GameplayController.getInstance().summon();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        MenuItem activateEffectItem = new MenuItem("Activate effect");
        activateEffectItem.setOnAction(actionEvent -> {
            try {
                GameplayController.getInstance().activateEffect(SpellAndTrapActivationType.NORMAL);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        handItems.add(setItem);
        handItems.add(summonItem);
        handItems.add(activateEffectItem);
    }
}
