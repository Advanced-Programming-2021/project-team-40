package Gameplay;

import Controller.DuelController.GameplayController;
import GUI.GameplayView;
import javafx.scene.control.ContextMenu;

public class SpellAndTrapFieldArea extends FieldArea {
    private boolean hasJustBeenSet = true;
    private boolean canBeActivated = false;

    public SpellAndTrapFieldArea(int id) {
        SpellAndTrapFieldArea thisField = this;
        this.setOnContextMenuRequested(contextMenuEvent -> {
            if (GameplayController.getInstance().gameplay.getSelectedField() == null) return;
            if (GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getSpellAndTrapFieldById(id).equals(thisField))
                return;
            GameplayView.getInstance().checkItems();
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.getItems().addAll(GameplayView.getInstance().spellItems);
            contextMenu.show(thisField, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
        });
        this.setOnMouseEntered(mouseEvent -> {
            try {
                if (GameplayController.getInstance().gameState == GameState.ATTACK_MODE) return;
                GameplayController.getInstance().selectCard(String.valueOf(id), "-s", !GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getSpellAndTrapFieldById(id).equals(thisField));
            } catch (Exception ignored) {
            }
        });
    }

    public boolean isHasJustBeenSet() {
        return hasJustBeenSet;
    }

    public void setHasJustBeenSet(boolean hasJustBeenSet) {
        this.hasJustBeenSet = hasJustBeenSet;
    }

    public boolean isCanBeActivated() {
        return canBeActivated;
    }

    public void setCanBeActivated(boolean canBeActivated) {
        this.canBeActivated = canBeActivated;
    }
}
