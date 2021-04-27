package ProgramController.Exceptions;

public class InvalidPasswordException extends Exception{
    public InvalidPasswordException(){
        super("current password is invalid");
    }
}
