package ProgramController.Exceptions;

public class invalidLogin extends Exception{
    public invalidLogin(){
        super("Username and password didn't match!");
    }
}
