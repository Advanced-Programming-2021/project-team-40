package View;

import Database.Cards.Card;

import java.util.ArrayList;

public class GraveyardView {
    public static void showGraveyard(ArrayList<Card> graveyard){
        for (Card card: graveyard) {
            CardView.showCard(card);
        }
    }
}