package View;

import Database.Cards.*;
import Database.Cards.Monster;
import Database.Cards.MonsterType;
import Database.Cards.SpellAndTrap;

public class ShopView {

    private static ShopView shopView;

    private ShopView() {

    }

    public static ShopView getInstance() {
        if (shopView == null)
            shopView = new ShopView();
        return shopView;
    }

    public void showAll() {
        for (Card card : Card.getAllCards()) {
            CardView.showCardInList(card);
        }
    }
}
