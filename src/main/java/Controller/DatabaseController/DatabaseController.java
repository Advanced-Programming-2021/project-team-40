package Controller.DatabaseController;

import Database.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.*;

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
        initializeCards();
    }

    public void saveUser(User user){
        File userFile = new File("./main/resources/Users/" + user.getUsername() + ".json");
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

    private void initializeCards(){
        File monsterCards = new File("./main/resources/Cards/Monster.csv");
        try{
            Scanner fileScanner = new Scanner(monsterCards);
            while (fileScanner.hasNextLine()){
                String currentCard = fileScanner.nextLine();
                String[] properties = currentCard.split(",");
                for (String string: properties) {
                    System.out.println(string);
                }
            }
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    private void initializeUsers(){
        File userDirectory = new File("./main/resources/Users");
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
    }
}
