package Controller.DuelController;

import Database.Cards.Card;
import GamePlay.Player;

import java.util.List;

public class PlayerController {
    private static PlayerController playerController;

    private PlayerController() {
    }

    public static PlayerController getInstance() {
        if (playerController == null) playerController = new PlayerController();
        return playerController;
    }

    public void drawACard(Player player) {
        List<Card> cards = player.getPlayingDeck().getMainCards();
        Card card = cards.get(0);
        cards.remove(card);
        player.getPlayingHand().add(card);
        System.out.println("new card added to the hand : " + card.getName());
    }
}
