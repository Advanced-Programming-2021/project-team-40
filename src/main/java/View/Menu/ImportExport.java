package View.Menu;

import Controller.ProgramController.Menu;
import Controller.ProgramController.Regex;
import Database.Cards.Card;
import Database.EfficientUser;
import View.Exceptions.InvalidCardNameException;
import View.Exceptions.NoCardFoundException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;

import static Controller.ProgramController.Regex.help;

public class ImportExport implements Help {
    private static ImportExport importExport;

    private ImportExport() {

    }

    public static ImportExport getInstance() {
        if (importExport == null) importExport = new ImportExport();
        return importExport;
    }

    public void run(String command) {
        Matcher matcher;
        if (Regex.getCommandMatcher(command, help).matches()) help();
        else if (command.matches(Regex.importCard))
            importACard(Regex.getCommandMatcher(command, Regex.importCard).group("cardName"));
        else if ((matcher = Regex.getCommandMatcher(command, Regex.exportCard)).matches()){
            try {
                exportACard(matcher.group("cardName"));
            }catch (InvalidCardNameException e){
                System.out.println(e.getMessage());
            }
        }
        else if (command.matches(Regex.showCurrentMenu)) System.out.println(Menu.IMPORT_EXPORT_MENU.toString());
        else System.out.println("invalid command");
    }

    private void importACard(String cardName) {
        System.out.println("It looks like you're early! This exciting feature will become available in the next phase of our game!");
    }

    private void exportACard(String cardName) throws InvalidCardNameException {
        if (Card.getCardByName(cardName) == null) throw new InvalidCardNameException(cardName);
        File cardFile = new File("./src/main/resources/Cards/" + cardName + ".json");
        try {
            cardFile.createNewFile();
            Gson gson = new GsonBuilder().create();
            String writeToFile = gson.toJson(Card.getCardByName(cardName));
            FileWriter fw = new FileWriter(cardFile);
            BufferedWriter out = new BufferedWriter(fw);
            out.write(writeToFile);
            out.close();
            fw.close();
            System.out.println("basic JSON exported successfully!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void help() {
        System.out.println("Note: import/export directory is Cards folder in resources");
        System.out.println("import card [card name]");
        System.out.println("export card [card name]");
        System.out.println("menu show-current");
        System.out.println("help");
        System.out.println("menu exit");
    }
}
