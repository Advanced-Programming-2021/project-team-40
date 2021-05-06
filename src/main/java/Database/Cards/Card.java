package Database.Cards;

import java.util.*;

abstract public class Card {

    protected String name;
    protected String description;
    protected int cardPrice = 0;

    public Card(String name, String description, int cardPrice) {
        setName(name);
        setDescription(description);
        setCardPrice(cardPrice);
    }

    public static Card getCardByName(String name) {
        for (Card card : getAllCards()) {
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

    public void setCardPrice(int cardPrice) {
        this.cardPrice = cardPrice;
    }

    public int getCardPrice() {
        return cardPrice;
    }

    public static ArrayList<Card> getAllCards() {
        ArrayList<Card> allCards = new ArrayList<>();
        allCards.addAll(Monster.getMonsters());
        allCards.addAll(Trap.getTraps());
        allCards.addAll(Spell.getSpells());
        return allCards;
    }
}
