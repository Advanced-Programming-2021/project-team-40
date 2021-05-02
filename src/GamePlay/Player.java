package GamePlay;


import Database.Cards.Card;
import Database.Deck;
import Database.User;

import java.util.ArrayList;

public class Player {
    private User user;
    private Deck playingDeck;
    private int lifePoints = 8000;
    private Field field = new Field();
    private ArrayList<Card> playingHand = new ArrayList<>();
    public Player(User user){
        this.user = user;
        playingDeck = user.getActiveDeck();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Deck getPlayingDeck() {
        return playingDeck;
    }

    public void setPlayingDeck(Deck playingDeck) {
        this.playingDeck = playingDeck;
    }

    public int getLifePoints() {
        return lifePoints;
    }

    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public ArrayList<Card> getPlayingHand() {
        return playingHand;
    }

    public void setPlayingHand(ArrayList<Card> playingHand) {
        this.playingHand = playingHand;
    }
}