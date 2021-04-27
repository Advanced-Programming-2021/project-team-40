package ProgramController.Exceptions;

public class InvalidCardNameException extends Exception {
    public InvalidCardNameException(String cardName){
        super("card with name " + cardName + " does not exist");
    }
}
