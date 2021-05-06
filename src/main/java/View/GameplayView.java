package View;

import Controller.DuelController.GameplayController;
import Controller.ProgramController.Regex;
import View.Exceptions.*;

import java.util.regex.Matcher;

public class GameplayView {
    private static GameplayView gameplayView;
    private GameplayView(){

    }
    public static GameplayView getInstance(){
        if (gameplayView == null) gameplayView = new GameplayView();
        return gameplayView;
    }
    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.selectCard)).matches()) selectCard(matcher);
        else if (Regex.getCommandMatcher(command, Regex.deselectCard).matches()) deselectCard();
        else if (Regex.getCommandMatcher(command, Regex.nextPhase).matches())
            GameplayController.getInstance().goToNextPhase();
        else if (Regex.getCommandMatcher(command, Regex.showGraveyard).matches())
            GraveyardView.showGraveyard(GameplayController.getInstance().getGameplay().getCurrentPlayer().getField().getGraveyard());
        else if (Regex.getCommandMatcher(command, Regex.showSelectedCard).matches()) showCard();
        else if (Regex.getCommandMatcher(command, Regex.summon).matches()) summon();
        else if (Regex.getCommandMatcher(command, Regex.set).matches()) set();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.setPosition)).matches()) setPosition(matcher);
        else if (Regex.getCommandMatcher(command, Regex.surrender).matches()) ;
        else if (Regex.getCommandMatcher(command, Regex.flipSummon).matches()) ;
        else if (Regex.getCommandMatcher(command, Regex.activateEffect).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.attack)).matches()) attack(matcher);
        else if (Regex.getCommandMatcher(command, Regex.directAttack).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.increaseMoneyCheatCode)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.increaseLifePointsCheatCode)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.forceAddCardCheatCode)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.setWinnerCheatCode)).matches()) ;
        else System.err.println("invalid command");
    }

    private void attack(Matcher matcher) {
        String monsterId = matcher.group("monsterId");
        try {
            StringBuilder temp;
            temp = GameplayController.getInstance().attack(monsterId);
            System.out.println(temp);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void setPosition(Matcher matcher) {
        try {
            boolean isAttack = matcher.group(1).matches("^attack$");
            GameplayController.getInstance().changePosition(isAttack);
            System.out.println("monster card position changed successfully");
        } catch (NoCardIsSelectedException | InvalidChangePositionException | WrongPhaseException | AlreadyInPositionException | AlreadySetPositionException e) {
            System.out.println(e.getMessage());
        }
    }

    private void set() {
        try {
            GameplayController.getInstance().putMonsterCard(false);
            System.out.println("set successfully");
        } catch (NoCardIsSelectedException | AlreadySummonedException | InvalidSummonException | MonsterZoneFullException | WrongPhaseException e) {
            System.out.println(e.getMessage());
            ;
        }
    }

    private void summon() {
        try {
            GameplayController.getInstance().putMonsterCard(true);
            System.out.println("summoned successfully");
        } catch (NoCardIsSelectedException | AlreadySummonedException | InvalidSummonException | MonsterZoneFullException | WrongPhaseException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showCard() {
        try {
            GameplayController.getInstance().showCard();
        } catch (NoCardIsSelectedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deselectCard() {
        try {
            GameplayController.getInstance().deselectCard();
            System.out.println("card deselected");
        } catch (NoCardIsSelectedException e) {
            System.out.println(e.getMessage());
        }
    }

    private void selectCard(Matcher matcher) {
        try {
            GameplayController.getInstance().selectCard(matcher);
            System.out.println("card selected");
        } catch (InvalidCardSelectionException | NoCardFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
