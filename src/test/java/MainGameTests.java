import Controller.DatabaseController.DatabaseController;
import Controller.DuelController.GameplayController;
import Controller.MenuController.DeckMenuController;
import Controller.MenuController.DuelMenuController;
import Controller.MenuController.LoginController;
import Controller.MenuController.ShopController;
import Database.Cards.Monster;
import Database.Deck;
import Database.User;
import View.Exceptions.*;
import View.Menu.DeckMenu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class MainGameTests {
    @BeforeAll
    static void createUsers() throws WeakPasswordException, RepetitiveUsernameException, RepetitiveNicknameException, InvalidCardNameException, NotEnoughMoneyException {
        DatabaseController.getInstance();
        User.clearUsers();
        LoginController.registerUser("Kian", "1234Kian", "KiaKia");
        LoginController.registerUser("Danial", "1234Dani", "DanDan");
        User kian = User.getUserByName("Kian");
        User dani = User.getUserByNickname("DanDan");
        String[] cardNames = {"Battle OX", "Axe Raider", "Yomi Ship", "Wattkid", "Crab Turtle", "Feral Imp", "Exploder Dragon", "Horn Imp",
                "Battle warrior", "Haniwa", "Crawling dragon", "Mirror Force", "Magic Cylinder"};
        for (String cardName : cardNames) {
            for (int i = 0; i < 3; i++) {
                ShopController.getInstance().buy(cardName, kian);
                ShopController.getInstance().buy(cardName, dani);
            }
        }
        ShopController.getInstance().buy("Yami", kian);
        ShopController.getInstance().buy("Yami", dani);
    }

    @BeforeAll
    static void buildDecks() throws Exception {
        User kian = User.getUserByName("Kian");
        User dani = User.getUserByNickname("DanDan");
        DeckMenuController.getInstance().createDeck("DeckB", kian);
        DeckMenuController.getInstance().activateDeck("DeckB", kian);
        DeckMenuController.getInstance().createDeck("DeckJ", dani);
        DeckMenuController.getInstance().activateDeck("DeckJ", dani);
        while (! kian.getInactiveCards().isEmpty()){
            DeckMenuController.getInstance().addCard("DeckB", kian.getInactiveCards().get(0).getName(), false, kian);
        }
        while (! dani.getInactiveCards().isEmpty()){
            DeckMenuController.getInstance().addCard("DeckJ", dani.getInactiveCards().get(0).getName(), false, dani);
        }
        DatabaseController.getInstance().saveUser(kian);
        DatabaseController.getInstance().saveUser(dani);
    }

    @Test
    public void startGame() throws Exception {
        User kian = User.getUserByName("Kian");
        DuelMenuController.getInstance().startPlayerGame("Danial", "1", kian);
        User dani = User.getUserByNickname("DanDan");
        GameplayController.getInstance().goToNextPhase();
        Assertions.assertEquals(5, GameplayController.getInstance().getGameplay().getCurrentPlayer().getPlayingHand().size());
        GameplayController.getInstance().forceAddCard("Axe Raider");
        GameplayController.getInstance().selectCard("5", "-h", false);
        if (GameplayController.getInstance().getGameplay().getSelectedField().getCard() instanceof Monster){
            GameplayController.getInstance().summon();
            Assertions.assertNotNull(GameplayController.getInstance().getGameplay().getCurrentPlayer().getField().getMonstersField()[0].getCard());
        }
        GameplayController.getInstance().goToEndPhase();
        GameplayController.getInstance().selectCard("1", "-h", true);
        Executable opponentCardSummon = new Executable() {
            @Override
            public void execute() throws Throwable {
                GameplayController.getInstance().summon();
            }
        };
        Assertions.assertThrows(InvalidSummonException.class, opponentCardSummon);
        GameplayController.getInstance().forceAddCard("Feral Imp");
        GameplayController.getInstance().selectCard("7", "-h", true);
        GameplayController.getInstance().summon();
        GameplayController.getInstance().goToNextPhase();
        GameplayController.getInstance().selectCard("1", "-m", true);
        Executable opponentCardAttack = new Executable() {
            @Override
            public void execute() throws Throwable {
                GameplayController.getInstance().attack("1");
            }
        };
        Assertions.assertThrows(AttackNotPossibleException.class, opponentCardAttack);
        GameplayController.getInstance().selectCard("1", "-m", false);
        Executable emptyAttackTarget = new Executable() {
            @Override
            public void execute() throws Throwable {
                GameplayController.getInstance().attack("2");
            }
        };
        Assertions.assertThrows(NoCardToAttackException.class, emptyAttackTarget);

    }
}
