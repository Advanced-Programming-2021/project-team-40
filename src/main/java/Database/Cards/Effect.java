package Database.Cards;

import Gameplay.Player;

public interface Effect {
    void execute(Player cardOwner);
}