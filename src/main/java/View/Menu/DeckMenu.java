package View.Menu;


import Controller.DatabaseController.DeckController;
import Controller.MenuController.MenuNavigationController;
import Controller.ProgramController.Menu;
import Controller.ProgramController.Regex;
import Database.Cards.Card;
import Database.Deck;
import Database.User;
import View.CardView;
import View.DeckView;
import View.Exceptions.*;
import View.UserView;

import java.util.regex.Matcher;

public class DeckMenu implements Help {
    private static DeckMenu deckMenu;

    private User currentUser;
    private DeckView deckView = new DeckView();
    private DeckMenu() {

    }

    public static DeckMenu getInstance() {
        if (deckMenu == null) deckMenu = new DeckMenu();
        return deckMenu;
    }

    public void run(String command) {
        Matcher matcher;
        if (Regex.getCommandMatcher(command, Regex.help).matches()) help();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.createDeck)).matches()) createDeck(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.deleteDeck)).matches()) deleteDeck(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.activateDeck)).matches()) activateDeck(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.addCardToDeck)).matches()) addCard(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.removeCardFromDeck)).matches()) removeCard(matcher);
        else if (Regex.getCommandMatcher(command, Regex.showAllDeck).matches()) DeckView.showAllDecks(currentUser);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.showOneDeck)).matches()) showOneDeck(matcher);
        else if (Regex.getCommandMatcher(command, Regex.showCards).matches()) UserView.showAllCards(currentUser);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.showCardByName)).matches()) showACard(matcher);
        else if (Regex.getCommandMatcher(command, Regex.exitMenu).matches()) {
            MenuNavigationController.getInstance().toUpperMenu(Menu.DECK_MENU);
        } else System.err.println("invalid command");
    }

    private void showACard(Matcher matcher) {
        String name = matcher.group("cardName");
        try {
            Card card;
            if ((card = Card.getCardByName(name)) == null) throw new InvalidCardNameException(name);
            CardView.showCard(card);
        } catch (InvalidCardNameException e){
            System.err.println(e.getMessage());
        }
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
        } catch (Exception e) {
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
        } catch (DeckIsFullException | InvalidCardNameException | InvalidDeckNameException | CardNotInDeckException e) {
            System.err.println(e.getMessage());
        }
    }

    public void showOneDeck(Matcher matcher) {
        String deckName = matcher.group("deckName");
        Deck deck;
        boolean isSide = (matcher.group("isSide") != null);
        if ((deck = currentUser.getDeckByName(deckName)) == null) try {
            throw new InvalidDeckNameException(deckName);
        } catch (InvalidDeckNameException e) {
            System.err.println(e.getMessage());
        }
        else deckView.showDetailedDeck(deck, isSide);
    }

    public void help() {
        System.out.println("deck create <deck name>");
        System.out.println("deck delete <deck name>");
        System.out.println("deck set-activate <deck name>");
        System.out.println("deck add-card --card <card name> --deck <deck name> --side(optional)");
        System.out.println("deck rm-card --card <card name> --deck <deck name> --side(optional)");
        System.out.println("deck show --all");
        System.out.println("deck show --deck-name <deck name> --side(Opt)");
        System.out.println("deck show --cards");
        System.out.println("menu show-current");
        System.out.println("help");
        System.out.println("menu exit");
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
