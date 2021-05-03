package main.java.Controller.MenuController;

import main.java.Controller.DatabaseController.DeckController;
import main.java.Controller.DuelController.GameplayController;
import main.java.Controller.ProgramController.Menu;
import main.java.Controller.ProgramController.ProgramController;
import main.java.Database.User;
import main.java.Gameplay.Gameplay;
import main.java.Gameplay.Player;
import main.java.View.Exceptions.ActiveDeckNotFoundException;
import main.java.View.Exceptions.InvalidDeckException;
import main.java.View.Exceptions.InvalidRoundNumberException;
import main.java.View.Exceptions.UserNotFoundException;

import java.util.regex.Matcher;

public class DuelMenuController {
    private static DuelMenuController duelMenuController;
    private DuelMenuController() {

    }

    public static DuelMenuController getInstance() {
        if (duelMenuController == null)
            duelMenuController = new DuelMenuController();
        return duelMenuController;
    }
    public void startPlayerGame(Matcher matcher, User currentUser) throws UserNotFoundException, ActiveDeckNotFoundException, InvalidDeckException, InvalidRoundNumberException {
        String playerTwoUsername = matcher.group("username");
        String roundCount = matcher.group("rounds");
        User userTwo;
        if ((userTwo = User.getUserByName(playerTwoUsername)) == null) throw new UserNotFoundException();
        if (currentUser.getActiveDeck() == null) throw new ActiveDeckNotFoundException(currentUser.getUsername());
        if (userTwo.getActiveDeck() == null) throw new ActiveDeckNotFoundException(playerTwoUsername);
        if (!DeckController.getInstance().isDeckValid(currentUser.getActiveDeck())) throw new InvalidDeckException(currentUser.getUsername());
        if (!DeckController.getInstance().isDeckValid(userTwo.getActiveDeck())) throw new InvalidDeckException(playerTwoUsername);
        if (!isRoundSupported(roundCount)) throw new InvalidRoundNumberException();
        GameplayController.getInstance().setGameplay(new Gameplay(new Player(currentUser),new Player(userTwo),Integer.parseInt(roundCount)));
        ProgramController.setCurrentMenu(Menu.GAMEPLAY);
    }

    public void startAIGame() {

    }
    private boolean isRoundSupported(String round){
        if (round.matches("^\\d+$")) return Integer.parseInt(round) < 4 && Integer.parseInt(round) > 0;
        return false;
    }
}
