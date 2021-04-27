package ProgramController.Exceptions;

public class repetitiveNickname extends Exception {
    public repetitiveNickname(String nickname){
        super("user with nickname " + nickname + " already exists");
    }
}
