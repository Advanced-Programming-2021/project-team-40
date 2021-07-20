package Controller;

import Controller.DatabaseController.DatabaseController;
import Database.Cards.Card;

import java.util.ArrayList;
import java.util.HashMap;

public class ShopController {
    private static HashMap<String, Integer> cardStock;
    private static ArrayList<String> unavailableCards;

    public static void initializeCardData() {
        cardStock = DatabaseController.getInstance().getCardStock();
        if (cardStock == null) {
            initializeCardStock();
            DatabaseController.getInstance().saveCardStock(cardStock);
        }else {
            System.out.println(cardStock.get("Battle OX"));
        }
        //unavailableCards = DatabaseController.getInstance().getUnavailableCards();
    }

    private static void initializeCardStock() {
        HashMap<String , Integer> stock = new HashMap<>();
        for (Card card : Card.getAllCards()) {
            stock.put(card.getName(), 10);
        }
        cardStock = stock;
    }

    public static ArrayList<String> getUnavailableCards() {
        return unavailableCards;
    }

    public static HashMap<String, Integer> getCardStock() {
        return cardStock;
    }
}
