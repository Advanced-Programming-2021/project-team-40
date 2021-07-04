package Gameplay;

import Controller.DuelController.GameplayController;
import Database.Cards.Card;
import Database.Cards.Monster;
import GUI.GameplayView;
import javafx.scene.control.ContextMenu;
import javafx.scene.shape.Rectangle;

public class MonsterFieldArea extends FieldArea {
    private boolean canAttack = true;
    private boolean canBeAttacked = true;
    private boolean canBeDestroyed = true;
    private boolean isAttack;
    private boolean hasAttacked;
    private boolean hasSwitchedMode;
    private int attackPoint;
    private int defensePoint;
    private int turnsLeftForEffect;

    public MonsterFieldArea(int id) {
        super();
        MonsterFieldArea thisField = this;
        this.setOnContextMenuRequested(contextMenuEvent -> {
            if (GameplayController.getInstance().gameplay.getSelectedField() == null) return;
            if (GameplayController.getInstance().gameState == GameState.ATTACK_MODE) {
                try {
                    GameplayController.getInstance().selectCard(String.valueOf(id), "-m", !GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getMonstersFieldById(id).equals(thisField));
                } catch (Exception ignored) {
                }

            }
            if (!GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getMonstersFieldById(id).equals(thisField))
                return;
            ContextMenu contextMenu = new ContextMenu();
            GameplayView.getInstance().checkItems();
            contextMenu.getItems().addAll(GameplayView.getInstance().monsterItems);
            contextMenu.show(thisField, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
        });
        this.setOnMouseEntered(mouseEvent -> {
            try {
                if (GameplayController.getInstance().gameState == GameState.ATTACK_MODE) return;
                GameplayController.getInstance().selectCard(String.valueOf(id), "-m", !GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getMonstersFieldById(id).equals(thisField));
                GameplayController.getInstance().gameState = GameState.NORMAL_MODE;
            } catch (Exception ignored) {
            }
        });
        this.setOnMouseClicked(mouseEvent -> {
            try {
                if (GameplayController.getInstance().gameState != GameState.ATTACK_MODE) return;
                GameplayController.getInstance().attack(String.valueOf(id));

            } catch (Exception e) {
                GameplayView.showAlert(e.getMessage());
            }
        });
    }

    @Override
    public void putCard(Card card, boolean isAttack) {
        this.isAttack = isAttack;
        super.putCard(card, isAttack);
        if (card != null) {
            setAttackPoint(((Monster) card).getAttackPoints());
            setDefensePoint(((Monster) card).getDefensePoints());
            hasSwitchedMode = true;
        } else {
            setAttackPoint(0);
            setAttackPoint(0);
        }
    }

    public void changePosition() {
        isAttack = !isAttack;
        hasSwitchedMode = true;
    }

    public boolean isAttack() {
        return isAttack;
    }

    public void setAttack(boolean attack) {
        isAttack = attack;
    }

    public boolean hasAttacked() {
        return hasAttacked;
    }

    public boolean hasSwitchedMode() {
        return hasSwitchedMode;
    }

    public void setHasSwitchedMode(boolean hasSwitchedMode) {
        this.hasSwitchedMode = hasSwitchedMode;
    }

    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    public int getAttackPoint() {
        return attackPoint;
    }

    public void setAttackPoint(int attackPoint) {
        this.attackPoint = attackPoint;
    }

    public int getDefensePoint() {
        return defensePoint;
    }

    public void setDefensePoint(int defensePoint) {
        this.defensePoint = defensePoint;
    }

    public int getTurnsLeftForEffect() {
        return turnsLeftForEffect;
    }

    public void setTurnsLeftForEffect(int turnsLeftForEffect) {
        this.turnsLeftForEffect = turnsLeftForEffect;
    }

    public boolean isCanBeDestroyed() {
        return canBeDestroyed;
    }

    public void setCanBeDestroyed(boolean canBeDestroyed) {
        this.canBeDestroyed = canBeDestroyed;
    }

    public boolean isCanBeAttacked() {
        return canBeAttacked;
    }

    public void setCanBeAttacked(boolean canBeAttacked) {
        this.canBeAttacked = canBeAttacked;
    }

    public boolean isCanAttack() {
        return canAttack;
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }
}
