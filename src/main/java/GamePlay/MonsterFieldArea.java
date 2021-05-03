package GamePlay;

import Database.Cards.Card;

public class MonsterFieldArea extends FieldArea{
    private String attackOrDefense;
    private boolean hasAttacked;
    private boolean hasSwitchedMode;
    private int turnsLeftForEffect;
    public MonsterFieldArea(Card card,boolean visibility){
        super(card,visibility);

    }
}
