package Gameplay;

import Controller.DuelController.GameplayController;
import Database.Cards.Card;
import GUI.GameplayView;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;

public class HandFieldArea extends FieldArea {

    public HandFieldArea(Card card) {
        putCard(card, false);
        if (this.card != null) setFill(card.getFill());
        canBePutOnBoard = true;
        HandFieldArea thisField = this;
        this.setOnContextMenuRequested(contextMenuEvent -> {
            if (GameplayController.getInstance().gameplay.getSelectedField() == null) return;
            Gameplay gameplay = GameplayController.getInstance().gameplay;
            ArrayList<HandFieldArea> hand = gameplay.getCurrentPlayer().getPlayingHand();
            if (!hand.contains(thisField)) return;
            if (GameplayController.getInstance().gameplay.getSelectedField() != thisField) return;
            GameplayView.checkItems();
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.getItems().addAll(GameplayView.handItems);
            contextMenu.show(thisField, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
        });
        this.setOnMouseClicked(mouseEvent -> {
            Gameplay gameplay = GameplayController.getInstance().gameplay;
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
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
            }
        });
    }
}
