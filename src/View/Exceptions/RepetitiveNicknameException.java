package View.Exceptions;

public class RepetitiveNicknameException extends Exception {
    public RepetitiveNicknameException(String nickname){
        super("user with nickname " + nickname + " already exists");
    }
}