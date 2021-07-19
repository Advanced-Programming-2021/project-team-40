package Controller.Exceptions;

public class MonsterZoneFullException extends Exception{
    public MonsterZoneFullException(){
        super("monster card zone is full");
    }
}
