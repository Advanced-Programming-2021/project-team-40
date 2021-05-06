package View;

import Database.Cards.*;
import Database.Cards.Monster;
import Database.Cards.MonsterType;
import Database.Cards.SpellAndTrap;

public class ShopView {

    private static ShopView shopView;

    private ShopView(){

    }

    public static ShopView getInstance(){
        if (shopView == null)
            shopView = new ShopView();
        return shopView;
    }

    public void showAll() {
        for (Card card: Card.getAllCards()) {
            if (card instanceof Monster){
                Monster monster = (Monster) card;
                System.out.println("Name: " + monster.getName());
                System.out.println("Level: " +  monster.getLevel());
                System.out.println("Type: " + monster.getMonsterType());
                System.out.println("ATK: " + monster.getAttackPoints());
                System.out.println("DEF: " + monster.getDefensePoints());
                System.out.println("Description: " + monster.getDescription());
                System.out.println("Price: " + card.getCardPrice());
                System.out.println("---------------------------");
            }else{
                SpellAndTrap spellAndTrap = (SpellAndTrap) card;
                System.out.println("Name: " + spellAndTrap.getName());
                System.out.println("Type: " + spellAndTrap.getIcon());
                if (spellAndTrap instanceof Trap) System.out.println("Trap");
                if (spellAndTrap instanceof Spell) System.out.println("Spell");
                System.out.println("Description: " + spellAndTrap.getDescription());
                System.out.println("Price: " + card.getCardPrice());
                System.out.println("---------------------------");
            }
        }
    }
}
