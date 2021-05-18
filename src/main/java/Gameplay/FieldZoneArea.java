package Gameplay;

import Controller.DuelController.GameplayController;
import Database.Cards.*;
import View.Exceptions.InvalidCardForFieldzoneException;
import View.Exceptions.InvalidCardSelectionException;

public class FieldZoneArea extends SpellAndTrapFieldArea{
    @Override
    public void putCard(Card card, boolean visibility) {
        if (this.card != null)
            GameplayController.getInstance().destroySpellAndTrapCard(GameplayController.getInstance().gameplay.getCurrentPlayer(), this);
        super.putCard(card, visibility);
    }

}