package View;

import Controller.DuelController.GameplayController;
import Controller.ProgramController.Regex;
import View.Exceptions.*;

import java.util.regex.Matcher;

public class GameplayView {
    private static GameplayView gameplayView;
    private boolean tributeMode;
    private int tributeCount = 0;
    private boolean graveyardMode;
    private boolean ritualMode;

    private GameplayView() {

    }

    public static GameplayView getInstance() {
        if (gameplayView == null) gameplayView = new GameplayView();
        return gameplayView;
    }

    public void run(String command) {
        Matcher matcher;
        if (tributeMode) {
        }
        if (graveyardMode) {

        }
        if (ritualMode) {

        } else if ((matcher = Regex.getCommandMatcher(command, Regex.selectCard)).matches()) selectCard(matcher);
        else if (Regex.getCommandMatcher(command, Regex.deselectCard).matches()) deselectCard();
        else if (Regex.getCommandMatcher(command, Regex.nextPhase).matches())
            GameplayController.getInstance().goToNextPhase();
        else if (Regex.getCommandMatcher(command, Regex.showGraveyard).matches())
            GraveyardView.showGraveyard(GameplayController.getInstance().getGameplay().getCurrentPlayer().getField().getGraveyard());
        else if (Regex.getCommandMatcher(command, Regex.showSelectedCard).matches()) showCard();
        else if (Regex.getCommandMatcher(command, Regex.summon).matches()) summon();
        else if (Regex.getCommandMatcher(command, Regex.set).matches()) set();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.setPosition)).matches()) setPosition(matcher);
        else if (Regex.getCommandMatcher(command, Regex.surrender).matches())
            GameplayController.getInstance().surrender();
        else if (Regex.getCommandMatcher(command, Regex.flipSummon).matches()) flipSummon();
        else if (Regex.getCommandMatcher(command, Regex.activateEffect).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.attack)).matches()) attack(matcher);
        else if (Regex.getCommandMatcher(command, Regex.directAttack).matches()) directAttack();
        else if (Regex.getCommandMatcher(command, Regex.cancelAction).matches()) cancelAction();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.increaseMoneyCheatCode)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.increaseLifePointsCheatCode)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.forceAddCardCheatCode)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.setWinnerCheatCode)).matches()) ;
        else System.err.println("invalid command");
    }

    private void flipSummon() {
        try {
            GameplayController.getInstance().flipSummon();
            System.out.println("flip summoned successfully");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void directAttack() {
        try {
            String message = GameplayController.getInstance().directAttack();
            System.out.println(message);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void attack(Matcher matcher) {
        String monsterId = matcher.group("monsterId");
        try {
            StringBuilder message = GameplayController.getInstance().attack(monsterId);
            System.out.println(message);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void setPosition(Matcher matcher) {
        try {
            boolean isAttack = matcher.group(1).matches("^attack$");
            GameplayController.getInstance().changePosition(isAttack);
            System.out.println("monster card position changed successfully");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void set() {
        try {
            GameplayController.getInstance().set();
            System.out.println("set successfully");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void summon() {
        try {
            GameplayController.getInstance().summon();
            System.out.println("summoned successfully");
        } catch (Exception e) {
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
        String monsterId = matcher.group("monsterId");
        String spellId = matcher.group("handId");
        String handId = matcher.group("handId");
        String isField = matcher.group("isField");
        boolean isOpponent = true;
        if (matcher.group("oppo1") == null && matcher.group("oppo2") == null) isOpponent = false;
        try {
            if (monsterId != null) GameplayController.getInstance().selectCard(monsterId, "monster", isOpponent);
            else if (spellId != null) GameplayController.getInstance().selectCard(spellId, "spell", isOpponent);
            else if (handId != null) GameplayController.getInstance().selectCard(handId, "hand", isOpponent);
            else if (isField != null) GameplayController.getInstance().selectCard(isField, "fieldZone", isOpponent);
            System.out.println("card selected");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setTributeMode(boolean tributeMode) {
        this.tributeMode = tributeMode;
    }

    public void setGraveyardMode(boolean graveyardMode) {
        this.graveyardMode = graveyardMode;
    }

    public void setRitualMode(boolean ritualMode) {
        this.ritualMode = ritualMode;
    }

    private void cancelAction() {

    }

    private void tributeSummon(Matcher matcher) {

    }
}
