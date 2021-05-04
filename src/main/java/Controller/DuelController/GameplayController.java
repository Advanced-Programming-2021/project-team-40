package Controller.DuelController;


import Controller.ProgramController.Regex;
import Database.Cards.Card;
import Gameplay.FieldArea;
import Gameplay.Gameplay;
import Gameplay.MonsterFieldArea;
import Gameplay.Phase;
import View.CardView;
import View.Exceptions.*;
import View.GraveyardView;

import java.util.regex.Matcher;

public class GameplayController {
    private static GameplayController gameplayController = null;
    public Gameplay gameplay;
    //TODO add view

    private GameplayController() {
    }

    public static GameplayController getInstance() {
        if (gameplayController == null)
            gameplayController = new GameplayController();
        return gameplayController;
    }

    public void setGameplay(Gameplay gameplay) {
        this.gameplay = gameplay;
    }

    public void run(String command) {
        Matcher matcher;
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
        } else if (Regex.getCommandMatcher(command, Regex.nextPhase).matches()) goToNextPhase();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.showGraveyard)).matches())
            GraveyardView.showGraveyard(gameplay.getCurrentPlayer().getField().getGraveyard());
        else if ((matcher = Regex.getCommandMatcher(command, Regex.showSelectedCard)).matches()) {
            try {
                showCard();
            } catch (NoCardIsSelectedException e) {
                System.out.println(e.getMessage());
            }
        } else if ((matcher = Regex.getCommandMatcher(command, Regex.summon)).matches()) {
            try {
                putMonsterCard(true);
                System.out.println("summoned successfully");
            } catch (NoCardIsSelectedException | AlreadySummonedException | InvalidSummonException | MonsterZoneFullException | WrongPhaseException e) {
                System.out.println(e.getMessage());
                ;
            }
        } else if ((matcher = Regex.getCommandMatcher(command, Regex.set)).matches()) {
            try {
                putMonsterCard(false);
                System.out.println("set successfully");
            } catch (NoCardIsSelectedException | AlreadySummonedException | InvalidSummonException | MonsterZoneFullException | WrongPhaseException e) {
                System.out.println(e.getMessage());
                ;
            }
        } else if ((matcher = Regex.getCommandMatcher(command, Regex.setPosition)).matches()) {
            try {
                boolean isAttack = matcher.group(1).matches("attack");
                changePosition(isAttack);
                System.out.println("monster card position changed successfully");
            } catch (NoCardIsSelectedException | InvalidChangePositionException | WrongPhaseException | AlreadyInPositionException | AlreadySetPositionException e) {
                System.out.println(e.getMessage());
            }
        } else if ((matcher = Regex.getCommandMatcher(command, Regex.surrender)).matches()) ;
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

    private void goToNextPhase() {
        switch (gameplay.getCurrentPhase()) {

            case DRAW_PHASE -> {
                gameplay.setCurrentPhase(Phase.STANDBY_PHASE);
                System.out.println("phase: standby phase");
            }
            case STANDBY_PHASE -> {
                gameplay.setCurrentPhase(Phase.MAIN_PHASE_ONE);
                System.out.println("phase: main phase one");
            }
            case MAIN_PHASE_ONE -> {
                gameplay.setCurrentPhase(Phase.BATTLE_PHASE);
                System.out.println("phase: battle phase");
            }
            case BATTLE_PHASE -> {
                gameplay.setCurrentPhase(Phase.MAIN_PHASE_TW0);
                System.out.println("phase: main phase two");
            }
            case MAIN_PHASE_TW0 -> {
                gameplay.setCurrentPhase(Phase.END_PHASE);
                System.out.println("phase: end phase");
            }
            case END_PHASE -> {
                gameplay.setCurrentPhase(Phase.DRAW_PHASE);
                System.out.println(gameplay.getCurrentPlayer().getUser().getNickname() + "'s turn is completed");
            }
        }
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
            gameplay.setSelectedField(fieldArea);
        }
        if (matcher.group("spellId") != null) {
            String spellId = matcher.group("spellId");
            if (isNumberInvalid(spellId)) throw new InvalidCardSelectionException();
            int id = Integer.parseInt(spellId);
            if (matcher.group("oppo1") == null || matcher.group("oppo2") == null)
                fieldArea = gameplay.getCurrentPlayer().getField().getSpellAndTrapFieldById(id);
            else fieldArea = gameplay.getOpponentPlayer().getField().getSpellAndTrapFieldById(id);
            if (fieldArea.getCard() == null) throw new NoCardFoundException();
            gameplay.setSelectedField(fieldArea);
        }
        if (matcher.group("isField") != null) {
            if (matcher.group("oppo1") == null || matcher.group("oppo2") == null)
                fieldArea = gameplay.getCurrentPlayer().getField().getFieldZone();
            else fieldArea = gameplay.getOpponentPlayer().getField().getFieldZone();
            if (fieldArea.getCard() == null) throw new NoCardFoundException();
            gameplay.setSelectedField(fieldArea);
        }
    }

    private void deselectCard() throws NoCardIsSelectedException {
        if (gameplay.getSelectedField().getCard() == null) throw new NoCardIsSelectedException();
        gameplay.setSelectedField(null);
    }

    private void showCard() throws NoCardIsSelectedException {
        Card card = gameplay.getSelectedField().getCard();
        if (card == null) throw new NoCardIsSelectedException();
        CardView.showCard(card);
    }

    private boolean isNumberInvalid(String string) {
        if (string.matches("^\\d+$")) {
            int id = Integer.parseInt(string);
            return id <= 0 || id >= 6;
        }
        return true;
    }

    private void putMonsterCard(boolean isAttack) throws NoCardIsSelectedException, AlreadySummonedException, InvalidSummonException, MonsterZoneFullException, WrongPhaseException {
        if (gameplay.getSelectedField().getCard() == null) throw new NoCardIsSelectedException();
        if (!gameplay.getSelectedField().canBePutOnBoard()) throw new InvalidSummonException();
        if (!gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_ONE) && !gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_TW0))
            throw new WrongPhaseException();
        MonsterFieldArea monsterFieldArea = gameplay.getCurrentPlayer().getField().getFreeMonsterFieldArea();
        if (monsterFieldArea == null) throw new MonsterZoneFullException();
        if (gameplay.hasPlacedMonster()) throw new AlreadySummonedException();
        monsterFieldArea.putCard(gameplay.getSelectedField().getCard(), isAttack);
        gameplay.setHasPlacedMonster(true);
        deselectCard();
    }

    private void changePosition(boolean isAttack) throws NoCardIsSelectedException, InvalidChangePositionException, WrongPhaseException, AlreadyInPositionException, AlreadySetPositionException {
        FieldArea fieldArea = gameplay.getSelectedField();
        if (fieldArea.getCard() == null) throw new NoCardIsSelectedException();
        if (!(fieldArea instanceof MonsterFieldArea)) throw new InvalidChangePositionException();
        if (!gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_ONE) && !gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_TW0))
            throw new WrongPhaseException();
        if (((MonsterFieldArea) fieldArea).isAttack() == isAttack) throw new AlreadyInPositionException();
        if (((MonsterFieldArea) fieldArea).hasSwitchedMode()) throw new AlreadySetPositionException();
        ((MonsterFieldArea) fieldArea).changePosition();
    }
}