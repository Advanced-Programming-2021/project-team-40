package View;

import Controller.DuelController.GameplayController;
import Controller.ProgramController.ProgramController;
import Controller.ProgramController.Regex;
import Database.Cards.CardType;
import Database.Cards.Monster;
import Database.User;
import Gameplay.*;
import View.Exceptions.*;

import java.util.ArrayList;
import java.util.Random;
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
        else if (Regex.getCommandMatcher(command, Regex.surrender).matches()) surrender();
        else if (Regex.getCommandMatcher(command, Regex.flipSummon).matches()) flipSummon();
        else if (Regex.getCommandMatcher(command, Regex.activateEffect).matches())
            activateEffect(SpellAndTrapActivationType.NORMAL);
        else if (Regex.getCommandMatcher(command, Regex.directAttack).matches()) directAttack();
        else if ((matcher = Regex.getCommandMatcher(command, Regex.attack)).matches()) attack(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.addCardToHandCheatCode)).matches())
            forceAddCardCheat(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.increaseLifePointsCheatCode)).matches())
            increaseLifePointsCheat(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.forceAddCardCheatCode)).matches())
            forceAddCardCheat(matcher);
        else if ((matcher = Regex.getCommandMatcher(command, Regex.setWinnerCheatCode)).matches())
            setWinnerCheat(matcher);
        else System.out.println("invalid command");
        String message = GameplayController.getInstance().checkWinningConditions();
        if (message != null) {
            System.out.println(message);
            GameplayController.getInstance().doPhaseAction();
        }
        GameplayController.getInstance().calculateFieldZoneEffects();
        gameplayView.showBoard();
    }

    private void surrender() {
        String message = GameplayController.getInstance().surrender();
        System.out.println(message);
        GameplayController.getInstance().doPhaseAction();
    }

    private void setWinnerCheat(Matcher matcher) {
        String nickname = matcher.group("nickname");
        String message = GameplayController.getInstance().setWinnerCheat(nickname);
        if (message == null) return;
        System.out.println(message);
        GameplayController.getInstance().doPhaseAction();
    }

    public void discardACard() {
        while (true) {
            try {
                Matcher matcher;
                String command = ProgramController.getInstance().getScanner().nextLine();
                if ((matcher = Regex.getCommandMatcher(command, Regex.selectHandCard)).matches()) {
                    selectCard(matcher);
                    GameplayController.getInstance().discardSelectedCard();
                    break;
                }
            } catch (InvalidCardSelectionException e) {
                System.out.println(e.getMessage());
                deselectCard();
            }
        }
    }

    private void forceAddCardCheat(Matcher matcher) {
        String cardName = matcher.group("cardName");
        try {
            GameplayController.getInstance().forceAddCard(cardName);
        } catch (InvalidCardNameException e) {
            System.out.println(e.getMessage());
        }
    }

    private void increaseLifePointsCheat(Matcher matcher) {
        int amount = Integer.parseInt(matcher.group("amount"));
        GameplayController.getInstance().increaseLifePointsCheat(amount);
    }


    public void showBoard() {
        if (GameplayController.getInstance().gameplay == null) return;
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
        Matcher matcher;
        System.out.println("select the ritual monster you want to summon/set:");
        while (true) {
            cardInput = ProgramController.getInstance().getScanner().nextLine();
            if ((matcher = Regex.getCommandMatcher(cardInput, Regex.selectHandCard)).matches())
                selectCard(matcher);
            else if (cardInput.matches(Regex.cancelAction)) {
                //TODO: more stuff here
                throw new CommandCancellationException("ritual summon");
            } else if (cardInput.matches(Regex.showSelectedCard)) showCard();
            else System.out.println("you should ritual summon right now");
            if (gameplay.getSelectedField() != null) {
                try {
                    if (!(gameplay.getSelectedField().getCard() instanceof Monster))
                        throw new InvalidCardSelectionException();
                    if (!((Monster) gameplay.getSelectedField().getCard()).getCardType().equals(CardType.RITUAL))
                        throw new InvalidCardSelectionException();
                    break;
                } catch (InvalidCardSelectionException e) {
                    System.out.println("you should ritual summon right now");
                }
            }
            gameplayView.showBoard();
        }
        while (true) {
            cardInput = ProgramController.getInstance().getScanner().nextLine();
            if (cardInput.matches(Regex.cancelAction)) {
                //TODO: more things should be done here
                throw new CommandCancellationException("Ritual summon");
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
            gameplayView.showBoard();
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
            //TODO: show board?
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
        String id = "-1";
        if (!matcher.group("type").matches("--field|-f")) id = matcher.group("id");
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

    public String[] ritualTribute() {
        String inputRegex = "^(\\d+\\s?)+$";
        String input;
        String[] ids;
        System.out.println("select cards you want to tribute for ritual summon:");
        while (true) {
            input = ProgramController.getInstance().getScanner().nextLine();
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
            gameplayView.showBoard();
        }
        return ids;
    }

    public boolean spellAndTrapActivationPrompt() {
        String input;
        System.out.println("do you want to activate your trap and spell?");
        while (true) {
            input = ProgramController.getInstance().getScanner().nextLine();
            if (input.equalsIgnoreCase("yes")) return true;
            else if (input.equalsIgnoreCase("no")) return false;
            else System.out.println("it's not your turn to play this kind of moves");
            gameplayView.showBoard();
            //TODO: check correct side
        }
    }

    public void spellAndTrapToChainPrompt(SpellAndTrapActivationType type) throws ActionNotPossibleException, AttackNotPossibleException {
        String input;
        System.out.println("select the Trap card you want to chain:");
        while (true) {
            Matcher matcher;
            input = ProgramController.getInstance().getScanner().nextLine();
            if ((matcher = Regex.getCommandMatcher(input, Regex.selectSpellCard)).matches())
                selectCard(matcher);
            else if (input.matches(Regex.showSelectedCard)) showCard();
            else if (input.matches(Regex.deselectCard)) deselectCard();
            else if (input.matches(Regex.help)) System.out.println("select the Trap card you want to chain");
            else if (input.matches(Regex.activateEffect)) {
                if (chainActivateEffect(type)) break;
            } else System.out.println("it's not your turn to play this kind of moves");
            gameplayView.showBoard();
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

    public void selectMonsterForEquip(FieldArea equipSpell) throws CommandCancellationException {
        System.out.println("choose your card to equip:");
        Matcher matcher;
        while (true) {
            String input = ProgramController.getInstance().getScanner().nextLine();
            if (input.matches(Regex.cancelAction)) throw new CommandCancellationException("Equip");
            else if (input.matches(Regex.help))
                System.out.println("choose you card for equip; or cancel the operation");
            else if ((matcher = Regex.getCommandMatcher(input, Regex.selectMonsterCard)).matches()) {
                selectCard(matcher);
                if (GameplayController.getInstance().gameplay.getSelectedField() == null) continue;
                if (equipSpell.getCard().equipEffect.isMonsterCorrect((MonsterFieldArea) GameplayController.getInstance().gameplay.getSelectedField()))
                    return;
                else {
                    System.out.println("Wrong Card Selection!");
                    deselectCard();
                }
            } else System.out.println("you have to equip a monster right now");
            gameplayView.showBoard();
        }
    }

    public void utiliseSideDeckPrompt(Player player) {
        System.out.println("it's " + player.getUser().getNickname() + "'s turn to utilise their side deck" );
        System.out.println("type \"card switch --main <main> --side <side>\" to switch cards between side deck and main deck, or \"done\"");
        Matcher matcher;
        while (true) {
            DeckView.showDeckMidGame(player.getPlayingDeck());
            String input = ProgramController.getInstance().getScanner().nextLine();
            if (input.matches("done")) break;
            else if ((matcher = Regex.getCommandMatcher(input, Regex.switchCard)).matches()) {
                try {
                    GameplayController.getInstance().getGameplay().switchCards(matcher, player);
                } catch (InvalidSideSwitchException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}