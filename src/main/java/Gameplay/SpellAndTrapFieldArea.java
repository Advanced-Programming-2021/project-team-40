package Gameplay;

import Database.Cards.Card;

public class SpellAndTrapFieldArea extends FieldArea {
    private boolean canBeActivated = false;
    public SpellAndTrapFieldArea() {
        super();
    }

    public boolean isCanBeActivated() {
        return canBeActivated;
    }

    public void setCanBeActivated(boolean canBeActivated) {
        this.canBeActivated = canBeActivated;
    }
}
