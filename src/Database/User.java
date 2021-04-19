package Database;

import Database.Cards.Card;

import java.util.*;

public class User {
    public static ArrayList<User> users = new ArrayList<>();
    private String username;
    private String password;
    private String nickname;
    private int score;
    private int balance;
    private ArrayList<Deck> decks = new ArrayList<>();
    private Deck activeDeck;
    private ArrayList<Card> inactiveCards = new ArrayList<>();

    public User(String username, String password, String nickname){
        setUsername(username);
        setPassword(password);
        setNickname(nickname);
        setScore(0);
        //TODO fix balance
        setBalance(10000);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(int amount){
        score += amount;
    }

    public void decreaseScore(int amount){
        score -= amount;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public void increaseBalance(int amount){
        balance += amount;
    }

    public void decreaseBalance(int amount){
        balance -= amount;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public Deck getActiveDeck() {
        return activeDeck;
    }

    public void setActiveDeck(Deck activeDeck) {
        this.activeDeck = activeDeck;
    }

    public ArrayList<Card> getInactiveCards() {
        return inactiveCards;
    }

    public void addCard(Card card){
        inactiveCards.add(card);
    }

}
