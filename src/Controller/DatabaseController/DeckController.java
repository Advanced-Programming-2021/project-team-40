package Controller.DatabaseController;

import Database.Cards.Card;
import Database.Deck;
import Database.User;
import View.Exceptions.DeckIsFullException;
import View.Exceptions.InvalidCardNameException;
import View.Exceptions.InvalidDeckNameException;
import View.Exceptions.RepetitiveDeckNameException;

import java.util.Collections;
import java.util.regex.Matcher;

public class DeckController {

    public void removeCard(Matcher matcher, User currentUser) throws DeckIsFullException, InvalidCardNameException, InvalidDeckNameException {
        String cardName = matcher.group("cardName");
        String deckName = matcher.group("deckName");
        boolean isSide = false;
        Deck currentDeck;
        Card currentCard;
        if (matcher.group("isSide") != null) isSide = true;
        if ((currentCard = Card.getCardByName(cardName)) == null) throw new InvalidCardNameException(cardName);
        if ((currentDeck = currentUser.getDeckByName(deckName)) == null) throw new InvalidDeckNameException(deckName);
        if (isSide) removeCardFromDeck(currentCard, "Side", currentDeck);
        else removeCardFromDeck(currentCard, "Main", currentDeck);
    }

    public void addCard(Matcher matcher, User currentUser) throws InvalidCardNameException, InvalidDeckNameException, DeckIsFullException {
        String cardName = matcher.group("cardName");
        String deckName = matcher.group("deckName");
        boolean isSide = false;
        Deck currentDeck;
        Card currentCard;
        if (matcher.group("isSide") != null) isSide = true;
        if ((currentCard = Card.getCardByName(cardName)) == null) throw new InvalidCardNameException(cardName);
        if ((currentDeck = currentUser.getDeckByName(deckName)) == null) throw new InvalidDeckNameException(deckName);
        if (isSide) addCardToDeck(currentCard, "Side", currentDeck);
        else addCardToDeck(currentCard, "Main", currentDeck);
    }

    public void activateDeck(Matcher matcher, User currentUser) throws InvalidDeckNameException {
        String deckName = matcher.group("deckName");
        Deck deck;
        if ((deck = currentUser.getDeckByName(deckName)) == null) throw new InvalidDeckNameException(deckName);
        currentUser.setActiveDeck(deck);
    }

    public void deleteDeck(Matcher matcher, User currentUser) throws InvalidDeckNameException {
        String deckName = matcher.group("deckName");
        Deck deck;
        if ((deck = currentUser.getDeckByName(deckName)) == null) throw new InvalidDeckNameException(deckName);
        currentUser.getDecks().remove(deck);
    }

    public void createDeck(Matcher matcher, User currentUser) throws RepetitiveDeckNameException {
        String deckName = matcher.group("deckName");
        if (currentUser.getDeckByName(deckName) != null) throw new RepetitiveDeckNameException(deckName);
        Deck deck = new Deck(deckName);
        currentUser.getDecks().add(deck);
    }

    public void addCardToDeck(Card cardToAdd, String deck, Deck currentDeck) throws DeckIsFullException {
        switch (deck) {
            case "Main":
                if (mainIsFull(currentDeck)) throw new DeckIsFullException("main");
                else currentDeck.getMainCards().add(cardToAdd);
                break;
            case "Side":
                if (sideIsFull(currentDeck)) throw new DeckIsFullException("side");
                else currentDeck.getSideCards().add(cardToAdd);
                break;
        }
    }

    public void removeCardFromDeck(Card cardToRemove, String deck, Deck currentDeck) throws DeckIsFullException {
        switch (deck) {
            case "Main":
                if (mainIsFull(currentDeck)) throw new DeckIsFullException("main");
                else for (int i = 0; i < currentDeck.getMainCards().size(); i++) {
                    if (currentDeck.getMainCards().get(i).equals(cardToRemove)) {
                        currentDeck.getMainCards().remove(i);
                        return;
                    }
                }
                break;
            case "Side":
                if (sideIsFull(currentDeck)) throw new DeckIsFullException("side");
                else for (int i = 0; i < currentDeck.getSideCards().size(); i++) {
                    if (currentDeck.getSideCards().get(i).equals(cardToRemove)) {
                        currentDeck.getSideCards().remove(i);
                        return;
                    }
                }
                break;
        }
    }

    public void shuffleDeck(Deck currentDeck) {
        Collections.shuffle(currentDeck.getMainCards());
        Collections.shuffle(currentDeck.getSideCards());
    }

    private boolean mainIsFull(Deck currentDeck) {
        return currentDeck.getMainCards().size() >= 60;
    }

    private boolean sideIsFull(Deck currentDeck) {
        return currentDeck.getSideCards().size() >= 15;
    }
}