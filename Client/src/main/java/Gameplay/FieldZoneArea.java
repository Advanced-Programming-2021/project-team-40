package Gameplay;

import Controller.DuelController.GameplayController;
import Database.Cards.Card;
import GUI.GameplayView;
import javafx.scene.control.ContextMenu;

public class FieldZoneArea extends SpellAndTrapFieldArea {
    public FieldZoneArea() {
        super(-1);
        SpellAndTrapFieldArea thisField = this;
        getCardView().setOnContextMenuRequested(contextMenuEvent -> {
            if (GameplayController.getInstance().gameplay.getSelectedField() == null) return;
            if (!GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getFieldZone().equals(thisField))
                return;
            GameplayView.checkItems();
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.getItems().addAll(GameplayView.spellItems);
            contextMenu.show(thisField, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
        });
        getCardView().setOnMouseEntered(mouseEvent -> {
            GameState gameState = GameplayController.getGameState();
            try {
                GameplayView.updateCardDisplayPanel(thisField);
                if (gameState == GameState.CHAIN_MODE) return;
                if (gameState == GameState.EQUIP_ACTIVATION_MODE) return;
                if (gameState == GameState.RITUAL_SET_MODE) return;
                if (gameState == GameState.RITUAL_SUMMON_MODE) return;
                if (gameState == GameState.RITUAL_SPELL_ACTIVATED_MODE) return;
                if (gameState == GameState.TRIBUTE_SUMMON_MODE) return;
                if (gameState == GameState.TRIBUTE_SET_MODE) return;
                if (gameState == GameState.ATTACK_MODE) return;
                GameplayController.getInstance().selectCard(null, "-f", !GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getFieldZone().equals(thisField));
            } catch (Exception ignored) {
            }
        });
    }

    @Override
    public void putCard(Card card, boolean visibility) {
        if (card != null && this.card != null)
            GameplayController.getInstance().destroySpellAndTrapCard(GameplayController.getInstance().gameplay.getCurrentPlayer(), this);
        super.putCard(card, visibility);
    }

}