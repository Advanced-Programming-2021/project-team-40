package Gameplay;


import Controller.DuelController.GameplayController;
import Database.Cards.Card;
import View.Exceptions.NoCardFoundException;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class Field extends GridPane {
    private MonsterFieldArea[] monstersField = new MonsterFieldArea[5];
    private SpellAndTrapFieldArea[] spellAndTrapField = new SpellAndTrapFieldArea[5];
    private ArrayList<Card> graveyard = new ArrayList<>();
    private FieldZoneArea fieldZone = new FieldZoneArea();

    public Field() {
        super();
        for (int i = 0; i < 5; i++) {
            monstersField[i] = new MonsterFieldArea();
            spellAndTrapField[i] = new SpellAndTrapFieldArea();
        }
        this.add(fieldZone,0,0);
        this.add(monstersField[0],2,1);
        this.add(monstersField[1],3,1);
        this.add(monstersField[2],1,1);
        this.add(monstersField[3],4,1);
        this.add(monstersField[4],0,1);
        this.add(spellAndTrapField[0],2,2);
        this.add(spellAndTrapField[1],3,2);
        this.add(spellAndTrapField[2],1,2);
        this.add(spellAndTrapField[3],4,2);
        this.add(spellAndTrapField[4],0,2);
        fieldZone.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    if (GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getFieldZone().equals(fieldZone
                    ))
                        GameplayController.getInstance().selectCard(null,"-f",false);
                    else GameplayController.getInstance().selectCard(null,"-f",true);
                    //TODO: update show card panel
                } catch (Exception e) {
                    //TODO: pop-up or sth
                    System.out.println(e.getMessage());
                }
            }
        });
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

    public void setFieldZone(FieldZoneArea fieldZone) {
        this.fieldZone = fieldZone;
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
