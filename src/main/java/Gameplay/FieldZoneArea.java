package Gameplay;

import Controller.DuelController.GameplayController;
import Database.Cards.*;
import View.Exceptions.InvalidCardForFieldzoneException;
import View.Exceptions.InvalidCardSelectionException;

public class FieldZoneArea extends SpellAndTrapFieldArea{
    @Override
    public void putCard(Card card, boolean visibility) {
        calculateFieldEffects(card);
        super.putCard(card, visibility);
    }

    private void calculateFieldEffects(Card card) {
        for (MonsterFieldArea monsterFieldArea:
                GameplayController.getInstance().getGameplay().getCurrentPlayer().getField().getMonstersField()) {
           if (((Monster) monsterFieldArea.getCard()).getMonsterType().equals()) monsterFieldArea.setAttackPoint(((Monster)monsterFieldArea.getCard()).getAttackPoints() );
        }
    }
}
