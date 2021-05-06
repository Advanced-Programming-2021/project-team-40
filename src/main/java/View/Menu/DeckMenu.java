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

public class DeckMenu implements Help{
    private User currentUser;
    private final DeckView deckView = new DeckView();
    private final UserView userView = new UserView();

    public void run(String command) {
        Matcher matcher;
        if (Regex.getCommandMatcher(command,Regex.help).matches()) help();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.createDeck)).matches()) createDeck(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.deleteDeck)).matches()) deleteDeck(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.activateDeck)).matches()) activateDeck(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.addCardToDeck)).matches()) addCard(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.removeCardFromDeck)).matches()) removeCard(matcher);
        else if (Regex.getCommandMatcher(command, Regex.showAllDeck).matches()) userView.showUserDecks(currentUser);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.showOneDeck)).matches()) showOneDeck(matcher);
        else if (Regex.getCommandMatcher(command, Regex.showCards).matches()) userView.showUserCards(currentUser);
        else System.err.println("invalid command");
    }

    private void createDeck(Matcher matcher) {
        String deckName = matcher.group("deckName");
        try {
            DeckController.getInstance().createDeck(deckName, currentUser);
            System.out.println("deck created successfully!");
        } catch (RepetitiveDeckNameException e) {
            System.err.println(e.getMessage());
        }
    }

    private void deleteDeck(Matcher matcher) {
        String deckName = matcher.group("deckName");
        try {
            DeckController.getInstance().deleteDeck(deckName, currentUser);
            System.out.println("deck deleted successfully!");
        } catch (InvalidDeckNameException e) {
            System.err.println(e.getMessage());
        }
    }

    private void activateDeck(Matcher matcher) {
        String deckName = matcher.group("deckName");
        try {
            DeckController.getInstance().activateDeck(deckName, currentUser);
            System.out.println("deck activated successfully");
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
            System.out.println("card added to deck successfully");
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
            System.out.println("card removed form deck successfully");
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
    public void help() {
        System.out.println("menu exit");
        System.out.println("menu show-current");
        System.out.println("deck create <deck name>");
        System.out.println("deck delete <deck name>");
        System.out.println("deck set-activate <deck name>");
        System.out.println("deck add-card --card <card name> --deck <deck name> --side(optional)");
        System.out.println("deck rm-card --card <card name> --deck <deck name> --side(optional)");
        System.out.println("deck show --all");
        System.out.println("deck show --deck-name <deck name> --side(Opt)");
        System.out.println("deck show --cards");
        System.out.println("help");
    }
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
