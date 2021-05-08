package View.Menu;

import Controller.MenuController.ShopController;
import Controller.ProgramController.Menu;
import Controller.ProgramController.Regex;
import Database.Cards.Card;
import Database.User;
import View.CardView;
import View.Exceptions.InvalidCardNameException;
import View.Exceptions.NotEnoughMoneyException;
import View.ShopView;

import java.util.regex.Matcher;

public class ShopMenu implements Help {
    User currentUser;
    private ShopView shopView;
    private static ShopMenu shopMenu;

    private ShopMenu() {
        shopView = ShopView.getInstance();
    }

    public static ShopMenu getInstance() {
        if (shopMenu == null)
            shopMenu = new ShopMenu();
        return shopMenu;
    }

    public void run(String command) {
        Matcher matcher;
        if (Regex.getCommandMatcher(command, Regex.help).matches()) help();
        else if (command.matches(Regex.showCurrentMenu)) System.out.println(Menu.SHOP_MENU.toString());
        else if (command.matches(Regex.exitMenu)) ShopController.getInstance().toUpperMenu();
        else if (command.matches(Regex.menuNavigation)) ShopController.getInstance().toLowerMenu("");
        else if ((matcher = Regex.getCommandMatcher(command, Regex.shopBuy)).matches()) buy(matcher);
        else if (Regex.getCommandMatcher(command, Regex.shopShowAll).matches()) shopView.showAll();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.showCardByName)).matches()) showACard(matcher);
        else System.out.println("invalid command");
    }

    public void help() {
        System.out.println("shop show --all");
        System.out.println("shop buy <card name>");
        System.out.println("card show <card name>");
        System.out.println("menu show-current");
        System.out.println("help");
        System.out.println("menu exit");
    }

    private void showACard(Matcher matcher) {
        String name = matcher.group("cardName");
        try {
            Card card;
            if ((card = Card.getCardByName(name)) == null) throw new InvalidCardNameException(name);
            CardView.showCard(card);
        } catch (InvalidCardNameException e) {
            System.out.println(e.getMessage());
        }
    }

    private void buy(Matcher matcher) {
        String cardName = matcher.group("cardName");
        try {
            ShopController.getInstance().buy(cardName, currentUser);
            System.out.println("card bought successfully");
        } catch (InvalidCardNameException | NotEnoughMoneyException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}