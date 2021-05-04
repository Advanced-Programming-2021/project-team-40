package Controller.DatabaseController;

import Database.Cards.*;
import Database.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.*;

import java.io.*;
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
        initializeUsers();
        initializeMonsterCards();
    }

    public void saveUser(User user){
        File userFile = new File("./src/main/resources/Users/" + user.getUsername() + ".json");
        try {
            userFile.createNewFile();
            Gson gson = new GsonBuilder().create();
            String writeToFile = gson.toJson(user);
            FileWriter fw = new FileWriter(userFile);
            BufferedWriter out = new BufferedWriter(fw);
            out.write(writeToFile);
            out.close();
            fw.close();
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
            System.err.println(e.getMessage());
        }
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
        try {
            for (File userFile : userDirectory.listFiles()){
                try{
                    Scanner fileScanner = new Scanner(userFile);
                    String userJson = fileScanner.nextLine();
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    User tempUser = gson.fromJson(userJson, User.class);
                    new User(tempUser.getUsername(), tempUser.getPassword(), tempUser.getNickname(), tempUser.getScore(),
                            tempUser.getBalance(), tempUser.getDecks(), tempUser.getActiveDeck(), tempUser.getInactiveCards());
                }catch (FileNotFoundException e){
                    System.out.println(e.getMessage());
                }
            }
        } catch (NullPointerException e){
            System.out.println(e.getMessage());
        }
    }
}
