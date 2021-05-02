package GamePlay;

import Database.Cards.Card;

import java.util.ArrayList;

public class Field {
    private boolean direction;
    private MonsterFieldArea monstersField[] = new MonsterFieldArea[5];
    private SpellAndTrapFieldArea spellAndTrapField[] = new SpellAndTrapFieldArea[5];
    private ArrayList<Card> graveyard = new ArrayList<>();
    private FieldArea fieldZone;

}
