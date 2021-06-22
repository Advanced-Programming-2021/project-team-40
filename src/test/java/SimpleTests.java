import Controller.MenuController.LoginController;
import Database.User;
import View.Exceptions.RepetitiveNicknameException;
import View.Exceptions.RepetitiveUsernameException;
import View.Exceptions.WeakPasswordException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

public class SimpleTests {
    @BeforeAll
    static void clearUsers(){
        User.clearUsers();
    }

    @BeforeAll
    static void createFirstUser() throws WeakPasswordException, RepetitiveUsernameException, RepetitiveNicknameException {
        LoginController.registerUser("Testali", "1234Pass", "TestUser1");
    }

    @Test
    public void createSecondUser(){
        Executable weakPassword = new Executable() {
            @Override
            public void execute() throws Throwable {
                LoginController.registerUser("Testali", "1234", "TestUser1");
            }
        };

        Executable repetitiveUsername = new Executable() {
            @Override
            public void execute() throws Throwable {
                LoginController.registerUser("Testali", "Password666", "TestUser1");

            }
        };

        Executable repetitiveNickname = new Executable() {
            @Override
            public void execute() throws Throwable {
                LoginController.registerUser("MammadTest", "Password666", "TestUser1");

            }
        };

        Executable everythingFine = new Executable() {
            @Override
            public void execute() throws Throwable {
                LoginController.registerUser("MammadTest", "Password666", "TestUser2");
            }
        };

        Assertions.assertThrows(WeakPasswordException.class, weakPassword);
        Assertions.assertThrows(RepetitiveUsernameException.class, repetitiveUsername);
        Assertions.assertThrows(RepetitiveNicknameException.class, repetitiveNickname);
        Assertions.assertDoesNotThrow(everythingFine);
        Assertions.assertEquals(User.getUsers().size(), 2);
    }

    @Test
    public void testTest(){
        int a = 2;
        int b = a + 2;
        Assertions.assertEquals(4, b);
    }
}
