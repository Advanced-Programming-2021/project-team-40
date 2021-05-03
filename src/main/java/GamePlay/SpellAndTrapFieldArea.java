package GamePlay;

import Database.Cards.Card;

public class SpellAndTrapFieldArea extends FieldArea{
    private boolean isActivated;
    public SpellAndTrapFieldArea(Card card, boolean visibility) {
        super(card, visibility);
    }
}
