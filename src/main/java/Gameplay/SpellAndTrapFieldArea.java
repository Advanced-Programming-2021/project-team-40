package Gameplay;

import Database.Cards.Card;

public class SpellAndTrapFieldArea extends FieldArea {
    private boolean isActivated;
    private boolean hasJustBeenSet = true;
    public SpellAndTrapFieldArea() {
        super();
    }

    public void setHasJustBeenSet(boolean hasJustBeenSet) {
        this.hasJustBeenSet = hasJustBeenSet;
    }

    public boolean isHasJustBeenSet() {
        return hasJustBeenSet;
    }
}
