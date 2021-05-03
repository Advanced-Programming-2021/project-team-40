package GamePlay;

import Database.Cards.Card;

public class FieldArea {
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

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}
