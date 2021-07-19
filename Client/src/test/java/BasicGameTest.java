import Controller.MenuController.LoginController;
import Database.User;
import View.Exceptions.RepetitiveUsernameException;
import View.Exceptions.WeakPasswordException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.InputStream;

public class BasicGameTest {
    public static InputStream sysInBackup;

    @BeforeAll
    static void backupSysIn() {
        sysInBackup = System.in;
    }

    @BeforeAll
    static void clearUsers(){
        User.clearUsers();
    }

    @Test
    public void createUsers() {
        Executable firstUser = new Executable() {
            @Override
            public void execute() throws Throwable {
                LoginController.registerUser("Testali", "1234Passowrd", "Test1");
            }
        };

        Executable repetitiveUsername = new Executable() {
            @Override
            public void execute() throws Throwable {
                LoginController.registerUser("Testali", "1234", "Test2");
            }
        };

        Executable weakPassword = new Executable() {
            @Override
            public void execute() throws Throwable {
                LoginController.registerUser("Testali", "1234", "Test2");
            }
        };

        Assertions.assertDoesNotThrow(firstUser);
        Assertions.assertThrows(RepetitiveUsernameException.class, repetitiveUsername);
        Assertions.assertThrows(WeakPasswordException.class, weakPassword);
    }

}
