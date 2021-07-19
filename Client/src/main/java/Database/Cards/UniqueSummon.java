package Database.Cards;

import Controller.DuelController.GameplayController;
import Gameplay.Gameplay;

public interface UniqueSummon {
    Gameplay gameplay = GameplayController.getInstance().getGameplay();
    void summon() throws Exception;
}
