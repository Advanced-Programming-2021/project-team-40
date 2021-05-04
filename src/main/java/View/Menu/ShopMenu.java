package View.Menu;

import Database.User;
import Controller.ProgramController.Regex;
import Controller.MenuController.ShopController;
import View.Exceptions.InvalidCardNameException;
import View.Exceptions.NotEnoughMoneyException;
import View.ShopView;

import java.util.regex.Matcher;

public class ShopMenu {
    User currentUser;
    private final ShopView shopView = new ShopView();

    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.shopBuy)).matches()) {
            try {
                ShopController.getInstance().buy(matcher, currentUser);
            } catch (InvalidCardNameException | NotEnoughMoneyException e) {
                System.out.println(e.getMessage());
            }
        } else if (Regex.getCommandMatcher(command, Regex.shopShowAll).matches()) {
            shopView.showAll();
        } else System.out.println("invalid command");
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}