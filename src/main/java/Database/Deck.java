package Database;

import Database.Cards.*;

import java.util.*;

public class Deck {
    private String name;
    private ArrayList<Card> mainCards = new ArrayList<>();
    private ArrayList<Card> sideCards = new ArrayList<>();

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
}
