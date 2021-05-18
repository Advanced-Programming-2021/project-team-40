package View;

import Controller.DuelController.GameplayController;
import Controller.ProgramController.ProgramController;
import Controller.ProgramController.Regex;
import Gameplay.SpellAndTrapActivationType;
import Database.Cards.CardType;
import Database.Cards.Monster;
import Gameplay.Gameplay;
import Gameplay.MonsterFieldArea;
import Gameplay.Player;
import View.Exceptions.*;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class GameplayView {
    private static GameplayView gameplayView;
    private boolean isFirstOfGame = true;

    private GameplayView() {

    }

    public static GameplayView getInstance() {
        if (gameplayView == null) gameplayView = new GameplayView();
        return gameplayView;
    }

    public void run(String command) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(command, Regex.selectMonsterCard)).matches()) selectCard(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.selectSpellCard)).matches()) selectCard(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.selectHandCard)).matches()) selectCard(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.selectFieldZoneCard)).matches()) selectCard(matcher);
        else if (Regex.getCommandMatcher(command, Regex.deselectCard).matches()) deselectCard();
        else if (Regex.getCommandMatcher(command, Regex.nextPhase).matches())
            GameplayController.getInstance().goToNextPhase();
        else if (Regex.getCommandMatcher(command, Regex.showGraveyard).matches()) graveyardMode();
        else if (Regex.getCommandMatcher(command, Regex.showSelectedCard).matches()) showCard();
        else if (Regex.getCommandMatcher(command, Regex.summon).matches()) summon();
        else if (Regex.getCommandMatcher(command, Regex.set).matches()) set();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.setPosition)).matches()) setPosition(matcher);
        else if (Regex.getCommandMatcher(command, Regex.surrender).matches())
            GameplayController.getInstance().surrender();
        else if (Regex.getCommandMatcher(command, Regex.flipSummon).matches()) flipSummon();
        else if (Regex.getCommandMatcher(command, Regex.activateEffect).matches())
            activateEffect(SpellAndTrapActivationType.NORMAL);
        else if (Regex.getCommandMatcher(command, Regex.directAttack).matches()) directAttack();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.attack)).matches()) attack(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.addCardToHandCheatCode)).matches())
            forceAddCard(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.increaseMoneyCheatCode)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.increaseLifePointsCheatCode)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.forceAddCardCheatCode)).matches()) ;
        else if ((matcher = Regex.getCommandMatcher(command, Regex.setWinnerCheatCode)).matches()) ;
        else System.out.println("invalid command");
        gameplayView.showBoard();
    }

    private void forceAddCard(Matcher matcher) {
        String cardName = matcher.group("cardName");
        try {
            GameplayController.getInstance().forceAddCard(cardName);
        } catch (InvalidCardNameException e) {
            System.out.println(e.getMessage());
        }
    }


    private void showBoard() {
        FieldView.showBoard(GameplayController.getInstance().gameplay.getOpponentPlayer(), GameplayController.getInstance().gameplay.getCurrentPlayer());
    }

    private void activateEffect(SpellAndTrapActivationType type) {
        try {
            GameplayController.getInstance().activateEffect(type);
            System.out.println("spell/trap activated");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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

    public void selectRitualMonster() throws NoCardIsSelectedException, CommandCancellationException {
        Gameplay gameplay = GameplayController.getInstance().getGameplay();
        GameplayController.getInstance().deselectCard();
        String cardInput;
        while (true) {
            Matcher matcher;
            cardInput = ProgramController.getInstance().getScanner().nextLine();
            if ((matcher = Regex.getCommandMatcher(cardInput, Regex.selectHandCard)).matches())
                selectCard(matcher);
            else if (cardInput.matches(Regex.cancelAction)) {
                //TODO: more stuff here
                throw new CommandCancellationException("ritual summon");
            } else System.out.println("you should ritual summon right now");
            if (gameplay.getSelectedField() != null) {
                try {
                    if (!(gameplay.getSelectedField().getCard() instanceof Monster))
                        throw new InvalidCardSelectionException();
                    if (!((Monster) gameplay.getSelectedField().getCard()).getCardType().equals(CardType.RITUAL))
                        throw new InvalidCardSelectionException();
                    break;
                } catch (Exception e) {
                    deselectCard();
                    System.out.println(e.getMessage());
                }
            }
        }
        while (true) {
            cardInput = ProgramController.getInstance().getScanner().nextLine();
            if (cardInput.matches(Regex.cancelAction)) {
                //TODO: more things should be done here
                throw new CommandCancellationException("ritual summon");
            }
            if (cardInput.matches(Regex.summon)) {
                summon();
                break;
            } else if (cardInput.matches(Regex.set)) {
                set();
                break;
            } else System.out.println("you should ritual summon right now");
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
            else if (tributesInput.matches(Regex.cancelAction)) throw new CommandCancellationException("tribute");
            else if (tributesInput.matches(Regex.help))
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

    private void graveyardMode() {
        GraveyardView.showGraveyard(GameplayController.getInstance().getGameplay().getCurrentPlayer().getField().getGraveyard());
        String command;
        while (true) {
            command = ProgramController.getInstance().getScanner().nextLine();
            if (command.matches(Regex.back)) break;
            if (command.matches(Regex.showGraveyard))
                GraveyardView.showGraveyard(GameplayController.getInstance().getGameplay().getCurrentPlayer().getField().getGraveyard());
            else System.out.println("invalid command");
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
            if (message != null) System.out.println(message);
            GameplayController.getInstance().checkWinningConditions();
        } catch (Exception e) {
            GameplayController.getInstance().getGameplay().setAttacker(null);
            GameplayController.getInstance().getGameplay().setBeingAttacked(null);
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

    public void selectCard(Matcher matcher) {
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

    public boolean isFirstOfGame() {
        return isFirstOfGame;
    }

    public void setFirstOfGame(boolean firstOfGame) {
        isFirstOfGame = firstOfGame;
    }

    public String[] ritualTribute() throws Exception {
        String inputRegex = "^(\\d )+$";
        String input;
        String[] ids;
        while (true) {
            input = ProgramController.getInstance().getScanner().nextLine();
            if (input.matches(Regex.cancelAction)) {
                //TODO: more things should be done here
                throw new CommandCancellationException("ritual summon");
            }
            try {
                if (input.matches(inputRegex)) {
                    ids = input.split(" ");
                    if (!GameplayController.getInstance().isRitualInputsValid(ids))
                        throw new Exception("selected monsters levels don't match with ritual monster");
                    break;
                } else System.out.println("you should ritual summon right now");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return ids;
    }

    public boolean spellAndTrapActivationPrompt() {
        String input;
        while (true) {
            input = ProgramController.getInstance().getScanner().nextLine();
            if (input.equalsIgnoreCase("yes")) return true;
            else if (input.equalsIgnoreCase("no")) return false;
            else System.out.println("it's not your turn to play this kind of moves");
        }
    }

    public void spellAndTrapToChainPrompt(SpellAndTrapActivationType type) throws ActionNotPossibleException, AttackNotPossibleException {
        String input;
        while (true) {
            Matcher matcher;
            input = ProgramController.getInstance().getScanner().nextLine();
            if ((matcher = Regex.getCommandMatcher(input, Regex.selectSpellCard)).matches())
                selectCard(matcher);
            else if (input.matches(Regex.deselectCard)) deselectCard();
            else if (input.matches(Regex.activateEffect)) if (chainActivateEffect(type)) break;
            else System.out.println("it's not your turn to play this kind of moves");
        }
    }

    private boolean chainActivateEffect(SpellAndTrapActivationType type) throws ActionNotPossibleException, AttackNotPossibleException {
        try {
            GameplayController.getInstance().activateEffect(type);
            System.out.println("spell/trap activated");
            return true;
        } catch (InvalidActivateException | RitualSummonNotPossibleException | AlreadyActivatedException | SpecialSummonNotPossibleException | CommandCancellationException | MonsterZoneFullException | WrongPhaseForSpellException | SpellZoneFullException | PreparationNotReadyException | NoCardIsSelectedException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}