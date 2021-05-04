package View.Exceptions;

public class WeakPasswordException extends Exception{
    public WeakPasswordException(){
        super("password must be 8 digits or longer, and contain an uppercase letter, a lowercase letter, and a digit");
    }
}
