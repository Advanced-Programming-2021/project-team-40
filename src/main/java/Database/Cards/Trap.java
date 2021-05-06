package Database.Cards;

import java.util.ArrayList;

public class Trap extends SpellAndTrap{
    private static ArrayList<Trap> traps = new ArrayList<>();

    public Trap(String name, Icon icon, String description, boolean isLimited, int cardPrice) {
        super(name, icon, description, isLimited, cardPrice);
        traps.add(this);
    }

    public static ArrayList<Trap> getTraps() {
        return traps;
    }
}
