package View.Exceptions;

public class SpellZoneFullException extends Exception {
    public SpellZoneFullException() {
        super("spell card zone is full");
    }
}
