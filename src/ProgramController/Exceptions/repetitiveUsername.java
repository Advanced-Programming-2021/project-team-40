package ProgramController.Exceptions;

public class repetitiveUsername extends Exception{
    public repetitiveUsername(String username){
        super("user with username " + username + " already exists");
    }
}
