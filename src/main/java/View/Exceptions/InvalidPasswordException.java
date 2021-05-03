package main.java.View.Exceptions;

public class InvalidPasswordException extends Exception{
    public InvalidPasswordException(){
        super("current password is invalid");
    }
}
