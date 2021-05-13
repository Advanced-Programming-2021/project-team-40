package Controller.DatabaseController;

import Controller.DuelController.GameplayController;
import Controller.ProgramController.ProgramController;
import Database.Cards.*;
import Database.Deck;
import Database.EfficientDeck;
import Database.EfficientUser;
import Database.User;
import Gameplay.Gameplay;
import Gameplay.Player;
import Gameplay.MonsterFieldArea;
import View.Exceptions.InvalidCardSelectionException;
import View.Exceptions.NoCardFoundException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DatabaseController {

    private static DatabaseController databaseController;

    private DatabaseController(){
        initialize();
    }

    public static DatabaseController getInstance(){
        if (databaseController == null)
            databaseController = new DatabaseController();
        return databaseController;
    }

    private void initialize(){
        initializeMonsterCards();
        initializeSpellAndTrapCards();
        initializeUsers();
    }

    public void saveUser(User user){
        File userFile = new File("./src/main/resources/Users/" + user.getUsername() + ".json");
        EfficientUser efficientUser = new EfficientUser(user);
        try {
            userFile.createNewFile();
            Gson gson = new GsonBuilder().create();
            String writeToFile = gson.toJson(efficientUser);
            FileWriter fw = new FileWriter(userFile);
            BufferedWriter out = new BufferedWriter(fw);
            out.write(writeToFile);
            out.close();
            fw.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }

    public void initializeSpellAndTrapCards(){
        try{
            File spellAndTrapCards = new File("./src/main/resources/Cards/SpellTrap.csv");
            FileReader fileReader= new FileReader(spellAndTrapCards);
            CSVReader csvReader = new CSVReader(fileReader);
            csvReader.readNext();
            String[] cardDetails;
            while ((cardDetails = csvReader.readNext()) != null){
                switch (cardDetails[1]){
                    case "Trap":
                        new Trap(cardDetails[0], getIcon(cardDetails[2]), cardDetails[3], isLimited(cardDetails[4]), Integer.parseInt(cardDetails[5]) );
                        break;
                    case "Spell":
                        new Spell(cardDetails[0], getIcon(cardDetails[2]), cardDetails[3], isLimited(cardDetails[4]), Integer.parseInt(cardDetails[5]) );

                }
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private void initializeMonsterCards(){
        try{
            File monsterCards = new File("./src/main/resources/Cards/Monster.csv");
            FileReader fileReader= new FileReader(monsterCards);
            CSVReader csvReader = new CSVReader(fileReader);
            csvReader.readNext();
            String[] cardDetails;
            while ((cardDetails = csvReader.readNext()) != null){
                new Monster(cardDetails[0], Integer.parseInt(cardDetails[1]), getAttribute(cardDetails[2]), getMonsterType(cardDetails[3]),
                        getCardType(cardDetails[4]), Integer.parseInt(cardDetails[5]), Integer.parseInt(cardDetails[6]), cardDetails[7], Integer.parseInt(cardDetails[8]));
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        initializeMonsterCardEffects();
    }

    private void initializeMonsterCardEffects() {
        //TODO add effects to cards
        Gameplay gameplay = GameplayController.getInstance().getGameplay();
        try {
            Card.getCardByName("Yomi Ship").onDestruction = new Effect() {
                @Override
                public Object execute(Object... objects) {
                    Player cardOwner = (Player) objects[0];
                    if (cardOwner.equals(gameplay.getOpponentPlayer()))
                        GameplayController.getInstance().destroyMonsterCard(gameplay.getCurrentPlayer(), (MonsterFieldArea) gameplay.getAttacker());
                    return null;
                }
            };
            Card.getCardByName("Man-Eater Bug").onFlipSummon = new Effect() {
                @Override
                public Object execute(Object... objects) {//TODO: implement
                    System.out.println("please enter a monster id");
                    Player player = GameplayController.getInstance().getGameplay().getOpponentPlayer();
                    String idToCheck;
                    while (true) {
                        idToCheck = ProgramController.getInstance().getScanner().nextLine();
                        try {
                            if (GameplayController.getInstance().getMonsterFieldCount(player) == 0)
                                throw new NoCardFoundException();
                            if (GameplayController.getInstance().isLocationNumberInvalid(idToCheck))
                                throw new InvalidCardSelectionException();
                            int id = Integer.parseInt(idToCheck);
                            MonsterFieldArea monster = player.getField().getMonstersFieldById(id);
                            GameplayController.getInstance().destroyMonsterCard(player, monster);
                            break;
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    return null;
                }
            };
            Card.getCardByName("Suijin").onBeingAttacked = new Effect() {
                @Override
                public Object execute(Object... objects) {
                    String command;
                    while (true) {
                        System.out.println("do you want to activate Suijin?");
                        command = ProgramController.getInstance().getScanner().nextLine();
                        if (command.equalsIgnoreCase("yes")) {
                            ((MonsterFieldArea)gameplay.getAttacker()).setAttackPoint(0);
                            //TODO:
                            // return after one turn
                            // implement
                            break;
                        }
                        else if (command.equalsIgnoreCase("no")) break;
                        else System.out.println("invalid command");
                    }
                    return null;
                }
            };
            Card.getCardByName("Exploder Dragon").onDestruction = new Effect() {
                @Override
                public Object execute(Object... objects) {
                    Player cardOwner = (Player) objects[0];
                    if (cardOwner.equals(gameplay.getOpponentPlayer()))
                        GameplayController.getInstance().destroyMonsterCard(gameplay.getCurrentPlayer(), (MonsterFieldArea) gameplay.getAttacker());
                    return null;
                }
            };
            Card.getCardByName("Exploder Dragon").onDamageCalculation = new Effect() {
                @Override
                public Object execute(Object... objects) {
                    return 0;
                }
            };
            Card.getCardByName("Marshmallon").afterDamageCalculation = new Effect() {
                @Override
                public Object execute(Object... objects) {
                    Player cardOwner = (Player) objects[0];
                    //TODO: this is worthless,fix it
                    if (cardOwner.equals(gameplay.getOpponentPlayer()))
                        gameplay.getOpponentPlayer().setLifePoints(gameplay.getOpponentPlayer().getLifePoints() - 1000);
                    else
                        gameplay.getCurrentPlayer().setLifePoints(gameplay.getCurrentPlayer().getLifePoints() - 1000);
                    return null;
                }
            };
            Card.getCardByName("Scanner").onTurnStart = new ContinuousEffect() {
                @Override
                public void activate() {
                    while (true) {

                    }
                }

                @Override
                public void deactivate() {

                }
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("invalid card name in monster initialization");
        }
    }

    private boolean isLimited(String string){
        if (string.matches("Limited")) return true;
        return false;
    }

    private Icon getIcon(String name){
        name = name.toUpperCase();
        for (int i = 0; i < Icon.values().length; i++) {
            if (name.matches(Icon.values()[i].toString())) return Icon.values()[i];
        }
        return null;
    }

    private Attribute getAttribute(String name){
        name = name.toUpperCase();
        for (int i = 0; i < Attribute.values().length; i++) {
            if (name.matches(Attribute.values()[i].toString())) return Attribute.values()[i];
        }
        return null;
    }

    private MonsterType getMonsterType(String name){
        name = name.toUpperCase();
        for (int i = 0; i < MonsterType.values().length; i++) {
            if (name.matches(MonsterType.values()[i].toString())) return MonsterType.values()[i];
        }
        return null;
    }

    private CardType getCardType(String name){
        name = name.toUpperCase();
        for (int i = 0; i < MonsterType.values().length; i++) {
            if (name.matches(MonsterType.values()[i].toString())) return CardType.values()[i];
        }
        return null;
    }

    private void initializeUsers(){
        File userDirectory = new File("./src/main/resources/Users");
        userDirectory.mkdir();
        if (userDirectory.listFiles() == null) return;
        for (File userFile : userDirectory.listFiles()){
            try{
                Scanner fileScanner = new Scanner(userFile);
                String userJson = fileScanner.nextLine();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                EfficientUser tempUser = gson.fromJson(userJson, EfficientUser.class);
                ArrayList<Deck> actualDecks = new ArrayList<>();
                for (EfficientDeck deckName: tempUser.getDecks()) {
                    actualDecks.add(createDeckFromStringArray(deckName.getName(), deckName.isActive(), deckName.getMainCards(), deckName.getSideCards()));
                }
                ArrayList<Card> actualInactiveCards = new ArrayList<>();
                for (String cardName: tempUser.getInactiveCards()) actualInactiveCards.add(Card.getCardByName(cardName));
                new User(tempUser.getUsername(), tempUser.getPassword(), tempUser.getNickname(), tempUser.getScore(),
                        tempUser.getBalance(), actualDecks, actualInactiveCards);
            }catch (FileNotFoundException e){
                System.out.println(e.getMessage());
            }
        }
    }

    private Deck createDeckFromStringArray (String deckName, boolean isActive, ArrayList<String> mainCards, ArrayList<String> sideCards){
        Deck deck = new Deck(deckName, isActive);
        for (String cardName: mainCards) {
            deck.getMainCards().add(Card.getCardByName(cardName));
        }
        for (String cardName: sideCards){
            deck.getSideCards().add(Card.getCardByName(cardName));
        }
        return deck;
    }
}
