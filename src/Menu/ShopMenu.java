package Menu;

import Database.Cards.Card;
import Database.User;
import Exceptions.InvalidCardNameException;
import Exceptions.NotEnoughMoneyException;
import MenuController.ProgramController.Regex;
import MenuController.ShopController;
import View.ShopView;

import java.util.regex.Matcher;

public class ShopMenu {
    User currentUser;
    private final ShopController shopController = new ShopController();
    private final ShopView shopView = new ShopView();
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.shopBuy)).matches()) {
            try {
                shopController.buy(matcher,currentUser);
            } catch (InvalidCardNameException | NotEnoughMoneyException e) {
                System.out.println(e.getMessage());
            }
        } else if (Regex.getCommandMatcher(command, Regex.shopShowAll).matches()) {
            shopView.showAll();
        } else System.out.println("invalid command");
    }
}
