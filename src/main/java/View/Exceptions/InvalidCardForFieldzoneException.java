package View.Exceptions;

public class InvalidCardForFieldzoneException extends Exception{
    public InvalidCardForFieldzoneException(){
        super("you can't put this card in fieldzone");
    }
}
