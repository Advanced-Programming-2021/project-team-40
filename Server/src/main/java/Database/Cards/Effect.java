package Database.Cards;

import Controller.Exceptions.*;

public interface Effect {
    void execute(Object... objects) throws AttackNotPossibleException, ActionNotPossibleException, SpecialSummonNotPossibleException, MonsterZoneFullException, NoCardIsSelectedException;
}