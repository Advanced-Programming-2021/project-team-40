package GamePlay;

import Database.Cards.Card;

public class FieldArea {
    private Card card;
    private boolean visibility;
    public FieldArea(Card card, boolean visibility){
        this.card = card;
        this.visibility = visibility;
    }
}
