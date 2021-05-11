package Database.Cards.Effects;


import Controller.DuelController.GameplayController;
import Database.Cards.Monster;
import Gameplay.*;

//Yomi Ship,
public class DestroyAttackerOnDestruction extends Effect {
    public DestroyAttackerOnDestruction(){
        effectType = EffectTypes.ON_DESTRUCTION;
    }
    public void execute(Player cardOwner, Gameplay gameplay) {
        if (cardOwner.equals(gameplay.getOpponentPlayer()))
            GameplayController.getInstance().destroyMonsterCard(gameplay.getCurrentPlayer(), (MonsterFieldArea) gameplay.getAttacker());
    }
}
