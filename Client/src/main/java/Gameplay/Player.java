package Gameplay;


import Controller.MenuController.DeckMenuController;
import Database.Deck;
import Database.User;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private int maxLP = 0;
    private User user;
    private Deck playingDeck;
    private int lifePoints = 8000;
    private Field field = new Field();
    private ArrayList<HandFieldArea> playingHand = new ArrayList<>();
    private HashMap<SpellAndTrapFieldArea, MonsterFieldArea> equippedMonsters = new HashMap<>();

    public Player(User user) {
        this.user = user;
        playingDeck = (Deck) user.getActiveDeck().clone();
        DeckMenuController.getInstance().shuffleDeck(playingDeck);
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

    public int getMaxLP() {
        return maxLP;
    }

    public void setMaxLP(int maxLP) {
        if (maxLP > this.maxLP) this.maxLP = maxLP;
    }

    public HashMap<SpellAndTrapFieldArea, MonsterFieldArea> getEquippedMonsters() {
        return equippedMonsters;
    }
}