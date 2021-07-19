package Controller.Exceptions;

public class SpecialSummonNotPossibleException extends Exception {
    public SpecialSummonNotPossibleException() {
        super("there is no way you could special summon a monster");
    }
}
