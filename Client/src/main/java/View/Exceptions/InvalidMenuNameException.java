package View.Exceptions;

public class InvalidMenuNameException extends Exception{
    public InvalidMenuNameException () {super("no menu exists with this name");}
}
