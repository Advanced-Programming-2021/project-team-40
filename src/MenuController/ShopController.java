package MenuController;

import Database.Cards.Card;
import Database.User;
import Exceptions.InvalidCardNameException;
import Exceptions.NotEnoughMoneyException;

import java.util.regex.Matcher;

public class ShopController {
    public void buy(Matcher matcher, User currentUser) throws InvalidCardNameException, NotEnoughMoneyException {
        String cardName = matcher.group("card_name");
        if (Card.getCardByName(cardName) == null) throw new InvalidCardNameException(cardName);
        Card card = Card.getCardByName(cardName);
        if (card.getCardPrice() > currentUser.getBalance()) throw new NotEnoughMoneyException();
        currentUser.setBalance(currentUser.getBalance() - card.getCardPrice());
    }
}
