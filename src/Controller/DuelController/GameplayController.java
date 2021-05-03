package Controller.DuelController;


import Controller.ProgramController.Regex;
import GamePlay.FieldArea;
import GamePlay.Gameplay;
import GamePlay.Phase;
import GamePlay.Player;
import View.Exceptions.InvalidCardSelectionException;
import View.Exceptions.NoCardFoundException;
import View.Exceptions.NoCardIsSelectedException;

import java.util.regex.Matcher;

public class GameplayController {
    private static GameplayController gameplayController = null;
    private Gameplay gameplay;
    //TODO add view

    private GameplayController() {
    }

    public static GameplayController getInstance() {
        if (gameplayController == null)
            gameplayController = new GameplayController();
        return gameplayController;
    }

    public void run(String command) {
        Matcher matcher;
        if (gameplay.getCurrentPhase() == Phase.DRAW_PHASE) {
            PlayerController.getInstance().drawACard(gameplay.getCurrentPlayer());
            if (gameplay.getCurrentPlayer().getPlayingDeck().getMainCards().size() == 0) ;//TODO declare winner
        }
        if (gameplay.getCurrentPhase() == Phase.STANDBY_PHASE) {
            PhaseController.getInstance().goToNextPhase(gameplay.getCurrentPhase());
        }
        if (gameplay.getCurrentPhase() == Phase.MAIN_PHASE_ONE){
            //TODO show graveyard
        }
        if ((matcher = Regex.getCommandMatcher(command, Regex.selectCard)).matches()) {
            try {
                selectCard(matcher);
                System.out.println("card selected");
            } catch (InvalidCardSelectionException | NoCardFoundException e) {
                System.out.println(e.getMessage());
            }
        } else if (Regex.getCommandMatcher(command, Regex.deselectCard).matches()) {
            try {
                deselectCard();
                System.out.println("card deselected");
            } catch (NoCardIsSelectedException e) {
                System.out.println(e.getMessage());
            }
        } else if (Regex.getCommandMatcher(command, Regex.nextPhase).matches())
            gameplay.setCurrentPhase(PhaseController.getInstance().goToNextPhase(gameplay.getCurrentPhase()));
        else if ((matcher = Regex.getCommandMatcher(command, Regex.showGraveyard)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.showSelectedCard)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.surrender)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.summon)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.set)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.setPosition)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.flipSummon)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.activateEffect)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.attack)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.flipSummon)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.directAttack)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.increaseMoneyCheatCode)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.increaseLifePointsCheatCode)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.forceAddCardCheatCode)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.setWinnerCheatCode)).matches()) ;
        else System.out.println("invalid command");
    }

    private void selectCard(Matcher matcher) throws InvalidCardSelectionException, NoCardFoundException {
        FieldArea fieldArea;
        if (matcher.group("monsterId") != null) {
            String monsterId = matcher.group("monsterId");
            if (isNumberInvalid(monsterId)) throw new InvalidCardSelectionException();
            int id = Integer.parseInt(monsterId);
            if (matcher.group("oppo1") == null || matcher.group("oppo2") == null)
                fieldArea = gameplay.getCurrentPlayer().getField().getMonstersFieldById(id);
            else fieldArea = gameplay.getOpponentPlayer().getField().getMonstersFieldById(id);
            if (fieldArea.getCard() == null) throw new NoCardFoundException();
            gameplay.setSelectedCard(fieldArea);
        }
        if (matcher.group("spellId") != null) {
            String spellId = matcher.group("spellId");
            if (isNumberInvalid(spellId)) throw new InvalidCardSelectionException();
            int id = Integer.parseInt(spellId);
            if (matcher.group("oppo1") == null || matcher.group("oppo2") == null)
                fieldArea = gameplay.getCurrentPlayer().getField().getSpellAndTrapFieldById(id);
            else fieldArea = gameplay.getOpponentPlayer().getField().getSpellAndTrapFieldById(id);
            if (fieldArea.getCard() == null) throw new NoCardFoundException();
            gameplay.setSelectedCard(fieldArea);
        }
        if (matcher.group("isField") != null) {
            if (matcher.group("oppo1") == null || matcher.group("oppo2") == null)
                fieldArea = gameplay.getCurrentPlayer().getField().getFieldZone();
            else fieldArea = gameplay.getOpponentPlayer().getField().getFieldZone();
            if (fieldArea.getCard() == null) throw new NoCardFoundException();
            gameplay.setSelectedCard(fieldArea);
        }
    }

    private void deselectCard() throws NoCardIsSelectedException {
        if (gameplay.getSelectedCard() == null) throw new NoCardIsSelectedException();
        gameplay.setSelectedCard(null);
    }

    private boolean isNumberInvalid(String string) {
        if (string.matches("^\\d+$")) {
            int id = Integer.parseInt(string);
            return id <= 0 || id >= 6;
        }
        return true;
    }

    public void setGameplay(Gameplay gameplay) {
        this.gameplay = gameplay;
    }

    public Gameplay getGameplay() {
        return gameplay;
    }

    public void changeTurn() {

    }
}