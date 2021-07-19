package Database;

import Database.Cards.Card;

import java.util.ArrayList;
import java.util.Comparator;

public class EfficientUser {
    private String username;
    private String password;
    private String nickname;
    private String avatarID;
    private int score;
    private int balance;
    private int rank = 0;
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

    public static void updateRanks(ArrayList<EfficientUser> listToRank) {
        listToRank.sort(new Comparator<EfficientUser>() {
            @Override
            public int compare(EfficientUser o1, EfficientUser o2) {
                return o2.getScore() - o1.getScore();
            }
        });
        listToRank.get(0).setRank(1);
        for (int i = 1; i < listToRank.size(); i++) {
            if (listToRank.get(i).getScore() < listToRank.get(i - 1).getScore()) {
                listToRank.get(i).setRank(i + 1);
            } else listToRank.get(i).setRank(listToRank.get(i - 1).getRank());
        }
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

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
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
