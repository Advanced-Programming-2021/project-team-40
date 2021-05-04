package View.Menu;


import Controller.MenuController.ShopController;
import Controller.ProgramController.Regex;
import Database.User;
import View.Exceptions.InvalidCardNameException;
import View.Exceptions.NotEnoughMoneyException;
import View.ShopView;

import java.util.regex.Matcher;

public class ShopMenu {
    User currentUser;
    private final ShopView shopView = new ShopView();

    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.shopBuy)).matches()) buy(matcher);
        else if (Regex.getCommandMatcher(command, Regex.shopShowAll).matches()) shopView.showAll();
        else System.out.println("invalid command");
    }

    private void buy(Matcher matcher) {
        String cardName = matcher.group("cardName");
        try {
            ShopController.getInstance().buy(cardName,currentUser);
        } catch (InvalidCardNameException | NotEnoughMoneyException e) {
            System.err.println(e.getMessage());
        }
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}