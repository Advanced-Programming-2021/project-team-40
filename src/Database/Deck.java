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

    private boolean mainIsFull(){
        if (mainCards.size() >= 60) return true;
        return false;
    }

    private boolean sideIsFull(){
        if (sideCards.size() >= 15) return true;
        return false;
    }

    public void addCard(Card cardToAdd, String deck){
        switch (deck){
            case "Main":
                mainCards.add(cardToAdd);
                break;
            case "Side":
                sideCards.add(cardToAdd);
                break;
        }
    }

    private void removeCard(Card cardToRemove, String deck){
        switch (deck){
            case "Main":
                for (int i = 0; i < mainCards.size(); i++) {
                    if (mainCards.get(i).equals(cardToRemove)){
                        mainCards.remove(i);
                        return;
                    }
                }
                break;
            case "Side":
                for (int i = 0; i < sideCards.size(); i++) {
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


}
