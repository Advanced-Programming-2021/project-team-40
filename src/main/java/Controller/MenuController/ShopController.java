package Controller.MenuController;

import Controller.ProgramController.Menu;
import Controller.ProgramController.ProgramController;
import Database.Cards.Card;
import Database.User;
import View.Exceptions.InvalidCardNameException;
import View.Exceptions.MenuNavigationNotPossibleException;
import View.Exceptions.NotEnoughMoneyException;
import View.Menu.ShopMenu;

import java.util.regex.Matcher;

public class ShopController implements MenuNavigation{
    private static ShopController shopController;
    private ShopController() {
    }

    public static ShopController getInstance() {
        if (shopController == null)
            shopController = new ShopController();
        return shopController;
    }
    public void buy(String cardName, User currentUser) throws InvalidCardNameException, NotEnoughMoneyException {
        Card card;
        if ((card = Card.getCardByName(cardName)) == null) throw new InvalidCardNameException(cardName);
        if (card.getCardPrice() > currentUser.getBalance()) throw new NotEnoughMoneyException();
        currentUser.setBalance(currentUser.getBalance() - card.getCardPrice());
        currentUser.getInactiveCards().add(card);
    }

    public void toUpperMenu() {
        ProgramController.getInstance().setCurrentMenu(Menu.MAIN_MENU);
    }

    public void toLowerMenu(String menuName) {
        try {
            throw new MenuNavigationNotPossibleException();
        } catch (MenuNavigationNotPossibleException e) {
            System.out.println(e.getMessage());
        }
    }

    public void increaseMoneyCheat(Matcher matcher){
        int amount = Integer.parseInt(matcher.group("amount"));
        ShopMenu.getInstance().getCurrentUser().increaseBalance(amount);
    }
}