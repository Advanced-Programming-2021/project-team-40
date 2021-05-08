package View;

import Database.Cards.Card;
import Database.Deck;
import Database.User;

import java.util.ArrayList;

public class UserView {
    public static void showAllCards(User user) {
        ArrayList<Card> allCards = new ArrayList<>();
        for (Deck deck: user.getDecks()) {
            allCards.addAll(deck.getMainCards());
            allCards.addAll(deck.getSideCards());
        }
        allCards.addAll(user.getInactiveCards());
        allCards.sort(Card.cardNameComparator);
        for (Card card: allCards) {
            CardView.showCardInList(card);
        }
    }
}
