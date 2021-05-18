package Database.Cards;

import Controller.DuelController.GameplayController;
import Gameplay.Gameplay;
import View.Exceptions.*;

public interface Effect {
    Gameplay gameplay = GameplayController.getInstance().getGameplay();
    void execute(Object... objects) throws AttackNotPossibleException, ActionNotPossibleException, SpecialSummonNotPossibleException, MonsterZoneFullException, NoCardIsSelectedException;
}