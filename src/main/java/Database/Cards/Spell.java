package Database.Cards;

import Gameplay.Player;

import java.util.ArrayList;

public class Spell extends SpellAndTrap{
    private static ArrayList<Spell> spells = new ArrayList<>();

    public Effect spellEffect;

    public Spell(String name, Icon icon, String description, boolean isLimited, int cardPrice) {
        super(name, icon, description, isLimited, cardPrice);
        spells.add(this);
    }


    public static ArrayList<Spell> getSpells() {
        return spells;
    }
}
