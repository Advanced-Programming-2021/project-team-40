package Gameplay;


import Controller.DatabaseController.DeckController;
import Database.Deck;
import Database.User;

import java.util.ArrayList;

public class Player {
    private User user;
    private Deck playingDeck;
    private int lifePoints = 8000;
    private Field field = new Field();
    private ArrayList<HandFieldArea> playingHand = new ArrayList<>();
    public Player(User user){
        this.user = user;
        playingDeck = (Deck) user.getActiveDeck().clone();
        DeckController.getInstance().shuffleDeck(user.getActiveDeck());
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