package main.java.View.Menu;

import main.java.Database.Deck;
import main.java.Database.User;
import main.java.View.Exceptions.DeckIsFullException;
import main.java.View.Exceptions.InvalidCardNameException;
import main.java.View.Exceptions.InvalidDeckNameException;
import main.java.View.Exceptions.RepetitiveDeckNameException;
import main.java.Controller.ProgramController.Regex;
import main.java.Controller.DatabaseController.DeckController;
import main.java.View.DeckView;
import main.java.View.UserView;

import java.util.regex.Matcher;

public class DeckMenu {
    private User currentUser;
    private final DeckView deckView = new DeckView();
    private final UserView userView = new UserView();

    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.createDeck)).matches()) {
            try {
                DeckController.getInstance().createDeck(matcher, currentUser);
                System.out.println("deck created successfully!");
            } catch (RepetitiveDeckNameException e) {
                System.out.println(e.getMessage());
            }
        } else if ((matcher = Regex.getCommandMatcher(command, Regex.deleteDeck)).matches()) {
            try {
                DeckController.getInstance().deleteDeck(matcher, currentUser);
                System.out.println("deck deleted successfully");
            } catch (InvalidDeckNameException e) {
                System.out.println(e.getMessage());
            }
        } else if ((matcher = Regex.getCommandMatcher(command, Regex.activateDeck)).matches()) {
            try {
                DeckController.getInstance().activateDeck(matcher, currentUser);
                System.out.println("deck activated successfully");
            } catch (InvalidDeckNameException e) {
                System.out.println(e.getMessage());
            }
        } else if ((matcher = Regex.getCommandMatcher(command, Regex.addCardToDeck)).matches()) {
            try {
                DeckController.getInstance().addCard(matcher, currentUser);
                System.out.println("card added to deck successfully");
            } catch (InvalidCardNameException | InvalidDeckNameException | DeckIsFullException e) {
                System.out.println(e.getMessage());
            }
        } else if ((matcher = Regex.getCommandMatcher(command, Regex.removeCardFromDeck)).matches()) {
            try {
                DeckController.getInstance().removeCard(matcher, currentUser);
                System.out.println("card removed form deck successfully");
            } catch (InvalidCardNameException | InvalidDeckNameException | DeckIsFullException e) {
                System.out.println(e.getMessage());
            }
        } else if (Regex.getCommandMatcher(command, Regex.showAllDeck).matches())
            userView.showUserDecks(currentUser);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.showOneDeck)).matches()) {
            try {
                showOneDeck(matcher);
            } catch (InvalidDeckNameException e) {
                System.out.println(e.getMessage());
            }
        } else if (Regex.getCommandMatcher(command, Regex.showCards).matches())
            userView.showUserCards(currentUser);
        else System.out.println("invalid command");
    }

    public void showOneDeck(Matcher matcher) throws InvalidDeckNameException {
        String deckName = matcher.group("deckName");
        Deck deck;
        if ((deck = currentUser.getDeckByName(deckName)) == null) throw new InvalidDeckNameException(deckName);
        deckView.showDetailedDeck(deck);
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
