package Database.Cards;

import Gameplay.Player;

import java.util.ArrayList;

public class Spell extends SpellAndTrap{
    private static ArrayList<Spell> spells = new ArrayList<>();

    public Spell(String name, Icon icon, String description, boolean isLimited, int cardPrice) {
        super(name, icon, description, isLimited, cardPrice);
        spells.add(this);
    }

    public void activateEffect(Player cardOwner){

    }

    public void deactivateEffect(Player cardOwner){

    }

    public void continuousEffect(Player cardOwner){

    }

    public void quickPlayEffect(Player cardOwner){

    }

    public static ArrayList<Spell> getSpells() {
        return spells;
    }
}
