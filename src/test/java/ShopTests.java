import Controller.DatabaseController.DatabaseController;
import Controller.MenuController.LoginController;
import Controller.MenuController.ShopController;
import Database.User;
import View.Exceptions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class ShopTests {

    @BeforeAll
    static void initializeUser() throws WeakPasswordException, RepetitiveUsernameException, RepetitiveNicknameException {
        DatabaseController.getInstance();
        User.clearUsers();
        LoginController.registerUser("Testali", "1234Pass", "TestUser1");
    }

    @Test
    public void test() throws InvalidCardNameException, NotEnoughMoneyException {
        User testali = User.getUserByName("Testali");
        ShopController.getInstance().buy("Battle OX", testali);
        ShopController.getInstance().buy("Battle OX", testali);
        ShopController.getInstance().buy("Battle OX", testali);

        Executable invalidName = new Executable() {
            @Override
            public void execute() throws Throwable {
                ShopController.getInstance().buy("Random Card", testali);
            }
        };

        Executable notEnoughMoney = new Executable() {
            @Override
            public void execute() throws Throwable {
                LoginController.registerUser("RichDude", "1234Money", "Stonks");
                User richDude = User.getUserByName("RichDude");
                for (int i = 0; i < 10; i++) {
                    ShopController.getInstance().buy("Gate Guardian", richDude);
                }
            }
        };

        Assertions.assertThrows(InvalidCardNameException.class, invalidName);
        Assertions.assertThrows(NotEnoughMoneyException.class, notEnoughMoney);
        Assertions.assertEquals(User.getUserByName("RichDude").getInactiveCards().size(), 5);
    }
}
