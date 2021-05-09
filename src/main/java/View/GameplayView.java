package View;

import Controller.DuelController.GameplayController;
import Controller.ProgramController.ProgramController;
import Controller.ProgramController.Regex;
import Gameplay.MonsterFieldArea;
import Gameplay.Player;
import View.Exceptions.*;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class GameplayView {
    private static GameplayView gameplayView;
    private boolean isFirstOfGame;
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
        if (graveyardMode) {
            if (Regex.getCommandMatcher(command, Regex.back).matches()) {
                graveyardMode = false;
                System.out.println("back to game");
            } else System.out.println("invalid command");
        } else if (ritualMode) {

        } else if ((matcher = Regex.getCommandMatcher(command, Regex.selectMonsterCard)).matches()) selectCard(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.selectSpellCard)).matches()) selectCard(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.selectHandCard)).matches()) selectCard(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.selectFieldZoneCard)).matches()) selectCard(matcher);
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
        else System.out.println("invalid command");
    }

    private void activateEffect() {
        try {
            GameplayController.getInstance().activateEffect();
            System.out.println("spell activated");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void showGraveyard() {
        setGraveyardMode(true);
        GraveyardView.showGraveyard(GameplayController.getInstance().getGameplay().getCurrentPlayer().getField().getGraveyard());
    }

    private void tributeSummon(Matcher matcher) {

    }

    private void flipSummon() {
        try {
            GameplayController.getInstance().flipSummon();
            System.out.println("flip summoned successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void directAttack() {
        try {
            String message = GameplayController.getInstance().directAttack();
            System.out.println(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<MonsterFieldArea> getTributes(int number) throws NotEnoughCardsException, CommandCancellationException {
        String[] tributeRegex = {" ", "\\d+", "\\d+ \\d+", "\\d+ \\d+ \\d+"};
        String tributesInput;
        if (getNumberOfPlayerMonsters(GameplayController.getInstance().getGameplay().getCurrentPlayer()) < number)
            throw new NotEnoughCardsException();
        while (true) {
            tributesInput = ProgramController.getInstance().getScanner().nextLine();
            if (tributesInput.matches(tributeRegex[number])) break;
            else if (tributesInput.matches("cancel")) throw new CommandCancellationException("tribute");
            else if (tributesInput.matches("help"))
                System.out.println("type " + number + " locations for monsters to tribute, or cancel");
            else System.out.println("type exactly " + number + " locations to tribute");
        }
        String[] splitTributeInputs = tributesInput.split(" ");
        try {
            ArrayList<MonsterFieldArea> toReturn = new ArrayList<>();
            MonsterFieldArea fieldAreaToAdd;
            for (int i = 0; i < number; i++) {
                if (GameplayController.getInstance().isLocationNumberInvalid(splitTributeInputs[i]))
                    throw new InvalidCardSelectionException();
                if ((fieldAreaToAdd = GameplayController.getInstance().getGameplay().getCurrentPlayer().getField().getMonstersFieldById(Integer.parseInt(splitTributeInputs[i]))).getCard() == null)
                    throw new InvalidCardAddressException();
                toReturn.add(fieldAreaToAdd);
            }
            return toReturn;

        } catch (InvalidCardAddressException | InvalidCardSelectionException e) {
            System.out.println(e.getMessage());
            return getTributes(number);
        }
    }

    private int getNumberOfPlayerMonsters(Player currentPlayer) {
        int monsters = 0;
        MonsterFieldArea[] monsterFieldAreas = currentPlayer.getField().getMonstersField();
        for (MonsterFieldArea area : monsterFieldAreas) {
            if (area.getCard() != null) monsters++;
        }
        return monsters;
    }

    private void attack(Matcher matcher) {
        String monsterId = matcher.group("monsterId");
        try {
            StringBuilder message = GameplayController.getInstance().attack(monsterId);
            System.out.println(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void setPosition(Matcher matcher) {
        try {
            boolean isAttack = matcher.group(1).matches("^attack$");
            GameplayController.getInstance().changePosition(isAttack);
            System.out.println("monster card position changed successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void set() {
        try {
            GameplayController.getInstance().set();
            System.out.println("set successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
        String id = matcher.group("id");
        String type = matcher.group("type");
        boolean isOpponent = true;
        if (matcher.group("isOpponent") == null) isOpponent = false;
        try {
            GameplayController.getInstance().selectCard(id, type, isOpponent);
            System.out.println("card selected");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void setGraveyardMode(boolean graveyardMode) {
        this.graveyardMode = graveyardMode;
    }

    public void setRitualMode(boolean ritualMode) {
        this.ritualMode = ritualMode;
    }

    public boolean isFirstOfGame() {
        return isFirstOfGame;
    }

    public void setFirstOfGame(boolean firstOfGame) {
        isFirstOfGame = firstOfGame;
    }
}
