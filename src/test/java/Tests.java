import org.junit.jupiter.api.*;
import Database.*;

public class Tests{

    @BeforeAll
    static void clearUsers(){
        User.clearUsers();
    }
}
