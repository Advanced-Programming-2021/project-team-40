package Database.Cards;

import java.util.*;

abstract public class Card {


    protected static ArrayList<Card> allCards = new ArrayList<>();
    protected String name;
    protected String description;
    protected String cardNumber;
    protected int cardPrice;

    public Card(String name, String description, String cardNumber, int cardPrice) {
        setName(name);
        setDescription(description);
        setCardNumber(cardNumber);
        setCardPrice(cardPrice);
    }

    public static Card getCardByName(String name) {
        for (Card card : allCards) {
            if (card.name.matches(name)) return card;
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardPrice(int cardPrice) {
        this.cardPrice = cardPrice;
    }

    public int getCardPrice() {
        return cardPrice;
    }

    abstract public void printCard();
}
