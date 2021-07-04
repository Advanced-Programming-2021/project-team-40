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
    private int rank = 0;
    private ArrayList<Deck> decks = new ArrayList<>();
    private ArrayList<Card> inactiveCards = new ArrayList<>();

    public User(String username, String password, String nickname, int score, int balance, ArrayList<Deck> decks, ArrayList<Card> inactiveCards) {
        setUsername(username.trim());
        setPassword(password.trim());
        setNickname(nickname.trim());
        setScore(score);
        setBalance(balance);
        setDecks(decks);
        this.inactiveCards = inactiveCards;
        users.add(this);
    }

    public static User getUserByName(String username) {
        for (User user : users
        ) {
            if (user.getUsername().equals(username)) return user;
        }
        return null;
    }

    public static User getUserByNickname(String nickname) {
        for (User user : users
        ) {
            if (user.getNickname().equals(nickname)) return user;
        }
        return null;
    }

    public static ArrayList<User> getUsers() {
        if (users.size() > 0) updateRanks();
        return users;
    }

    public static void updateRanks() {
        users.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o2.getScore() - o1.getScore();
            }
        });
        users.get(0).setRank(1);
        for (int i = 1; i < users.size(); i++) {
            if (users.get(i).getScore() < users.get(i - 1).getScore()) {
                users.get(i).setRank(i + 1);
            } else users.get(i).setRank(users.get(i - 1).getRank());
        }
    }

    public static void clearUsers() {
        users.clear();
    }

    public HashMap<String, Integer> getCardHashMap() {
        HashMap<String, Integer> cardHashMap = new HashMap<>();
        for (Card card : Card.getAllCards()) {
            cardHashMap.put(card.getName(), 0);
        }
        for (Deck deck : decks) {
            for (Card card : deck.getMainCards()) {
                cardHashMap.put(card.getName(), cardHashMap.get(card.getName()) + 1);
            }
            for (Card card : deck.getSideCards()) {
                cardHashMap.put(card.getName(), cardHashMap.get(card.getName()) + 1);
            }
        }
        for (Card card : getInactiveCards()) {
            cardHashMap.put(card.getName(), cardHashMap.get(card.getName()) + 1);
        }
        return cardHashMap;
    }

    public Deck getDeckByName(String deckName) {
        for (Deck deck : decks
        ) {
            if (deck.getName().equals(deckName)) return deck;
        }
        return null;
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void increaseScore(int amount) {
        score += amount;
    }

    public void decreaseScore(int amount) {
        score -= amount;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public void increaseBalance(int amount) {
        balance += amount;
    }

    public void decreaseBalance(int amount) {
        balance -= amount;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    public Deck getActiveDeck() {
        for (Deck deck : decks) {
            if (deck.isActive()) return deck;
        }
        return null;
    }

    public void inactivateDeck() {
        getActiveDeck().setActive(false);
    }

    public void setActiveDeck(Deck activeDeck) {
        activeDeck.setActive(true);
    }

    public void setDecks(ArrayList<Deck> decks) {
        this.decks = decks;
    }

    public ArrayList<Card> getInactiveCards() {
        return inactiveCards;
    }

    public void addCard(Card card) {
        inactiveCards.add(card);
    }

}
