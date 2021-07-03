package Gameplay;

import Database.Cards.Card;
import javafx.scene.shape.Rectangle;

public class FieldArea extends Rectangle {
    private static final int FIELD_AREA_WIDTH = 50;
    private static final int FIELD_AREA_HEIGHT = 70;
    private boolean isEffectAvailable = true;
    protected boolean canBePutOnBoard = false;
    protected Card card;
    protected boolean visibility;
    public FieldArea(){
        super(FIELD_AREA_WIDTH,FIELD_AREA_HEIGHT);
    }

    public Card getCard() {
        return card;
    }

    public void putCard(Card card, boolean visibility){
        this.card = card;
        this.visibility = visibility;
    }

    public boolean visibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public boolean canBePutOnBoard() {
        return canBePutOnBoard;
    }

    public boolean isEffectAvailable() {
        return isEffectAvailable;
    }

    public void setEffectAvailable(boolean effectAvailable) {
        isEffectAvailable = effectAvailable;
    }

    public static int getFieldAreaWidth() {
        return FIELD_AREA_WIDTH;
    }

    public static int getFieldAreaHeight() {
        return FIELD_AREA_HEIGHT;
    }
}
