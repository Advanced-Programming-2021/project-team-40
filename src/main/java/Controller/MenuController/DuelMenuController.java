package Controller.MenuController;

import Controller.DatabaseController.DeckController;
import Controller.DuelController.GameplayController;

import Controller.ProgramController.Menu;
import Controller.ProgramController.ProgramController;
import Database.User;
import Gameplay.Gameplay;
import Gameplay.Player;
import View.Exceptions.ActiveDeckNotFoundException;
import View.Exceptions.InvalidDeckException;
import View.Exceptions.InvalidRoundNumberException;
import View.Exceptions.UserNotFoundException;

public class DuelMenuController {
    private static DuelMenuController duelMenuController;
    private DuelMenuController() {

    }

    public static DuelMenuController getInstance() {
        if (duelMenuController == null)
            duelMenuController = new DuelMenuController();
        return duelMenuController;
    }
    public void startPlayerGame(String playerTwoUsername,String roundCount, User currentUser) throws UserNotFoundException, ActiveDeckNotFoundException, InvalidDeckException, InvalidRoundNumberException {
        User userTwo;
        if ((userTwo = User.getUserByName(playerTwoUsername)) == null) throw new UserNotFoundException();
        if (currentUser.getActiveDeck() == null) throw new ActiveDeckNotFoundException(currentUser.getUsername());
        if (userTwo.getActiveDeck() == null) throw new ActiveDeckNotFoundException(playerTwoUsername);
        if (DeckController.getInstance().isDeckInvalid(currentUser.getActiveDeck())) throw new InvalidDeckException(currentUser.getUsername());
        if (DeckController.getInstance().isDeckInvalid(userTwo.getActiveDeck())) throw new InvalidDeckException(playerTwoUsername);
        if (!isRoundSupported(roundCount)) throw new InvalidRoundNumberException();
        ProgramController.getInstance().setCurrentMenu(Menu.GAMEPLAY);
        Gameplay gameplay = new Gameplay(new Player(currentUser),new Player(userTwo),Integer.parseInt(roundCount));
        GameplayController.getInstance().setGameplay(gameplay);
        GameplayController.getInstance().setStartingPlayer();
        GameplayController.getInstance().dealCardsAtBeginning();
        System.out.println(GameplayController.getInstance().drawCard());
    }

    public void startAIGame(String roundCount) {

    }
    private boolean isRoundSupported(String round){
        if (round.matches("^\\d+$")) return Integer.parseInt(round) < 4 && Integer.parseInt(round) > 0;
        return false;
    }
}
