package Database;

import Database.Cards.*;
import View.Exceptions.DeckIsFullException;

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

    public void setMainCards(ArrayList<Card> mainCards) {
        this.mainCards = mainCards;
    }

    public void setSideCards(ArrayList<Card> sideCards) {
        this.sideCards = sideCards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public Object clone(){
        Deck deck = new Deck(this.name);
        deck.setMainCards(this.mainCards);
        deck.setSideCards(this.sideCards);
        return deck;
    }
}
