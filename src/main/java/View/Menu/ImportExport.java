package View.Menu;

import Controller.ProgramController.Menu;
import Controller.ProgramController.Regex;

import java.util.regex.Matcher;

import static Controller.ProgramController.Regex.help;

public class ImportExport implements Help{
    private static ImportExport importExport;

    private ImportExport(){

    }

    public static ImportExport getInstance(){
        if (importExport == null) importExport = new ImportExport();
        return importExport;
    }
    public void run(String command){
        Matcher matcher;
        if (Regex.getCommandMatcher(command, help).matches()) help();
        else if (command.matches(Regex.importCard)) importACard(Regex.getCommandMatcher(command, Regex.importCard).group("cardName"));
        else if (command.matches(Regex.showCurrentMenu)) System.out.println(Menu.IMPORT_EXPORT_MENU.toString());
        else System.out.println("invalid command");
    }

    private void importACard(String cardName) {

    }

    public void help(){
        System.out.println("Note: import/export directory is Cards folder in resources");
        System.out.println("import card [card name]");
        System.out.println("export card [card name]");
        System.out.println("menu show-current");
        System.out.println("help");
        System.out.println("menu exit");
    }
}
