package View.Menu;

import Controller.ProgramController.Regex;

import static Controller.ProgramController.Regex.help;

public class ImportExport implements Help{
    public void run(String command){
        if (Regex.getCommandMatcher(command, help).matches()) help();
    }
    public void help(){
        System.out.println("import card [card name]");
        System.out.println("export card [card name]");
        System.out.println("menu show-current");
        System.out.println("help");
        System.out.println("menu exit");
    }
}
