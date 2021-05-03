package main.java.View.Exceptions;

public class RepetitiveUsernameException extends Exception{
    public RepetitiveUsernameException(String username){
        super("user with username " + username + " already exists");
    }
}
