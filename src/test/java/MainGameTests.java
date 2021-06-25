import Controller.DatabaseController.DatabaseController;
import Controller.DuelController.GameplayController;
import Controller.MenuController.DeckMenuController;
import Controller.MenuController.DuelMenuController;
import Controller.MenuController.LoginController;
import Controller.MenuController.ShopController;
import Database.Cards.CardType;
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
        ShopController.getInstance().buy("Bitron", kian);
        ShopController.getInstance().buy("Yami", dani);
        ShopController.getInstance().buy("Bitron", dani);
    }

    @BeforeAll
    static void buildDecks() throws Exception {
        User kian = User.getUserByName("Kian");
        User dani = User.getUserByNickname("DanDan");
        DeckMenuController.getInstance().createDeck("DeckB", kian);
        DeckMenuController.getInstance().activateDeck("DeckB", kian);
        DeckMenuController.getInstance().createDeck("DeckJ", dani);
        DeckMenuController.getInstance().activateDeck("DeckJ", dani);
        DeckMenuController.getInstance().addCard("DeckB", "Bitron", true, kian);
        while (!kian.getInactiveCards().isEmpty()) {
            DeckMenuController.getInstance().addCard("DeckB", kian.getInactiveCards().get(0).getName(), false, kian);
        }
        DeckMenuController.getInstance().addCard("DeckJ", "Bitron", true, dani);
        while (!dani.getInactiveCards().isEmpty()) {
            DeckMenuController.getInstance().addCard("DeckJ", dani.getInactiveCards().get(0).getName(), false, dani);
        }
        DatabaseController.getInstance().saveUser(kian);
        DatabaseController.getInstance().saveUser(dani);
    }

    @Test
    public void start(){
        Assertions.assertEquals(true, "Use the users created here to check gameplay".startsWith("U"));
    }

}
