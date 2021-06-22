import Controller.DatabaseController.DatabaseController;
import Controller.MenuController.LoginController;
import Controller.ProgramController.Menu;
import Controller.ProgramController.ProgramController;
import Database.User;
import View.Exceptions.*;
import org.apache.commons.logging.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

public class SignupLoginTests {

    @BeforeAll
    static void createFirstUser() throws WeakPasswordException, RepetitiveUsernameException, RepetitiveNicknameException {
        DatabaseController.getInstance();
        User.clearUsers();
        LoginController.registerUser("Testali", "1234Pass", "TestUser1");
    }

    @Test
    public void createSecondUserTest(){
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
    }

    @Test
    public void loginTests(){
        Executable usernameNotFound = new Executable() {
            @Override
            public void execute() throws Throwable {
                LoginController.loginUser("TestZahra", "Password666");
            }
        };

        Executable invalidLogin = new Executable() {
            @Override
            public void execute() throws Throwable {
                LoginController.loginUser("MammadTest", "password666");
            }
        };

        Executable everythingFine = new Executable() {
            @Override
            public void execute() throws Throwable {
                LoginController.loginUser("Testali", "1234Pass");
            }
        };

        Assertions.assertThrows(InvalidLoginException.class, usernameNotFound);
        Assertions.assertThrows(InvalidLoginException.class, invalidLogin);
        Assertions.assertDoesNotThrow(everythingFine);
        Assertions.assertEquals(ProgramController.getInstance().getCurrentMenu(), Menu.MAIN_MENU);
    }

}
