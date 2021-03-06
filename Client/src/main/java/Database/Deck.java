package Database;

import Database.Cards.*;

import java.util.*;

public class Deck {
    private String name;
    private ArrayList<Card> mainCards = new ArrayList<>();
    private ArrayList<Card> sideCards = new ArrayList<>();
    private boolean isActive = false;

    public Deck(String name) {
        this.name = name;
    }

    public Deck (String name, boolean isActive){
        this.name = name;
        this.isActive = isActive;
    }

    public ArrayList<Card> getMainCards() {
        return mainCards;
    }

    public ArrayList<Card> getSideCards() {
        return sideCards;
    }

    public String getName() {
        return name;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSideCards(ArrayList<Card> sideCards) {
        this.sideCards = sideCards;
    }

    public void setMainCards(ArrayList<Card> mainCards) {
        this.mainCards = mainCards;
    }

    @Override
    public Object clone() {
        Deck newDeck = new Deck(this.getName());
        for (Card card : mainCards) {
            newDeck.getMainCards().add(card);
        }
        for (Card card : sideCards) {
            newDeck.getSideCards().add(card);
        }
        return newDeck;
    }

    public boolean hasCard(Card currentCard, boolean isSide) {
        if (isSide) {
            for (Card card : sideCards
            ) {
                if (card.equals(currentCard)) return true;
            }
        } else {
            for (Card card : mainCards
            ) {
                if (card.equals(currentCard)) return true;
            }
        }
        return false;
    }

    public int cardCount(Card card) {
        int count = 0;
        for (Card c :
                mainCards) {
            if (card.equals(c)) count++;
        }
        for (Card c :
                sideCards) {
            if (card.equals(c)) count++;
        }
        return count;
    }

    public static Comparator<Deck> deckNameComparator = new Comparator<Deck>() {
        @Override
        public int compare(Deck firstDeck, Deck secondDeck) {
            String first = firstDeck.getName().toUpperCase();
            String second = secondDeck.getName().toUpperCase();
            return first.compareTo(second);
        }
    };
}
