package View;

import Database.Cards.Card;

import java.util.ArrayList;

public class GraveyardView {
    public static void showGraveyard(ArrayList<Card> graveyard){
        System.out.println("cards in Graveyard:");
        int counter = 1;
        for (Card card: graveyard) {
            System.out.print(counter++ + ":");CardView.showCardInList(card);
        }
    }
}