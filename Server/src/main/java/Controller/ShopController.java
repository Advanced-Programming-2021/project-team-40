package Controller;

import Controller.DatabaseController.DatabaseController;
import Controller.Exceptions.*;
import Database.Cards.Card;
import Database.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

public class ShopController {
    private static HashMap<String, Integer> cardStock;
    private static ArrayList<String> unavailableCards = new ArrayList<>();
    private static String adminToken;

    public static ArrayList<String> getUnavailableCards() {
        return unavailableCards;
    }

    public static HashMap<String, Integer> getCardStock() {
        return cardStock;
    }

    public static void setCardStock(HashMap<String, Integer> cardStock) {
        ShopController.cardStock = cardStock;
    }

    public synchronized static void buyCard(String cardName, User currentUser) throws Exception {
        Card card;
        if ((card = Card.getCardByName(cardName)) == null) throw new InvalidCardNameException(cardName);
        if (card.getCardPrice() > currentUser.getBalance()) throw new NotEnoughMoneyException();
        if ((cardStock.get(cardName) == 0)) throw new Exception("card is out of stock");
        if ((unavailableCards.contains(cardName))) throw new Exception("admin doesn't allow buying this card");
        cardStock.put(cardName, cardStock.get(cardName) - 1);
        currentUser.setBalance(currentUser.getBalance() - card.getCardPrice());
        currentUser.getInactiveCards().add(card);
        DatabaseController.getInstance().saveUser(currentUser);
        DatabaseController.getInstance().updateCardStock();
    }

    public synchronized static void sellCard(String cardName, User currentUser) throws Exception {
        Card card;
        if ((card = Card.getCardByName(cardName)) == null) throw new InvalidCardNameException(cardName);
        cardStock.put(cardName, cardStock.get(cardName) + 1);
        currentUser.setBalance(currentUser.getBalance() + card.getCardPrice());
        currentUser.getInactiveCards().remove(card);
        DatabaseController.getInstance().saveUser(currentUser);
        DatabaseController.getInstance().updateCardStock();
    }

    public synchronized static void restock(String cardName, int amount) throws Exception {
        Card card;
        if ((card = Card.getCardByName(cardName)) == null) throw new InvalidCardNameException(cardName);
        if (amount == -1 && cardStock.get(cardName) == 0) return;
        cardStock.put(cardName, cardStock.get(cardName) + amount);
        DatabaseController.getInstance().updateCardStock();
    }

    public static void toggle(String cardName) throws Exception {
        if (Card.getCardByName(cardName) == null) throw new InvalidCardNameException(cardName);
        if (unavailableCards.contains(cardName)) unavailableCards.remove(cardName);
        else unavailableCards.add(cardName);
    }

    public static String getAdminToken() {
        return adminToken;
    }

    public static void setAdminToken(String adminToken) {
        ShopController.adminToken = adminToken;
    }
}
