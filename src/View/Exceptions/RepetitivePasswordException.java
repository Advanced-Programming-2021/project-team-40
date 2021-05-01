package View.Exceptions;

public class RepetitivePasswordException extends Exception {
    public RepetitivePasswordException(){
        super("please enter a new password");
    }
}
