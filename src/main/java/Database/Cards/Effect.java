package Database.Cards;

import Controller.DuelController.GameplayController;
import Gameplay.Gameplay;

public interface Effect {
    Gameplay gameplay = GameplayController.getInstance().getGameplay();
    void execute(Object... objects) throws Exception;
}