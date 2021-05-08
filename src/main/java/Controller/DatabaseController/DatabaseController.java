package Controller.DatabaseController;

import Database.Cards.*;
import Database.Cards.Effects.DestroyAttackerOnDestruction;
import Database.Cards.Effects.Effect;
import Database.User;
import View.ShopView;
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
        initializeSpellAndTrapCards();
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
                User tempUser = gson.fromJson(userJson, User.class);
                new User(tempUser.getUsername(), tempUser.getPassword(), tempUser.getNickname(), tempUser.getScore(),
                        tempUser.getBalance(), tempUser.getDecks(), tempUser.getInactiveCards());
            }catch (FileNotFoundException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
