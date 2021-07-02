package Gameplay;

import Database.Cards.Card;

public class SpellAndTrapFieldArea extends FieldArea {
    private boolean hasJustBeenSet = true;
    private boolean canBeActivated = false;
    public SpellAndTrapFieldArea() {

    }

    public void setHasJustBeenSet(boolean hasJustBeenSet) {
        this.hasJustBeenSet = hasJustBeenSet;
    }

    public boolean isHasJustBeenSet() {
        return hasJustBeenSet;
    }

    public boolean isCanBeActivated() {
        return canBeActivated;
    }

    public void setCanBeActivated(boolean canBeActivated) {
        this.canBeActivated = canBeActivated;
    }
}
