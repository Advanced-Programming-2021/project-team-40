package GamePlay;

import Database.Cards.Card;

import java.util.ArrayList;

public class Field {
    private boolean direction;
    private MonsterFieldArea[] monstersField = new MonsterFieldArea[5];
    private SpellAndTrapFieldArea[] spellAndTrapField = new SpellAndTrapFieldArea[5];
    private ArrayList<Card> graveyard = new ArrayList<>();
    private FieldArea fieldZone;

    public MonsterFieldArea[] getMonstersField() {
        return monstersField;
    }

    public void setMonstersField(MonsterFieldArea[] monstersField) {
        this.monstersField = monstersField;
    }

    public SpellAndTrapFieldArea[] getSpellAndTrapField() {
        return spellAndTrapField;
    }

    public void setSpellAndTrapField(SpellAndTrapFieldArea[] spellAndTrapField) {
        this.spellAndTrapField = spellAndTrapField;
    }

    public ArrayList<Card> getGraveyard() {
        return graveyard;
    }

    public void setGraveyard(ArrayList<Card> graveyard) {
        this.graveyard = graveyard;
    }
}
