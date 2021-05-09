package View.Exceptions;

public class CommandCancellationException extends Exception {
    public CommandCancellationException(String command){
        super(command + " cancelled");
    }
}
