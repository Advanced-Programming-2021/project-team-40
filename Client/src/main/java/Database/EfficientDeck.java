package Database;

import Database.Cards.Card;

import java.util.ArrayList;

public class EfficientDeck {
    private String name;
    private boolean isActive;
    private ArrayList<String> mainCards = new ArrayList<>();
    private ArrayList<String> sideCards = new ArrayList<>();

    public EfficientDeck(String name, boolean isActive, ArrayList<Card> actualMainCards, ArrayList<Card> actualSideCards) {
        this.setName(name);
        this.setActive(isActive);
        this.setMainCards(cardArrayToStringArray(actualMainCards));
        setSideCards(cardArrayToStringArray(actualSideCards));

    }

    private ArrayList<String> cardArrayToStringArray(ArrayList<Card> actualCards) {
        ArrayList<String> cards = new ArrayList<>();
        if (actualCards.isEmpty()) return cards;
        for (Card card : actualCards) {
            cards.add(card.getName());
        }
        return cards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getMainCards() {
        return mainCards;
    }

    public void setMainCards(ArrayList<String> mainCards) {
        this.mainCards = mainCards;
    }

    public ArrayList<String> getSideCards() {
        return sideCards;
    }

    public void setSideCards(ArrayList<String> sideCards) {
        this.sideCards = sideCards;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
