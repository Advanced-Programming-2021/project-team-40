package View.Menu;

import Controller.ProgramController.Regex;

import static Controller.ProgramController.Regex.help;

public class ImportExport {
    public void run(String command){
        if (Regex.getCommandMatcher(command, help).matches()) help();
    }
    private void help(){
        System.out.println("menu exit");
        System.out.println("menu show-current");
        System.out.println("import card [card name]");
        System.out.println("export card [card name]");
    }
}
