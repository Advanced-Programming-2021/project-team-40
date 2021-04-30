package Menu;

import Database.Cards.Card;
import Database.User;
import Exceptions.InvalidCardNameException;
import Exceptions.NotEnoughMoneyException;
import ProgramController.Regex;

import java.util.regex.Matcher;

public class ShopMenu {
    User currentUser;

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.shopBuy)).matches()) {
            try {
                buy(matcher);
            } catch (InvalidCardNameException | NotEnoughMoneyException e) {
                System.out.println(e.getMessage());
            }
        } else if (Regex.getCommandMatcher(command, Regex.shopShowAll).matches()) {
            showAll();
        } else System.out.println("invalid command");
    }

    private void showAll() {

    }

    private void buy(Matcher matcher) throws InvalidCardNameException, NotEnoughMoneyException {
        String cardName = matcher.group("card_name");
        if (Card.getCardByName(cardName) == null) throw new InvalidCardNameException(cardName);
        else {
            Card card = Card.getCardByName(cardName);
            if (card.getCardPrice() > currentUser.getBalance()) throw new NotEnoughMoneyException();
            else currentUser.setBalance(currentUser.getBalance() - card.getCardPrice());
        }
    }
}
