package Controller.MenuController;

import Controller.DatabaseController.DeckController;
import Controller.DuelController.GameplayController;
import Controller.ProgramController.Menu;
import Controller.ProgramController.ProgramController;
import Database.User;
import GamePlay.Gameplay;
import GamePlay.Player;
import View.Exceptions.ActiveDeckNotFoundException;
import View.Exceptions.InvalidDeckException;
import View.Exceptions.InvalidRoundNumberException;
import View.Exceptions.UserNotFoundException;

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
