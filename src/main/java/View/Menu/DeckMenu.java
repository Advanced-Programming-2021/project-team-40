package View.Menu;


import Controller.DatabaseController.DeckController;
import Controller.ProgramController.Regex;
import Database.Deck;
import Database.User;
import View.DeckView;
import View.Exceptions.DeckIsFullException;
import View.Exceptions.InvalidCardNameException;
import View.Exceptions.InvalidDeckNameException;
import View.Exceptions.RepetitiveDeckNameException;
import View.UserView;

import java.util.regex.Matcher;

public class DeckMenu {
    private User currentUser;
    private final DeckView deckView = new DeckView();
    private final UserView userView = new UserView();

    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.createDeck)).matches()) createDeck(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.deleteDeck)).matches()) deleteDeck(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.activateDeck)).matches()) activateDeck(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.addCardToDeck)).matches()) addCard(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.removeCardFromDeck)).matches()) removeCard(matcher);
        else if (Regex.getCommandMatcher(command, Regex.showAllDeck).matches()) userView.showUserDecks(currentUser);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.showOneDeck)).matches()) showOneDeck(matcher);
        else if (Regex.getCommandMatcher(command, Regex.showCards).matches()) userView.showUserCards(currentUser);
        else System.out.println("invalid command");
    }

    private void createDeck(Matcher matcher) {
        String deckName = matcher.group("deckName");
        try {
            DeckController.getInstance().createDeck(deckName, currentUser);
        } catch (RepetitiveDeckNameException e) {
            System.err.println(e.getMessage());
        }
    }

    private void deleteDeck(Matcher matcher) {
        String deckName = matcher.group("deckName");
        try {
            DeckController.getInstance().deleteDeck(deckName, currentUser);
        } catch (InvalidDeckNameException e) {
            System.err.println(e.getMessage());
        }
    }

    private void activateDeck(Matcher matcher) {
        String deckName = matcher.group("deckName");
        try {
            DeckController.getInstance().activateDeck(deckName, currentUser);
        } catch (InvalidDeckNameException e) {
            System.err.println(e.getMessage());
        }
    }

    private void addCard(Matcher matcher) {
        String deckName = matcher.group("deckName");
        String cardName = matcher.group("cardName");
        boolean isSide = true;
        if (matcher.group("isSide") == null) isSide = false;
        try {
            DeckController.getInstance().addCard(deckName, cardName, isSide, currentUser);
        } catch (DeckIsFullException | InvalidCardNameException | InvalidDeckNameException e) {
            System.err.println(e.getMessage());
        }
    }

    private void removeCard(Matcher matcher) {
        String deckName = matcher.group("deckName");
        String cardName = matcher.group("cardName");
        boolean isSide = true;
        if (matcher.group("isSide") == null) isSide = false;
        try {
            DeckController.getInstance().removeCard(deckName, cardName, isSide, currentUser);
        } catch (DeckIsFullException | InvalidCardNameException | InvalidDeckNameException e) {
            System.err.println(e.getMessage());
        }
    }

    public void showOneDeck(Matcher matcher) {
        String deckName = matcher.group("deckName");
        Deck deck;
        if ((deck = currentUser.getDeckByName(deckName)) == null) try {
            throw new InvalidDeckNameException(deckName);
        } catch (InvalidDeckNameException e) {
            System.err.println(e.getMessage());
        }
        else deckView.showDetailedDeck(deck);
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
