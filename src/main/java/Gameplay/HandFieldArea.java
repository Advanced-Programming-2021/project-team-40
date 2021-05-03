package main.java.Gameplay;

import main.java.Database.Cards.Card;

public class HandFieldArea extends FieldArea{

    public HandFieldArea(Card card) {
        putCard(card, false);
        canBePutOnBoard = true;
    }
}
