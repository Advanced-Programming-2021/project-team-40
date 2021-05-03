package View.Exceptions;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(){
        super("there is no player with this username");
    }
}
