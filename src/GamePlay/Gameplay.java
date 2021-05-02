package GamePlay;

import Controller.ProgramController.Regex;

import java.util.regex.Matcher;

public class Gameplay {
    private Player playerOne;
    private Player playerTwo;
    private Player currentPlayer;
    private FieldArea selectedCard;
    private Phase currentPhase;
    private int rounds;
    private boolean hasPlacedMonster;

    public Gameplay(Player playerOne, Player playerTwo, int rounds) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.rounds = rounds;
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
        else switch (currentPhase) {
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
}
