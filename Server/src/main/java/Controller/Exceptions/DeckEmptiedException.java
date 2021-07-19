package Controller.Exceptions;

public class DeckEmptiedException extends Exception {
    public DeckEmptiedException() {
        super("no more cards in deck");
    }
}
