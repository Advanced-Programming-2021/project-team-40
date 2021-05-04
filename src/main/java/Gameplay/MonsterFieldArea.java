package Gameplay;

import Controller.DuelController.GameplayController;
import Database.Cards.Card;

public class MonsterFieldArea extends FieldArea{
    private boolean isAttack;
    private boolean hasAttacked;
    private boolean hasSwitchedMode;
    private int turnsLeftForEffect;
    public MonsterFieldArea(){

    }

    @Override
    public void putCard(Card card, boolean isAttack){
        this.isAttack = isAttack;
        super.putCard(card, isAttack);
        GameplayController.getInstance().getGameplay().setHasPlacedMonster(true);
    }

    public void setAttack(boolean attack) {
        isAttack = attack;
    }

    public void changePosition(){
        isAttack = !isAttack;
        hasSwitchedMode = true;
    }

    public boolean isAttack() {
        return isAttack;
    }

    public boolean hasAttacked() {
        return hasAttacked;
    }

    public boolean hasSwitchedMode() {
        return hasSwitchedMode;
    }

}
