package Menu;

import Database.Cards.Card;
import Database.Deck;
import Database.User;
import ProgramController.Exceptions.DeckIsFullException;
import ProgramController.Exceptions.InvalidCardNameException;
import ProgramController.Exceptions.InvalidDeckNameException;
import ProgramController.Exceptions.RepetitiveDeckNameException;
import ProgramController.Regex;

import java.util.regex.Matcher;

public class DeckMenu {
    User currentUser;

    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.createDeck)).matches()) {
            try {
                createDeck(matcher);
            } catch (RepetitiveDeckNameException e) {
                System.out.println(e.getMessage());
            }
        }
        else if ((matcher = Regex.getCommandMatcher(command, Regex.deleteDeck)).matches()) {
            try {
                deleteDeck(matcher);
            } catch (InvalidDeckNameException e) {
                System.out.println(e.getMessage());
            }
        }
        else if ((matcher = Regex.getCommandMatcher(command, Regex.activateDeck)).matches()) {
            try {
                activateDeck(matcher);
            } catch (InvalidDeckNameException e) {
                System.out.println(e.getMessage());
            }
        }
        else if ((matcher = Regex.getCommandMatcher(command, Regex.addCardToDeck)).matches()) {
            try {
                addCard(matcher);
            } catch (InvalidCardNameException | InvalidDeckNameException | DeckIsFullException e) {
                System.out.println(e.getMessage());
            }
        }
        else if ((matcher = Regex.getCommandMatcher(command, Regex.removeCardFromDeck)).matches())
            removeCard(matcher);
        else if (Regex.getCommandMatcher(command, Regex.showAllDeck).matches())
            showAllDecks();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.showOneDeck)).matches())
            showOneDeck(matcher);
        else if (Regex.getCommandMatcher(command, Regex.showCards).matches())
            showAllCards();

    }

    private void showAllCards() {

    }

    private void showOneDeck(Matcher matcher) {

    }

    private void showAllDecks() {

    }

    private void removeCard(Matcher matcher) {

    }

    private void addCard(Matcher matcher) throws InvalidCardNameException, InvalidDeckNameException, DeckIsFullException {
        String cardName = matcher.group("card_name");
        String deckName = matcher.group("deck_name") ;
        boolean isSide = false;
        if (matcher.group("is_side") != null) isSide = true;
        if (Card.getCardByName(cardName) == null) throw new InvalidCardNameException(cardName);
        else if (currentUser.getDeckByName(deckName) == null) throw new InvalidDeckNameException(deckName);
        else if (isSide) currentUser.getDeckByName(deckName).addCard(Card.getCardByName(cardName),"Side");
        else currentUser.getDeckByName(deckName).addCard(Card.getCardByName(cardName),"Main");
    }

    private void activateDeck(Matcher matcher) throws InvalidDeckNameException {
        String deckName = matcher.group("deck_name");
        if (currentUser.getDeckByName(deckName) == null) throw new InvalidDeckNameException(deckName);
        else {
            Deck deck = currentUser.getDeckByName(deckName);
            currentUser.setActiveDeck(deck);
            //TODO missing success message
        }
    }

    private void deleteDeck(Matcher matcher) throws InvalidDeckNameException {
        String deckName = matcher.group("deck_name");
        if (currentUser.getDeckByName(deckName) == null) throw new InvalidDeckNameException(deckName);
        else{
            Deck deck = currentUser.getDeckByName(deckName);
            currentUser.getDecks().remove(deck);
            //TODO missing success message
        }
    }

    private void createDeck(Matcher matcher) throws RepetitiveDeckNameException {
        String deckName = matcher.group("deck_name");
        if (currentUser.getDeckByName(deckName) != null) throw new RepetitiveDeckNameException(deckName);
        else {
            Deck deck = new Deck(deckName);
            currentUser.getDecks().add(deck);
            //TODO missing success message
        }
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
