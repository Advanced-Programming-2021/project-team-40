package main.java.Gameplay;


import main.java.Database.Cards.Card;
import main.java.Database.Deck;
import main.java.Database.User;

import java.util.ArrayList;

public class Player {
    private User user;
    private Deck playingDeck;
    private int lifePoints = 8000;
    private Field field = new Field();
    private ArrayList<HandFieldArea> playingHand = new ArrayList<>();
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

    public ArrayList<HandFieldArea> getPlayingHand() {
        return playingHand;
    }

}