package Gameplay;


import Database.Cards.Card;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Field extends GridPane {
    private MonsterFieldArea[] monstersField = new MonsterFieldArea[5];
    private SpellAndTrapFieldArea[] spellAndTrapField = new SpellAndTrapFieldArea[5];
    private ArrayList<Card> graveyard = new ArrayList<>();
    private FieldZoneArea fieldZone = new FieldZoneArea(-1);
    private ScrollPane handScrollPane = new ScrollPane();
    private HBox handFieldArea = new HBox();
    private Rectangle graveyardField = new Rectangle();

    public Field() {
        super();
        super.setPrefHeight(FieldArea.getFieldAreaHeight() * 4);
        super.setPrefWidth(FieldArea.getFieldAreaWidth() * 6);
        for (int i = 0; i < 5; i++) {
            monstersField[i] = new MonsterFieldArea(i + 1);
            spellAndTrapField[i] = new SpellAndTrapFieldArea(i + 1);
        }
        this.add(fieldZone, 0, 0);
        this.add(graveyardField,4,0);
        this.add(monstersField[0], 2, 1);
        this.add(monstersField[1], 3, 1);
        this.add(monstersField[2], 1, 1);
        this.add(monstersField[3], 4, 1);
        this.add(monstersField[4], 0, 1);
        this.add(spellAndTrapField[0], 2, 2);
        this.add(spellAndTrapField[1], 3, 2);
        this.add(spellAndTrapField[2], 1, 2);
        this.add(spellAndTrapField[3], 4, 2);
        this.add(spellAndTrapField[4], 0, 2);
        handScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        handScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        handScrollPane.setContent(handFieldArea);
        handScrollPane.setPrefSize(FieldArea.getFieldAreaWidth() * 3,FieldArea.getFieldAreaHeight());
        this.add(handScrollPane,1,3,3,1);
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

    public HBox getHandFieldArea() {
        return handFieldArea;
    }

    public void endTurnActions() {
        for (MonsterFieldArea monsterFieldArea : monstersField) {
            monsterFieldArea.setHasAttacked(false);
            monsterFieldArea.setHasSwitchedMode(false);
            if (monsterFieldArea.getTurnsLeftForEffect() > 0)
                monsterFieldArea.setTurnsLeftForEffect(monsterFieldArea.getTurnsLeftForEffect() - 1);
        }
        for (SpellAndTrapFieldArea spellAndTrapFieldArea : spellAndTrapField) {
            spellAndTrapFieldArea.setHasJustBeenSet(false);
        }
    }
}
