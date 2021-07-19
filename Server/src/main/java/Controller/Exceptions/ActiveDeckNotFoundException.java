package Controller.Exceptions;

public class ActiveDeckNotFoundException extends Exception{
    public ActiveDeckNotFoundException(String username){
        super(username + " has no active deck");
    }
}
