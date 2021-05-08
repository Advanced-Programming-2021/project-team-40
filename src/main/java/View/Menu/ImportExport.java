package View.Menu;

import Controller.ProgramController.Menu;
import Controller.ProgramController.Regex;

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
        if (Regex.getCommandMatcher(command, help).matches()) help();
        else if (command.matches(Regex.showCurrentMenu)) System.out.println(Menu.IMPORT_EXPORT_MENU.toString());
        else System.out.println("invalid command");
    }
    public void help(){
        System.out.println("import card [card name]");
        System.out.println("export card [card name]");
        System.out.println("menu show-current");
        System.out.println("help");
        System.out.println("menu exit");
    }
}
