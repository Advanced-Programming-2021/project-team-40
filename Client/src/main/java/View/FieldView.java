package View;

import Gameplay.*;

public class FieldView {
    public static void showBoard(Player opponent, Player myself) {
        printForOpponent(opponent);
        printForMyself(myself);
    }

    public static void printForOpponent(Player player) {
        System.out.println(player.getUser().getNickname() + ":" + player.getLifePoints());
        for (int i = 0; i < player.getPlayingHand().size(); i++) {
            System.out.print("\tc");
        }
        System.out.println("\n" + player.getPlayingDeck().getMainCards().size());
        System.out.println("\t" + getShowcaseString(player.getField().getSpellAndTrapFieldById(4))
                + "\t" + getShowcaseString(player.getField().getSpellAndTrapFieldById(2))
                + "\t" + getShowcaseString(player.getField().getSpellAndTrapFieldById(1))
                + "\t" + getShowcaseString(player.getField().getSpellAndTrapFieldById(3))
                + "\t" + getShowcaseString(player.getField().getSpellAndTrapFieldById(5)));
        System.out.println("\t" + getShowcaseString(player.getField().getMonstersFieldById(4))
                + "\t" + getShowcaseString(player.getField().getMonstersFieldById(2))
                + "\t" + getShowcaseString(player.getField().getMonstersFieldById(1))
                + "\t" + getShowcaseString(player.getField().getMonstersFieldById(3))
                + "\t" + getShowcaseString(player.getField().getMonstersFieldById(5)));
        System.out.println(player.getField().getGraveyard().size() + "\t\t\t\t\t\t" + getShowcaseString(player.getField().getFieldZone()));
    }

    public static void printForMyself(Player player){
        System.out.println(getShowcaseString(player.getField().getFieldZone()) + "\t\t\t\t\t\t" + player.getField().getGraveyard().size());
        System.out.println("\t" + getShowcaseString(player.getField().getMonstersFieldById(5))
                + "\t" + getShowcaseString(player.getField().getMonstersFieldById(3))
                + "\t" + getShowcaseString(player.getField().getMonstersFieldById(1))
                + "\t" + getShowcaseString(player.getField().getMonstersFieldById(2))
                + "\t" + getShowcaseString(player.getField().getMonstersFieldById(4)));
        System.out.println("\t" + getShowcaseString(player.getField().getSpellAndTrapFieldById(5))
                + "\t" + getShowcaseString(player.getField().getSpellAndTrapFieldById(3))
                + "\t" + getShowcaseString(player.getField().getSpellAndTrapFieldById(1))
                + "\t" + getShowcaseString(player.getField().getSpellAndTrapFieldById(2))
                + "\t" + getShowcaseString(player.getField().getSpellAndTrapFieldById(4)));
        System.out.println("\t\t\t\t\t\t" + player.getPlayingDeck().getMainCards().size());
        for (int i = 0; i < player.getPlayingHand().size(); i++) {
            System.out.print("\tc");
        }
        System.out.println("\n" + player.getUser().getNickname() + ":" + player.getLifePoints());
    }

    private static String getShowcaseString(FieldArea fieldArea) {
        if (fieldArea.getCard() == null) return "E";
        if (fieldArea instanceof SpellAndTrapFieldArea) {
            if (fieldArea.visibility()) return "O";
            return "H";
        } else if (fieldArea instanceof MonsterFieldArea) {
            if (((MonsterFieldArea) fieldArea).isAttack()) return "OO";
            if (fieldArea.visibility()) return "DO";
            return "DH";
        }else{
            if (fieldArea.getCard() == null) return "E";
            return "O";
        }
    }
}