package MenuController;

import Database.Cards.Card;
import Database.Deck;
import Database.User;
import Exceptions.DeckIsFullException;
import Exceptions.InvalidCardNameException;
import Exceptions.InvalidDeckNameException;
import Exceptions.RepetitiveDeckNameException;

import java.util.regex.Matcher;

public class DeckController {

    public void removeCard(Matcher matcher, User currentUser) throws DeckIsFullException, InvalidCardNameException, InvalidDeckNameException {
        String cardName = matcher.group("card_name");
        String deckName = matcher.group("deck_name");
        boolean isSide = false;
        if (matcher.group("is_side") != null) isSide = true;
        if (Card.getCardByName(cardName) == null) throw new InvalidCardNameException(cardName);
        if (currentUser.getDeckByName(deckName) == null) throw new InvalidDeckNameException(deckName);
        if (isSide) currentUser.getDeckByName(deckName).removeCard(Card.getCardByName(cardName), "Side");
        else currentUser.getDeckByName(deckName).removeCard(Card.getCardByName(cardName), "Main");
    }

    public void addCard(Matcher matcher, User currentUser) throws InvalidCardNameException, InvalidDeckNameException, DeckIsFullException {
        String cardName = matcher.group("card_name");
        String deckName = matcher.group("deck_name");
        boolean isSide = false;
        if (matcher.group("is_side") != null) isSide = true;
        if (Card.getCardByName(cardName) == null) throw new InvalidCardNameException(cardName);
        if (currentUser.getDeckByName(deckName) == null) throw new InvalidDeckNameException(deckName);
        if (isSide) currentUser.getDeckByName(deckName).addCard(Card.getCardByName(cardName), "Side");
        else currentUser.getDeckByName(deckName).addCard(Card.getCardByName(cardName), "Main");
    }

    public void activateDeck(Matcher matcher, User currentUser) throws InvalidDeckNameException {
        String deckName = matcher.group("deck_name");
        if (currentUser.getDeckByName(deckName) == null) throw new InvalidDeckNameException(deckName);
        Deck deck = currentUser.getDeckByName(deckName);
        currentUser.setActiveDeck(deck);
    }

    public void deleteDeck(Matcher matcher, User currentUser) throws InvalidDeckNameException {
        String deckName = matcher.group("deck_name");
        if (currentUser.getDeckByName(deckName) == null) throw new InvalidDeckNameException(deckName);
        Deck deck = currentUser.getDeckByName(deckName);
        currentUser.getDecks().remove(deck);
    }

    public void createDeck(Matcher matcher, User currentUser) throws RepetitiveDeckNameException {
        String deckName = matcher.group("deck_name");
        if (currentUser.getDeckByName(deckName) != null) throw new RepetitiveDeckNameException(deckName);
        Deck deck = new Deck(deckName);
        currentUser.getDecks().add(deck);
    }
}
