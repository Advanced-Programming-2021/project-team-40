package Gameplay;

import Controller.DuelController.GameplayController;
import Database.Cards.Card;
import Database.Cards.Monster;
import GUI.GameplayView;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
    private HBox stats = new HBox(5);
    private Label attackLabel = new Label();
    private Label defenseLabel = new Label();

    public MonsterFieldArea(int id) {
        super();

        MonsterFieldArea thisField = this;
        getCardView().setOnContextMenuRequested(contextMenuEvent -> {
            if (GameplayController.getInstance().gameState == GameState.ATTACK_MODE) {
                try {
                    GameplayController.getInstance().selectCard(String.valueOf(id), "-m", !GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getMonstersFieldById(id).equals(thisField));
                } catch (Exception ignored) {
                }
            }
            if (!GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getMonstersFieldById(id).equals(thisField))
                return;
            ContextMenu contextMenu = new ContextMenu();
            GameplayView.checkItems();
            contextMenu.getItems().addAll(GameplayView.monsterItems);
            contextMenu.show(thisField, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
        });
        getCardView().setOnMouseEntered(mouseEvent -> {
            try {
                GameplayView.updateCardDisplayPanel(thisField);
                if (GameplayController.getInstance().gameState == GameState.RITUAL_SET_MODE) return;
                if (GameplayController.getInstance().gameState == GameState.RITUAL_SUMMON_MODE) return;
                if (GameplayController.getInstance().gameState == GameState.RITUAL_SPELL_ACTIVATED_MODE) return;
                if (GameplayController.getInstance().gameState == GameState.TRIBUTE_SET_MODE) return;
                if (GameplayController.getInstance().gameState == GameState.TRIBUTE_SUMMON_MODE) return;
                if (GameplayController.getInstance().gameState == GameState.ATTACK_MODE) return;
                GameplayController.getInstance().selectCard(String.valueOf(id), "-m", !GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getMonstersFieldById(id).equals(thisField));
                GameplayController.getInstance().gameState = GameState.NORMAL_MODE;
            } catch (Exception ignored) {
            }
        });
        getCardView().setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.PRIMARY) return;
            if (GameplayController.getInstance().gameState == GameState.RITUAL_SET_MODE) return;
            if (GameplayController.getInstance().gameState == GameState.RITUAL_SUMMON_MODE) return;
            if (GameplayController.getInstance().gameState == GameState.RITUAL_SPELL_ACTIVATED_MODE) return;
            if (GameplayController.getInstance().gameState == GameState.ATTACK_MODE)
                try {
                    StringBuilder message = GameplayController.getInstance().attack(String.valueOf(id));
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(String.valueOf(message));
                    alert.show();
                    GameplayController.getInstance().gameState = GameState.NORMAL_MODE;
                } catch (Exception e) {
                    GameplayView.showAlert(e.getMessage());
                }
            if (GameplayController.getInstance().gameState == GameState.TRIBUTE_SUMMON_MODE || GameplayController.getInstance().gameState == GameState.TRIBUTE_SET_MODE) {
                boolean ownsTributedCard = false;
                for (MonsterFieldArea monsterField :
                        GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getMonstersField()) {
                    if (monsterField.equals(thisField)) {
                        ownsTributedCard = true;
                        break;
                    }
                }
                if (!ownsTributedCard) return;
                GameplayController.getInstance().toTributeCards.add(thisField);
                GameplayController.getInstance().tributeCount--;
                if (GameplayController.getInstance().tributeCount == 0) {
                    GameplayController.getInstance().tributeCards();
                    if (GameplayController.getInstance().gameState == GameState.TRIBUTE_SUMMON_MODE) try {
                        GameplayController.getInstance().summon();
                    } catch (Exception ignored) {
                    }
                    if (GameplayController.getInstance().gameState == GameState.TRIBUTE_SET_MODE) try {
                        GameplayController.getInstance().set();
                    } catch (Exception ignored) {
                    }
                    GameplayController.getInstance().gameState = GameState.NORMAL_MODE;
                }
            }
        });
        defenseLabel.setAlignment(Pos.CENTER_RIGHT);
        attackLabel.setAlignment(Pos.CENTER_LEFT);
        getStats().getChildren().add(attackLabel);
        getStats().getChildren().add(defenseLabel);
        getChildren().add(getStats());
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
            attackLabel.setText(null);
            defenseLabel.setText(null);
        }
    }

    public void changePosition() {
        isAttack = !isAttack;
        hasSwitchedMode = true;
        if (isAttack) getCardView().setFill(getCard().getFill());
        if (getCardView().getRotate() == -90) setRotate(0);
        if (getCardView().getRotate() == 0) setRotate(-90);
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
        attackLabel.setText(String.valueOf(attackPoint));
    }

    public int getDefensePoint() {
        return defensePoint;
    }

    public void setDefensePoint(int defensePoint) {
        this.defensePoint = defensePoint;
        defenseLabel.setText(String.valueOf(defensePoint));
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

    public HBox getStats() {
        return stats;
    }
}
