package Gameplay;

import Database.Cards.Card;

import java.util.ArrayList;

public class Field {
    private MonsterFieldArea[] monstersField = new MonsterFieldArea[5];
    private SpellAndTrapFieldArea[] spellAndTrapField = new SpellAndTrapFieldArea[5];
    private ArrayList<Card> graveyard = new ArrayList<>();
    private FieldArea fieldZone;

    public MonsterFieldArea getMonstersFieldById(int id) {
        return monstersField[id];
    }

    public MonsterFieldArea getFreeMonsterFieldArea(){
        for (int i = 0; i < 5; i++) {
            if (monstersField[i].getCard() == null) return monstersField[i];
        }
        return null;
    }

    public SpellAndTrapFieldArea getSpellAndTrapFieldById(int id) {
        return spellAndTrapField[id];
    }

    public ArrayList<Card> getGraveyard() {
        return graveyard;
    }

    public FieldArea getFieldZone() {
        return fieldZone;
    }

    //TODO delete useless setters
    public void setSpellAndTrapField(SpellAndTrapFieldArea[] spellAndTrapField) {
        this.spellAndTrapField = spellAndTrapField;
    }

    public void setMonstersField(MonsterFieldArea[] monstersField) {
        this.monstersField = monstersField;
    }

    public void setGraveyard(ArrayList<Card> graveyard) {
        this.graveyard = graveyard;
    }

    public void setFieldZone(FieldArea fieldZone) {
        this.fieldZone = fieldZone;
    }
}
