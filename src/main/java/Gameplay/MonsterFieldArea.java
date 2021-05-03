package main.java.Gameplay;

import main.java.Database.Cards.Card;

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
