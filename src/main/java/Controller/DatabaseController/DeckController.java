package Controller.DatabaseController;

import Database.Cards.Card;
import Database.Deck;
import Database.User;
import View.Exceptions.DeckIsFullException;
import View.Exceptions.InvalidCardNameException;
import View.Exceptions.InvalidDeckNameException;
import View.Exceptions.RepetitiveDeckNameException;

import java.util.Collections;

public class DeckController {
    private static DeckController deckController;
    private DeckController() {
    }

    public static DeckController getInstance() {
        if (deckController == null)
            deckController = new DeckController();
        return deckController;
    }
    public void removeCard(String deckName,String cardName,boolean isSide, User currentUser) throws DeckIsFullException, InvalidCardNameException, InvalidDeckNameException {
        Deck currentDeck;
        Card currentCard;
        if ((currentCard = Card.getCardByName(cardName)) == null) throw new InvalidCardNameException(cardName);
        if ((currentDeck = currentUser.getDeckByName(deckName)) == null) throw new InvalidDeckNameException(deckName);
        if (isSide) removeCardFromDeck(currentCard, "Side", currentDeck);
        else removeCardFromDeck(currentCard, "main.java.Main", currentDeck);
    }

    public void addCard(String deckName,String cardName,boolean isSide, User currentUser) throws InvalidCardNameException, InvalidDeckNameException, DeckIsFullException {
        Deck currentDeck;
        Card currentCard;
        if ((currentCard = Card.getCardByName(cardName)) == null) throw new InvalidCardNameException(cardName);
        if ((currentDeck = currentUser.getDeckByName(deckName)) == null) throw new InvalidDeckNameException(deckName);
        if (isSide) addCardToDeck(currentCard, "Side", currentDeck);
        else addCardToDeck(currentCard, "main.java.Main", currentDeck);
    }

    public void activateDeck(String deckName, User currentUser) throws InvalidDeckNameException {
        Deck deck;
        if ((deck = currentUser.getDeckByName(deckName)) == null) throw new InvalidDeckNameException(deckName);
        currentUser.setActiveDeck(deck);
    }

    public void deleteDeck(String deckName, User currentUser) throws InvalidDeckNameException {
        Deck deck;
        if ((deck = currentUser.getDeckByName(deckName)) == null) throw new InvalidDeckNameException(deckName);
        currentUser.getDecks().remove(deck);
    }

    public void createDeck(String deckName, User currentUser) throws RepetitiveDeckNameException {
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

    public boolean isDeckInvalid(Deck activeDeck) {
        int mainCount = activeDeck.getMainCards().size();
        int sideCount = activeDeck.getSideCards().size();
        return mainCount < 40 || mainCount > 60 || sideCount > 15;
    }
}