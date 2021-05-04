package Database;

import Database.Cards.*;

import java.util.*;

public class Deck {
    private String name;
    public ArrayList<Card> mainCards = new ArrayList<>();
    public ArrayList<Card> sideCards = new ArrayList<>();

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
}
