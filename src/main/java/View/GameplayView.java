package View;

import Controller.DuelController.GameplayController;
import Controller.ProgramController.Regex;
import View.Exceptions.*;

import java.util.regex.Matcher;

public class GameplayView {
    private static GameplayView gameplayView;
    private boolean oneTributeSetMode;
    private boolean twoTributeSetMode;
    private boolean oneTributeSummonMode;
    private boolean twoTributeSummonMode;
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
        if (oneTributeSetMode) {
            if (Regex.getCommandMatcher(command, Regex.cancelAction).matches()) {
                oneTributeSetMode = false;
                System.out.println("tribute cancelled");
            } else if ((matcher = Regex.getCommandMatcher(command, Regex.tributeOne)).matches()) oneTributeSet(matcher);
            else System.err.println("invalid command");
        } else if (twoTributeSetMode) {
            if (Regex.getCommandMatcher(command, Regex.cancelAction).matches()) {
                twoTributeSetMode = false;
                System.out.println("tribute cancelled");
            } else if ((matcher = Regex.getCommandMatcher(command, Regex.tributeTwo)).matches()) twoTributeSet(matcher);
            else System.err.println("invalid command");
        } else if (oneTributeSummonMode) {
            if (Regex.getCommandMatcher(command, Regex.cancelAction).matches()) {
                oneTributeSummonMode = false;
                System.out.println("tribute cancelled");
            } else if ((matcher = Regex.getCommandMatcher(command, Regex.tributeOne)).matches())
                oneTributeSummon(matcher);
            else System.err.println("invalid command");
        } else if (twoTributeSummonMode) {
            if (Regex.getCommandMatcher(command, Regex.cancelAction).matches()) {
                twoTributeSummonMode = false;
                System.out.println("tribute cancelled");
            } else if ((matcher = Regex.getCommandMatcher(command, Regex.tributeOne)).matches())
                twoTributeSummon(matcher);
            else System.err.println("invalid command");
        } else if (graveyardMode) {
            if (Regex.getCommandMatcher(command, Regex.back).matches()) {
                graveyardMode = false;
                System.out.println("back to game");
            } else System.err.println("invalid command");
        } else if (ritualMode) {

        } else if ((matcher = Regex.getCommandMatcher(command, Regex.selectCard)).matches()) selectCard(matcher);
        else if (Regex.getCommandMatcher(command, Regex.deselectCard).matches()) deselectCard();
        else if (Regex.getCommandMatcher(command, Regex.nextPhase).matches())
            GameplayController.getInstance().goToNextPhase();
        else if (Regex.getCommandMatcher(command, Regex.showGraveyard).matches()) showGraveyard();
        else if (Regex.getCommandMatcher(command, Regex.showSelectedCard).matches()) showCard();
        else if (Regex.getCommandMatcher(command, Regex.summon).matches()) summon();
        else if (Regex.getCommandMatcher(command, Regex.set).matches()) set();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.setPosition)).matches()) setPosition(matcher);
        else if (Regex.getCommandMatcher(command, Regex.surrender).matches())
            GameplayController.getInstance().surrender();
        else if (Regex.getCommandMatcher(command, Regex.flipSummon).matches()) flipSummon();
        else if (Regex.getCommandMatcher(command, Regex.activateEffect).matches()) activateEffect();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.attack)).matches()) attack(matcher);
        else if (Regex.getCommandMatcher(command, Regex.directAttack).matches()) directAttack();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.increaseMoneyCheatCode)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.increaseLifePointsCheatCode)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.forceAddCardCheatCode)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.setWinnerCheatCode)).matches()) ;
        else System.err.println("invalid command");
    }

    private void activateEffect() {
        try {
            GameplayController.getInstance().activateEffect();
            System.out.println("spell activated");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void showGraveyard() {
        setGraveyardMode(true);
        GraveyardView.showGraveyard(GameplayController.getInstance().getGameplay().getCurrentPlayer().getField().getGraveyard());
    }

    private void oneTributeSet(Matcher matcher) {
        String id = matcher.group(1);
        try {
            GameplayController.getInstance().OneTributeSet(id);
            System.out.println("set successfully");
            oneTributeSetMode = false;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void twoTributeSet(Matcher matcher) {
        String id1 = matcher.group(1);
        String id2 = matcher.group(2);
        try {
            GameplayController.getInstance().TwoTributeSet(id1, id2);
            System.out.println("set successfully");
            twoTributeSetMode = false;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void oneTributeSummon(Matcher matcher) {
        String id1 = matcher.group(1);
        try {
            GameplayController.getInstance().OneTributeSummon(id1);
            System.out.println("summoned successfully");
            oneTributeSummonMode = false;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void twoTributeSummon(Matcher matcher) {
        String id1 = matcher.group(1);
        String id2 = matcher.group(2);
        try {
            GameplayController.getInstance().TwoTributeSummon(id1, id2);
            System.out.println("summoned successfully");
            twoTributeSummonMode = false;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
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
            if (!oneTributeSetMode && !twoTributeSetMode) System.out.println("set successfully");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void summon() {
        try {
            GameplayController.getInstance().summon();
            if (!oneTributeSummonMode && !twoTributeSummonMode) System.out.println("summoned successfully");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void showCard() {
        try {
            GameplayController.getInstance().showCard();
        } catch (NoCardIsSelectedException e) {
            System.err.println(e.getMessage());
        }
    }

    private void deselectCard() {
        try {
            GameplayController.getInstance().deselectCard();
            System.out.println("card deselected");
        } catch (NoCardIsSelectedException e) {
            System.err.println(e.getMessage());
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
            System.err.println(e.getMessage());
        }
    }


    public void setGraveyardMode(boolean graveyardMode) {
        this.graveyardMode = graveyardMode;
    }

    public void setRitualMode(boolean ritualMode) {
        this.ritualMode = ritualMode;
    }

    private void tributeSummon(Matcher matcher) {

    }

    public void setOneTributeSetMode(boolean oneTributeSetMode) {
        this.oneTributeSetMode = oneTributeSetMode;
    }

    public void setTwoTributeSetMode(boolean twoTributeSetMode) {
        this.twoTributeSetMode = twoTributeSetMode;
    }

    public void setTwoTributeSummonMode(boolean twoTributeSummonMode) {
        this.twoTributeSummonMode = twoTributeSummonMode;
    }

    public void setOneTributeSummonMode(boolean oneTributeSummonMode) {
        this.oneTributeSummonMode = oneTributeSummonMode;
    }
}
