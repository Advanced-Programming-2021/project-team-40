package main.java.View.Exceptions;

public class RepetitiveDeckNameException extends Exception{
    public RepetitiveDeckNameException(String deckName){
        super("deck with name " + deckName + " already exists");
    }
}
