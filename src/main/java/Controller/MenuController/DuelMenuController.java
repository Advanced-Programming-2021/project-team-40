package Controller.MenuController;

import Controller.DuelController.GameplayController;

import Controller.ProgramController.Menu;
import Controller.ProgramController.ProgramController;
import Database.User;
import Gameplay.Gameplay;
import Gameplay.Player;
import View.Exceptions.*;

public class DuelMenuController implements MenuNavigation {
    private static DuelMenuController duelMenuController;
    private DuelMenuController() {

    }

    public static DuelMenuController getInstance() {
        if (duelMenuController == null)
            duelMenuController = new DuelMenuController();
        return duelMenuController;
    }
    public void startPlayerGame(String playerTwoUsername,String roundCount, User currentUser) throws Exception {
        User userTwo;
        if ((userTwo = User.getUserByName(playerTwoUsername)) == null) throw new UserNotFoundException();
        if (userTwo.equals(currentUser)) throw new SameUserChosenException();
        if (currentUser.getActiveDeck() == null) throw new ActiveDeckNotFoundException(currentUser.getUsername());
        if (userTwo.getActiveDeck() == null) throw new ActiveDeckNotFoundException(playerTwoUsername);
        if (DeckMenuController.getInstance().isDeckInvalid(currentUser.getActiveDeck())) throw new InvalidDeckException(currentUser.getUsername());
        if (DeckMenuController.getInstance().isDeckInvalid(userTwo.getActiveDeck())) throw new InvalidDeckException(playerTwoUsername);
        if (!isRoundSupported(roundCount)) throw new InvalidRoundNumberException();
        ProgramController.getInstance().setCurrentMenu(Menu.GAMEPLAY);
        Gameplay gameplay = new Gameplay(new Player(currentUser),new Player(userTwo),Integer.parseInt(roundCount));
        GameplayController.getInstance().setGameplay(gameplay);
        GameplayController.getInstance().setStartingPlayer();
        GameplayController.getInstance().dealCardsAtBeginning();
//        System.out.println(GameplayController.getInstance().drawCard());
    }

    public void startAIGame(String roundCount) {

    }
    private boolean isRoundSupported(String round){
        if (round.matches("^\\d+$")) return Integer.parseInt(round) < 4 && Integer.parseInt(round) > 0;
        return false;
    }

    public void toUpperMenu() {
        ProgramController.getInstance().setCurrentMenu(Menu.MAIN_MENU);
    }

    public void toLowerMenu(String menuName) {
        try {
            throw new MenuNavigationNotPossibleException();
        } catch (MenuNavigationNotPossibleException e) {
            System.out.println(e.getMessage());
        }
    }
}
