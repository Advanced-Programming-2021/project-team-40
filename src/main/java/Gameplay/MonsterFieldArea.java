package Gameplay;

import Database.Cards.Card;
import Database.Cards.Monster;

public class MonsterFieldArea extends FieldArea{
    private boolean isAttack;
    private boolean hasAttacked;
    private boolean hasSwitchedMode;
    private int attackPoint;
    private int defensePoint;
    private int turnsLeftForEffect;
    public MonsterFieldArea(){
    }

    @Override
    public void putCard(Card card, boolean isAttack){
        this.isAttack = isAttack;
        super.putCard(card, isAttack);
        if (card !=null) {
            attackPoint = ((Monster) card).getAttackPoints();
            defensePoint = ((Monster) card).getDefensePoints();
            hasSwitchedMode = true;
        }
        else {
            attackPoint = 0;
            defensePoint = 0;
        }
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

    public void setHasSwitchedMode(boolean hasSwitchedMode) {
        this.hasSwitchedMode = hasSwitchedMode;
    }

    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    public void setAttackPoint(int attackPoint) {
        this.attackPoint = attackPoint;
    }

    public void setDefensePoint(int defensePoint) {
        this.defensePoint = defensePoint;
    }

    public int getAttackPoint() {
        return attackPoint;
    }

    public int getDefensePoint() {
        return defensePoint;
    }

    public void setTurnsLeftForEffect(int turnsLeftForEffect) {
        this.turnsLeftForEffect = turnsLeftForEffect;
    }

    public int getTurnsLeftForEffect() {
        return turnsLeftForEffect;
    }
}
