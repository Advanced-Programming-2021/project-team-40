package Database;

import Database.Cards.*;

import java.util.*;

public class Deck {
    private String name;
    private ArrayList<Card> mainCards = new ArrayList<>();
    private ArrayList<Card> sideCards = new ArrayList<>();
    private boolean isActive = false;

    public Deck(String name){
        this.name = name;
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
    public Object clone(){
        Deck newDeck = new Deck(this.getName());
        newDeck.setSideCards(this.getSideCards());
        newDeck.setMainCards(this.getMainCards());
        return newDeck;
    }

    public boolean hasCard(Card currentCard,boolean isSide) {
        if (isSide) {
            for (Card card : sideCards
            ) {
                if (card.equals(currentCard)) return true;
            }
        } else {
            for (Card card: mainCards
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
}
