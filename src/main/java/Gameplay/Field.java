package Gameplay;


import Database.Cards.Card;

import java.util.ArrayList;

public class Field {
    private MonsterFieldArea[] monstersField = new MonsterFieldArea[5];
    private SpellAndTrapFieldArea[] spellAndTrapField = new SpellAndTrapFieldArea[5];
    private ArrayList<Card> graveyard = new ArrayList<>();
    private FieldZoneArea fieldZone = new FieldZoneArea();

    public Field() {
        for (int i = 0; i < 5; i++) {
            monstersField[i] = new MonsterFieldArea();
            spellAndTrapField[i] = new SpellAndTrapFieldArea();
        }
    }

    public MonsterFieldArea[] getMonstersField() {
        return monstersField;
    }

    public SpellAndTrapFieldArea[] getSpellAndTrapField() {
        return spellAndTrapField;
    }

    public MonsterFieldArea getMonstersFieldById(int id) {
        return monstersField[id - 1];
    }

    public MonsterFieldArea getFreeMonsterFieldArea() {
        for (int i = 0; i < 5; i++) {
            if (monstersField[i].getCard() == null) return monstersField[i];
        }
        return null;
    }

    public SpellAndTrapFieldArea getFreeSpellFieldArea() {
        for (int i = 0; i < 5; i++) {
            if (spellAndTrapField[i].getCard() == null) return spellAndTrapField[i];
        }
        return null;
    }

    public SpellAndTrapFieldArea getSpellAndTrapFieldById(int id) {
        return spellAndTrapField[id - 1];
    }

    public ArrayList<Card> getGraveyard() {
        return graveyard;
    }

    public FieldZoneArea getFieldZone() {
        return fieldZone;
    }

    public void endTurnActions() {
        for (MonsterFieldArea monsterFieldArea : monstersField) {
            monsterFieldArea.setHasAttacked(false);
            monsterFieldArea.setHasSwitchedMode(false);
            if (monsterFieldArea.getTurnsLeftForEffect() > 0)
                monsterFieldArea.setTurnsLeftForEffect(monsterFieldArea.getTurnsLeftForEffect() - 1);
        }
        for (SpellAndTrapFieldArea spellAndTrapFieldArea : spellAndTrapField){
            spellAndTrapFieldArea.setHasJustBeenSet(false);
        }
    }
}
