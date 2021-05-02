package Controller.DuelController;


import Controller.ProgramController.Regex;
import Database.Cards.Card;
import GamePlay.Gameplay;
import GamePlay.Player;
import View.Exceptions.InvalidCardSelectionException;

import java.util.regex.Matcher;

public class GameplayController {
    public Gameplay gameplay;
    //TODO add view
    public GameplayController(Player playerOne,Player playerTwo,int rounds){
        gameplay = new Gameplay(playerOne,playerTwo,rounds);
    }
    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.selectCard)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.deselectCard)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.nextPhase)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.showGraveyard)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.showSelectedCard)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.surrender)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.increaseMoneyCheatCode)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.increaseLifePointsCheatCode)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.forceAddCardCheatCode)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.setWinnerCheatCode)).matches()) ;
        else switch (gameplay.getCurrentPhase()) {
                case DRAW_PHASE:

                    break;
                case STANDBY_PHASE:

                    break;
                case MAIN_PHASE_ONE:
                    if ((matcher = Regex.getCommandMatcher(command, Regex.summon)).matches()) ;
                    if ((matcher = Regex.getCommandMatcher(command, Regex.set)).matches()) ;
                    if ((matcher = Regex.getCommandMatcher(command, Regex.setPosition)).matches()) ;
                    if ((matcher = Regex.getCommandMatcher(command, Regex.flipSummon)).matches()) ;
                    if ((matcher = Regex.getCommandMatcher(command, Regex.activateEffect)).matches()) ;
                    else System.out.println("invalid command");
                    break;
                case BATTLE_PHASE:
                    if ((matcher = Regex.getCommandMatcher(command, Regex.attack)).matches()) ;
                    if ((matcher = Regex.getCommandMatcher(command, Regex.flipSummon)).matches()) ;
                    if ((matcher = Regex.getCommandMatcher(command, Regex.directAttack)).matches()) ;
                    else System.out.println("invalid command");
                    break;
                case MAIN_PHASE_TW0:
                    break;
                case END_PHASE:
                    break;
            }
    }
    public void selectCard(Matcher matcher) throws InvalidCardSelectionException {
        if (matcher.group("monsterId") != null) {
            String monsterId = matcher.group("monsterId");
            if (!isNumberValid(monsterId)) throw new InvalidCardSelectionException();
            int id = Integer.parseInt(monsterId);
            if (matcher.group("oppo1") == null || matcher.group("oppo2") == null){
                gameplay.getCurrentPlayer().getField();
            }
        }
    }
    private boolean isNumberValid(String string) {
        if (string.matches("^\\d+$")){
            int id = Integer.parseInt(string);
            return id > 0 && id < 6;
        }
        return false;
    }
}