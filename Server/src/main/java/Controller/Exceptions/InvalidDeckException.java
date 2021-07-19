package Controller.Exceptions;

public class InvalidDeckException extends Exception{
    public InvalidDeckException(String username){
        super(username + "'s deck is invalid");
    }
}
