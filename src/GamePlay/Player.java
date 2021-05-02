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
}