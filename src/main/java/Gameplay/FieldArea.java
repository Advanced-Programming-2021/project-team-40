package main.java.Gameplay;

import main.java.Database.Cards.Card;

public class FieldArea {
    protected boolean canBePutOnBoard = false;
    private Card card;
    private boolean visibility;
    public FieldArea(Card card, boolean visibility){
        this.card = card;
        this.visibility = visibility;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public boolean isVisible() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}
