package Controller.DatabaseController;

import Controller.ShopController;
import Database.*;
import Database.Cards.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class DatabaseController {

    private static DatabaseController databaseController;

    private DatabaseController() {
        initialize();
    }

    public static DatabaseController getInstance() {
        if (databaseController == null)
            databaseController = new DatabaseController();
        return databaseController;
    }

    private void initialize() {
        initializeMonsterCards();
        initializeSpellAndTrapCards();
        initializeUsers();
        initializeChat();
        initializeCardStock();
        initializeUnavailableCards();
    }

    public void saveUser(User user) {
        EfficientUser efficientUser = new EfficientUser(user);
        saveEfficientUser(efficientUser);
    }

    public void saveEfficientUser(EfficientUser efficientUser) {
        File userFile = new File("./Server/src/main/resources/Users/" + efficientUser.getUsername() + ".json");
        try {
            userFile.createNewFile();
            Gson gson = new GsonBuilder().create();
            String writeToFile = gson.toJson(efficientUser);
            FileWriter fw = new FileWriter(userFile);
            BufferedWriter out = new BufferedWriter(fw);
            out.write(writeToFile);
            out.close();
            fw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void initializeSpellAndTrapCards() {
        try {
            File spellAndTrapCards = new File("./Server/src/main/resources/Cards/SpellTrap.csv");
            FileReader fileReader = new FileReader(spellAndTrapCards);
            CSVReader csvReader = new CSVReader(fileReader);
            csvReader.readNext();
            String[] cardDetails;
            while ((cardDetails = csvReader.readNext()) != null) {
                switch (cardDetails[1]) {
                    case "Trap":
                        new Trap(cardDetails[0], getIcon(cardDetails[2]), cardDetails[3], isLimited(cardDetails[4]), Integer.parseInt(cardDetails[5]));
                        break;
                    case "Spell":
                        new Spell(cardDetails[0], getIcon(cardDetails[2]), cardDetails[3], isLimited(cardDetails[4]), Integer.parseInt(cardDetails[5]));
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        initializeTrapEffects();
        initializeSpellEffects();
    }

    private void initializeMonsterCards() {
        try {
            File monsterCards = new File("./Server/src/main/resources/Cards/Monster.csv");
            FileReader fileReader = new FileReader(monsterCards);
            CSVReader csvReader = new CSVReader(fileReader);
            csvReader.readNext();
            String[] cardDetails;
            while ((cardDetails = csvReader.readNext()) != null) {
                new Monster(cardDetails[0], Integer.parseInt(cardDetails[1]), getAttribute(cardDetails[2]), getMonsterType(cardDetails[3]),
                        getCardType(cardDetails[4]), Integer.parseInt(cardDetails[5]), Integer.parseInt(cardDetails[6]), cardDetails[7], Integer.parseInt(cardDetails[8]));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        initializeMonsterCardEffects();
    }

    private void initializeTrapEffects() {
    }

    private void initializeMonsterCardEffects() {
    }

    private void initializeSpellEffects() {
    }

    private boolean isLimited(String string) {
        if (string.matches("Limited")) return true;
        return false;
    }

    private Icon getIcon(String name) {
        name = name.toUpperCase();
        for (int i = 0; i < Icon.values().length; i++) {
            if (name.matches(Icon.values()[i].toString())) return Icon.values()[i];
        }
        return null;
    }

    private Attribute getAttribute(String name) {
        name = name.toUpperCase();
        for (int i = 0; i < Attribute.values().length; i++) {
            if (name.matches(Attribute.values()[i].toString())) return Attribute.values()[i];
        }
        return null;
    }

    private MonsterType getMonsterType(String name) {
        name = name.toUpperCase();
        for (int i = 0; i < MonsterType.values().length; i++) {
            if (name.matches(MonsterType.values()[i].toString())) return MonsterType.values()[i];
        }
        return null;
    }

    private CardType getCardType(String name) {
        name = name.toUpperCase();
        for (int i = 0; i < CardType.values().length; i++) {
            if (name.matches(CardType.values()[i].toString())) return CardType.values()[i];
        }
        return null;
    }

    public void saveChat() {
        File chatFile = new File("./Server/src/main/resources/Chat/Chat.json");
        try {
            chatFile.createNewFile();
            Gson gson = new GsonBuilder().create();
            String writeToFile = gson.toJson(Message.messageList);
            FileWriter fw = new FileWriter(chatFile);
            BufferedWriter out = new BufferedWriter(fw);
            out.write(writeToFile);
            out.close();
            fw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void initializeChat() {
        File chatFolder = new File("./Server/src/main/resources/Chat");
        chatFolder.mkdir();
        File chatFile = new File("./Server/src/main/resources/Chat/Chat.json");
        try {
            Scanner fileScanner = new Scanner(chatFile);
            String chatJson = fileScanner.nextLine();
            Type messageList = new TypeToken<List<Message>>() {
            }.getType();
            Gson gson = new Gson();
            Message.messageList = gson.fromJson(chatJson, messageList);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    public void initializeCardStock() {
        File cardStockFile = new File("./Server/src/main/resources/Cards/cardStock.json");
        try {
            Scanner fileScanner = new Scanner(cardStockFile);
            String cardStockJson = fileScanner.nextLine();
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            ShopController.setCardStock(gson.fromJson(cardStockJson, new TypeToken<HashMap<String, Integer>>() {
            }.getType()));

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateCardStock() {
        File cardStockFile = new File("./Server/src/main/resources/Cards/cardStock.json");
        try {
            cardStockFile.createNewFile();
            Gson gson = new GsonBuilder().create();
            String writeToFile = gson.toJson(ShopController.getCardStock());
            FileWriter fw = new FileWriter(cardStockFile);
            BufferedWriter out = new BufferedWriter(fw);
            out.write(writeToFile);
            out.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeUnavailableCards() {
        File unavailableCards = new File("./Server/src/main/resources/Cards/unavailableCards.json");
        try {
            Scanner fileScanner = new Scanner(unavailableCards);
            String cardStockJson = fileScanner.nextLine();
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            ShopController.setUnavailableCards(gson.fromJson(cardStockJson, new TypeToken<ArrayList<String>>() {
            }.getType()));

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateUnavailableCards() {
        File cardStockFile = new File("./Server/src/main/resources/Cards/unavailableCards.json");
        try {
            cardStockFile.createNewFile();
            Gson gson = new GsonBuilder().create();
            String writeToFile = gson.toJson(ShopController.getUnavailableCards());
            FileWriter fw = new FileWriter(cardStockFile);
            BufferedWriter out = new BufferedWriter(fw);
            out.write(writeToFile);
            out.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeUsers() {
        File userDirectory = new File("./Server/src/main/resources/Users");
        userDirectory.mkdir();
        if (userDirectory.listFiles() == null) return;
        for (File userFile : userDirectory.listFiles()) {
            try {
                Scanner fileScanner = new Scanner(userFile);
                String userJson = fileScanner.nextLine();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                EfficientUser tempUser = gson.fromJson(userJson, EfficientUser.class);
                createUserFromEffUser(tempUser);
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void createUserFromEffUser(EfficientUser tempUser) {
        ArrayList<Deck> actualDecks = new ArrayList<>();
        for (EfficientDeck deckName : tempUser.getDecks()) {
            actualDecks.add(createDeckFromStringArray(deckName.getName(), deckName.isActive(), deckName.getMainCards(), deckName.getSideCards()));
        }
        ArrayList<Card> actualInactiveCards = new ArrayList<>();
        for (String cardName : tempUser.getInactiveCards())
            actualInactiveCards.add(Card.getCardByName(cardName));
        new User(tempUser.getUsername(), tempUser.getPassword(), tempUser.getNickname(), tempUser.getAvatarID(), tempUser.getScore(),
                tempUser.getBalance(), actualDecks, actualInactiveCards);
    }

    private Deck createDeckFromStringArray(String deckName, boolean isActive, ArrayList<String> mainCards, ArrayList<String> sideCards) {
        Deck deck = new Deck(deckName, isActive);
        for (String cardName : mainCards) {
            deck.getMainCards().add(Card.getCardByName(cardName));
        }
        for (String cardName : sideCards) {
            deck.getSideCards().add(Card.getCardByName(cardName));
        }
        return deck;
    }

}
