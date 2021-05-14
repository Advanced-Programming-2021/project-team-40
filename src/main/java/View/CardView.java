package View;

import Database.Cards.*;

public class CardView {
    public static void showCard(Card card) {
        System.out.println("---------------------------");
        System.out.println("Name: " + card.getName());
        if (card instanceof Monster) {
            Monster monster = (Monster) card;
            System.out.println("Level: " + monster.getLevel());
            System.out.println("Type: " + monster.getMonsterType());
            System.out.println("ATK: " + monster.getAttackPoints());
            System.out.println("DEF: " + monster.getDefensePoints());
        } else {
            SpellAndTrap spellAndTrap = (SpellAndTrap) card;
            System.out.println("Type: " + spellAndTrap.getIcon());
            if (spellAndTrap instanceof Trap) System.out.println("Trap");
            if (spellAndTrap instanceof Spell) System.out.println("Spell");
        }
        System.out.println("Description: " + card.getDescription());
        System.out.println("Price: " + card.getCardPrice());
        System.out.println("---------------------------");
    }

    public static void showCardInList(Card card) {
        System.out.println(card.getName() + ":\n\t" + card.getDescription() + "\n----------------");
    }

    public static void invisibleCard() {
        System.out.println("card is not visible");
    }
}
