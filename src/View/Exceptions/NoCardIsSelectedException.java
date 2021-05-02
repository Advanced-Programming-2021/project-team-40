package View.Exceptions;

public class NoCardIsSelectedException extends Exception{
    public NoCardIsSelectedException(){
        super("no card is selected yet");
    }
}
