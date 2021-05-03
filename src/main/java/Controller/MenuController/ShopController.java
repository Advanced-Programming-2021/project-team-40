package main.java.Controller.MenuController;

import main.java.Database.Cards.Card;
import main.java.Database.User;
import main.java.View.Exceptions.InvalidCardNameException;
import main.java.View.Exceptions.NotEnoughMoneyException;

import java.util.regex.Matcher;

public class ShopController {
    private static ShopController shopController;
    private ShopController() {
    }

    public static ShopController getInstance() {
        if (shopController == null)
            shopController = new ShopController();
        return shopController;
    }
    public void buy(Matcher matcher, User currentUser) throws InvalidCardNameException, NotEnoughMoneyException {
        String cardName = matcher.group("cardName");
        Card card;
        if ((card = Card.getCardByName(cardName)) == null) throw new InvalidCardNameException(cardName);
        if (card.getCardPrice() > currentUser.getBalance()) throw new NotEnoughMoneyException();
        currentUser.setBalance(currentUser.getBalance() - card.getCardPrice());
    }
}