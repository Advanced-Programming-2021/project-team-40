package View;

import Database.Cards.Card;

import java.util.ArrayList;

public class GraveyardView {
    public static void showGraveyard(ArrayList<Card> graveyard) {
        if (graveyard.size()==0){
            System.out.println("graveyard empty");
            return;
        }
        System.out.println("cards in graveyard:");
        int counter = 1;
        for (Card card : graveyard) {
            System.out.print(counter++ + ":");
            CardView.showCardInList(card);
        }
    }
}