package Controller.DatabaseController;

import Database.Cards.Card;
import Database.Deck;
import Database.User;
import View.Exceptions.*;

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
    public void removeCard(String deckName,String cardName,boolean isSide, User currentUser) throws DeckIsFullException, InvalidCardNameException, InvalidDeckNameException, CardNotInDeckException {
        Deck currentDeck;
        Card currentCard;
        if ((currentCard = Card.getCardByName(cardName)) == null) throw new InvalidCardNameException(cardName);
        if ((currentDeck = currentUser.getDeckByName(deckName)) == null) throw new InvalidDeckNameException(deckName);
        if (!currentDeck.hasCard(currentCard,isSide)) {
            if (isSide) throw new CardNotInDeckException("side",currentCard.getName());
            else throw new CardNotInDeckException("main",currentCard.getName());
        }
        currentUser.getInactiveCards().add(currentCard);
        if (isSide) removeCardFromDeck(currentCard, "Side", currentDeck);
        else removeCardFromDeck(currentCard, "Main", currentDeck);
    }

    public void addCard(String deckName,String cardName,boolean isSide, User currentUser) throws Exception {
        Deck currentDeck;
        Card currentCard;
        if ((currentCard = Card.getCardByName(cardName)) == null) throw new InvalidCardNameException(cardName);
        if ((currentDeck = currentUser.getDeckByName(deckName)) == null) throw new InvalidDeckNameException(deckName);
        currentUser.getInactiveCards().remove(currentCard);
        if (isSide) addCardToDeck(currentCard, "Side", currentDeck);
        else addCardToDeck(currentCard, "Main", currentDeck);
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

    public void addCardToDeck(Card cardToAdd, String deck, Deck currentDeck) throws DeckIsFullException, MaxedOutCardInDeckException {
        switch (deck) {
            case "Main":
                if (mainIsFull(currentDeck)) throw new DeckIsFullException("main");
                if (currentDeck.cardCount(cardToAdd) == 3)
                    throw new MaxedOutCardInDeckException(cardToAdd.getName(),currentDeck.getName());
                currentDeck.getMainCards().add(cardToAdd);
                break;
            case "Side":
                if (sideIsFull(currentDeck)) throw new DeckIsFullException("side");
                if (currentDeck.cardCount(cardToAdd) == 3)
                    throw new MaxedOutCardInDeckException(cardToAdd.getName(),currentDeck.getName());
                currentDeck.getSideCards().add(cardToAdd);
                break;
        }
    }

    public void removeCardFromDeck(Card cardToRemove, String deck, Deck currentDeck) {
        switch (deck) {
            case "Main":
                 for (int i = 0; i < currentDeck.getMainCards().size(); i++) {
                    if (currentDeck.getMainCards().get(i).equals(cardToRemove)) {
                        currentDeck.getMainCards().remove(i);
                        return;
                    }
                }
                break;
            case "Side":
                for (int i = 0; i < currentDeck.getSideCards().size(); i++) {
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

    public boolean isDeckInvalid(Deck deck) {
        int mainCount = deck.getMainCards().size();
        int sideCount = deck.getSideCards().size();
        return mainCount < 40;
    }

    public String deckValidityString(Deck deck) {
        if (isDeckInvalid(deck)) return "Invalid";
        return "Valid";
    }
}