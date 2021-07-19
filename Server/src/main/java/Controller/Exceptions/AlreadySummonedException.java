package Controller.Exceptions;

public class AlreadySummonedException extends Exception{
    public AlreadySummonedException(){
        super("You have already summoned/set a card in this turn");
    }
}