package View.Menu;


import Controller.MenuController.ShopController;
import Controller.ProgramController.Regex;
import Database.User;
import View.Exceptions.InvalidCardNameException;
import View.Exceptions.NotEnoughMoneyException;
import View.ShopView;

import java.util.regex.Matcher;

public class ShopMenu implements Help{
    User currentUser;
    private ShopView shopView;
    private static ShopMenu shopMenu;
    private ShopMenu(){
        shopView = ShopView.getInstance();
    }

    public static ShopMenu getInstance(){
        if (shopMenu == null)
            shopMenu = new ShopMenu();
        return shopMenu;
    }

    public void run(String command) {
        Matcher matcher;
        if (Regex.getCommandMatcher(command,Regex.help).matches()) help();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.shopBuy)).matches()) buy(matcher);
        else if (Regex.getCommandMatcher(command, Regex.shopShowAll).matches()) shopView.showAll();
        else System.err.println("invalid command");
    }

    public void help() {
        System.out.println("menu exit");
        System.out.println("menu show-current");
        System.out.println("shop buy <card name>");
    }

    private void buy(Matcher matcher) {
        String cardName = matcher.group("cardName");
        try {
            ShopController.getInstance().buy(cardName,currentUser);
            System.out.println("card bought successfully");
        } catch (InvalidCardNameException | NotEnoughMoneyException e) {
            System.err.println(e.getMessage());
        }
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}