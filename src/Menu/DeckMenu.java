package Menu;

import Database.User;
import Exceptions.DeckIsFullException;
import Exceptions.InvalidCardNameException;
import Exceptions.InvalidDeckNameException;
import Exceptions.RepetitiveDeckNameException;
import MenuController.ProgramController.Regex;
import MenuController.DeckController;
import View.DeckView;
import View.UserView;

import java.util.regex.Matcher;

public class DeckMenu {
    private User currentUser;
    private final DeckController deckController = new DeckController();
    private final DeckView deckView = new DeckView();
    private final UserView userView = new UserView();

    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.createDeck)).matches()) {
            try {
                deckController.createDeck(matcher, currentUser);
                System.out.println("deck created successfully!");
            } catch (RepetitiveDeckNameException e) {
                System.out.println(e.getMessage());
            }
        } else if ((matcher = Regex.getCommandMatcher(command, Regex.deleteDeck)).matches()) {
            try {
                deckController.deleteDeck(matcher, currentUser);
                System.out.println("deck deleted successfully");
            } catch (InvalidDeckNameException e) {
                System.out.println(e.getMessage());
            }
        } else if ((matcher = Regex.getCommandMatcher(command, Regex.activateDeck)).matches()) {
            try {
                deckController.activateDeck(matcher, currentUser);
                System.out.println("deck activated successfully");
            } catch (InvalidDeckNameException e) {
                System.out.println(e.getMessage());
            }
        } else if ((matcher = Regex.getCommandMatcher(command, Regex.addCardToDeck)).matches()) {
            try {
                deckController.addCard(matcher, currentUser);
                System.out.println("card added to deck successfully");
            } catch (InvalidCardNameException | InvalidDeckNameException | DeckIsFullException e) {
                System.out.println(e.getMessage());
            }
        } else if ((matcher = Regex.getCommandMatcher(command, Regex.removeCardFromDeck)).matches()) {
            try {
                deckController.removeCard(matcher, currentUser);
                System.out.println("card removed form deck successfully");
            } catch (InvalidCardNameException | InvalidDeckNameException | DeckIsFullException e) {
                System.out.println(e.getMessage());
            }
        } else if (Regex.getCommandMatcher(command, Regex.showAllDeck).matches())
            deckView.showAllDecks();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.showOneDeck)).matches())
            deckView.showOneDeck(matcher);
        else if (Regex.getCommandMatcher(command, Regex.showCards).matches())
            userView.showAllCards(currentUser);
        else System.out.println("invalid command");
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
