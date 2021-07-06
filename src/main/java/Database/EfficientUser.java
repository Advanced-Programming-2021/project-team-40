package Database;

import Database.Cards.Card;

import java.util.ArrayList;

public class EfficientUser {
    private String username;
    private String password;
    private String nickname;
    private String avatarID;
    private int score;
    private int balance;
    private ArrayList<EfficientDeck> decks = new ArrayList<>();
    private ArrayList<String> inactiveCards = new ArrayList<>();

    public EfficientUser (User user){
        setUsername(user.getUsername());
        setPassword(user.getPassword());
        setNickname(user.getNickname());
        setScore(user.getScore());
        setBalance(user.getBalance());
        setDecks(user.getDecks());
        setInactiveCards(user.getInactiveCards());
        setAvatarID(user.getAvatarID());
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarID() {
        return avatarID;
    }

    public void setAvatarID(String avatarID) {
        this.avatarID = avatarID;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public ArrayList<EfficientDeck> getDecks() {
        return decks;
    }

    public void setDecks(ArrayList<Deck> actualDecks) {
        for (Deck actualDeck : actualDecks) {
            EfficientDeck newDeck = new EfficientDeck(actualDeck.getName(), actualDeck.isActive(), actualDeck.getMainCards(), actualDeck.getSideCards());
            decks.add(newDeck);
        };
    }

    public ArrayList<String> getInactiveCards() {
        return inactiveCards;
    }

    public void setInactiveCards(ArrayList<Card> actualInactiveCards) {
        for (Card card: actualInactiveCards) {
            inactiveCards.add(card.getName());
        }
    }
}
