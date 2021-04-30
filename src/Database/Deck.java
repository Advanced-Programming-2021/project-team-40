package Database;

import Database.Cards.*;
import Exceptions.DeckIsFullException;

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

    private boolean mainIsFull(){
        if (mainCards.size() >= 60) return true;
        return false;
    }

    private boolean sideIsFull(){
        if (sideCards.size() >= 15) return true;
        return false;
    }

    public void addCard(Card cardToAdd, String deck) throws DeckIsFullException {
        switch (deck){
            case "Main":
                if (mainIsFull()) throw new DeckIsFullException("main");
                else {
                    mainCards.add(cardToAdd);
                    //TODO missing success message
                }
                break;
            case "Side":
                if (sideIsFull()) throw new DeckIsFullException("side");
                else {
                    sideCards.add(cardToAdd);
                    //TODO missing success message
                }
                break;
        }
    }

    public void removeCard(Card cardToRemove, String deck) throws DeckIsFullException {
        switch (deck){
            case "Main":
                if (mainIsFull()) throw new DeckIsFullException("main");
                else for (int i = 0; i < mainCards.size(); i++) {
                    if (mainCards.get(i).equals(cardToRemove)){
                        mainCards.remove(i);
                        return;
                    }
                }
                break;
            case "Side":
                if (mainIsFull()) throw new DeckIsFullException("side");
                else for (int i = 0; i < sideCards.size(); i++) {
                    if (sideCards.get(i).equals(cardToRemove)){
                        sideCards.remove(i);
                        return;
                    }
                }
                break;
        }
    }

    public void shuffleDeck(){
        Collections.shuffle(mainCards);
        Collections.shuffle(sideCards);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
