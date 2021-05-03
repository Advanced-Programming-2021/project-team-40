package main.java.View.Menu;

import main.java.Database.User;
import main.java.View.Exceptions.InvalidCardNameException;
import main.java.View.Exceptions.NotEnoughMoneyException;
import main.java.Controller.ProgramController.Regex;
import main.java.Controller.MenuController.ShopController;
import main.java.View.ShopView;

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