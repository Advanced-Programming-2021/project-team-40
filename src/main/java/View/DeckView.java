package View;

import Controller.DatabaseController.DeckController;
import Database.Cards.Card;
import Database.Cards.Monster;
import Database.Cards.SpellAndTrap;
import Database.Deck;
import Database.User;

import java.util.ArrayList;
import java.util.Collections;

public class DeckView {

    public static void showAllDecks(User user) {
        System.out.println("Decks:");
        System.out.println("Active deck:");
        showDeck(user.getActiveDeck());
        ArrayList<Deck> otherDecks = new ArrayList<>();
        for (Deck deck : user.getDecks()) {
            if (!deck.isActive()) otherDecks.add(deck);
        }
        otherDecks.sort(Deck.deckNameComparator);
        System.out.println("Other decks:");
        for (Deck deck : otherDecks) {
            showDeck(deck);
        }
    }

    public static void showDeck(Deck deck) {
        System.out.println(deck.getName() + ": main deck " + deck.getMainCards().size() + ", side deck " +
                deck.getSideCards().size() + ", " + DeckController.getInstance().deckValidityString(deck));
    }

    public void showDetailedDeck(Deck deck, boolean isSide) {
        ArrayList<Card> allCards;
        if (isSide) allCards = deck.getSideCards();
        else allCards = deck.getMainCards();
        ArrayList<Monster> monsters = new ArrayList<>();
        ArrayList<SpellAndTrap> spellAndTraps = new ArrayList<>();
        for (Card card : allCards) if (card instanceof Monster) monsters.add((Monster) card);
        for (Card card : allCards) if (card instanceof SpellAndTrap) spellAndTraps.add((SpellAndTrap) card);
        monsters.sort(Card.cardNameComparator);
        spellAndTraps.sort(Card.cardNameComparator);
        System.out.println("Deck: " + deck.getName());
        if (isSide) System.out.println("Side deck:");
        else System.out.println("Main deck:");
        System.out.println("Monsters:");
        for (Monster monster : monsters) CardView.showCardInList(monster);
        System.out.println("Spell and Traps:");
        for (SpellAndTrap spellAndTrap : spellAndTraps) CardView.showCardInList(spellAndTrap);
    }
}
