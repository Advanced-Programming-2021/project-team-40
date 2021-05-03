package main.java.Gameplay;

import main.java.Database.Cards.Card;

public class HandFieldArea extends FieldArea{

    public HandFieldArea(Card card, boolean visibility) {
        super(card, visibility);
        canBePutOnBoard = true;
    }
}
