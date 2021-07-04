package Gameplay;

import Controller.DuelController.GameplayController;
import Database.Cards.Card;
import Database.Cards.SpellAndTrap;
import GUI.GameplayView;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;

public class SpellAndTrapFieldArea extends FieldArea {
    private boolean hasJustBeenSet = true;
    private boolean canBeActivated = false;
    public SpellAndTrapFieldArea(int id) {
        SpellAndTrapFieldArea thisField = this;
        this.setOnContextMenuRequested(contextMenuEvent -> {
            if (GameplayController.getInstance().gameplay.getSelectedField() == null) return;
            if (GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getSpellAndTrapFieldById(id).equals(thisField))
                return;
            GameplayView.checkItems();
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.getItems().addAll(GameplayView.spellItems);
            contextMenu.show(thisField, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
        });
        this.setOnMouseClicked(mouseEvent -> {
            try {
                if (mouseEvent.getButton() != MouseButton.PRIMARY) return;
                GameplayController.getInstance().selectCard(String.valueOf(id), "-s", !GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getSpellAndTrapFieldById(id).equals(thisField));
                //TODO: update show card panel
            } catch (Exception e) {
                System.out.println(e.getMessage());
                //TODO: pop-up or sth
            }
        });
    }

    public void setHasJustBeenSet(boolean hasJustBeenSet) {
        this.hasJustBeenSet = hasJustBeenSet;
    }

    public boolean isHasJustBeenSet() {
        return hasJustBeenSet;
    }

    public boolean isCanBeActivated() {
        return canBeActivated;
    }

    public void setCanBeActivated(boolean canBeActivated) {
        this.canBeActivated = canBeActivated;
    }
}
