package ProgramController.Exceptions;

public class invalidPassword extends Exception{
    public invalidPassword(){
        super("current password is invalid");
    }
}
