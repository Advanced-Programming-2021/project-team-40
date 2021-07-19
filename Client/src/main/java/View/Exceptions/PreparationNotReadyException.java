package View.Exceptions;

public class PreparationNotReadyException extends Exception{
    public PreparationNotReadyException() {
        super("preparations of this spell are not done yet");
    }
}
