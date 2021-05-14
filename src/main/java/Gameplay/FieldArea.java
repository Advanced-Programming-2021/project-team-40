package Gameplay;

import Database.Cards.Card;

public class FieldArea {
    private boolean isEffectAvailable = true;
    protected boolean canBePutOnBoard = false;
    protected Card card;
    protected boolean visibility;
    public FieldArea(){

    }

    public Card getCard() {
        return card;
    }

    public void putCard(Card card, boolean visibility){
        this.card = card;
        this.visibility = visibility;
    }

    public boolean isVisible() {
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
}
