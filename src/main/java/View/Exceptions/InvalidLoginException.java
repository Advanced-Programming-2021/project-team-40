package main.java.View.Exceptions;

public class InvalidLoginException extends Exception{
    public InvalidLoginException(){
        super("Username and password didn't match!");
    }
}
