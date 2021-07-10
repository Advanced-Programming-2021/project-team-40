package Controller.DuelController;


import Controller.MenuController.DeckMenuController;
import Controller.ProgramController.Menu;
import Controller.ProgramController.ProgramController;
import Database.Cards.*;
import Database.User;
import Gameplay.*;
import View.CardView;
import View.Exceptions.*;
import View.GameplayView;
import javafx.scene.control.Label;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class GameplayController {
    private static GameState gameState = GameState.NORMAL_MODE;
    private static SpellAndTrapActivationType chainType = SpellAndTrapActivationType.NORMAL;
    private static GameplayController gameplayController = null;
    public Gameplay gameplay;
    public ArrayList<MonsterFieldArea> toTributeCards = new ArrayList<>();
    public int tributeCount = 0;
    public ArrayList<FieldArea> effectSpellAndTraps = new ArrayList<>();
    private boolean possibleChainChecked = false;

    private GameplayController() {

    }

    public static GameState getGameState() {
        return gameState;
    }

    public static void setGameState(GameState gameState) {
        System.out.println(gameState);
        GameplayController.gameState = gameState;
    }

    public static SpellAndTrapActivationType getChainType() {
        return chainType;
    }

    public static void setChainType(SpellAndTrapActivationType chainType) {
        GameplayController.chainType = chainType;
    }

    public static GameplayController getInstance() {
        if (gameplayController == null)
            gameplayController = new GameplayController();
        return gameplayController;
    }

    public Gameplay getGameplay() {
        return gameplay;
    }

    public void setGameplay(Gameplay gameplay) {
        this.gameplay = gameplay;
    }

    public String doPhaseAction() throws DeckEmptiedException {
        if (gameplay == null) return null;
        switch (gameplay.getCurrentPhase()) {
            case DRAW_PHASE:
                Card card = drawCard();
                if (card == null) throw new DeckEmptiedException();
                return "new card added to the hand : " + card.getName();
            case END_PHASE:
                switchTurn();
                break;
        }
        return null;
    }

    public String goToNextPhase() {
        //TODO: console view does not work any more
        if (GameplayController.getInstance().getGameplay().getSelectedField() != null) {
            try {
                deselectCard();
            } catch (NoCardIsSelectedException ignored) {
            }
        }
        switch (gameplay.getCurrentPhase()) {
            case DRAW_PHASE:
                gameplay.setCurrentPhase(Phase.STANDBY_PHASE);
                return gameplay.getCurrentPhase().toString();
            case STANDBY_PHASE:
                gameplay.setCurrentPhase(Phase.MAIN_PHASE_ONE);
                return gameplay.getCurrentPhase().toString();
            case MAIN_PHASE_ONE:
                if (GameplayView.getInstance().isFirstOfGame()) gameplay.setCurrentPhase(Phase.END_PHASE);
                else if (!hasAttackMonster()) gameplay.setCurrentPhase(Phase.END_PHASE);
                else gameplay.setCurrentPhase(Phase.BATTLE_PHASE);
                return gameplay.getCurrentPhase().toString();
            case BATTLE_PHASE:
                gameplay.setCurrentPhase(Phase.MAIN_PHASE_TW0);
                return gameplay.getCurrentPhase().toString();
            case MAIN_PHASE_TW0:
                gameplay.setCurrentPhase(Phase.END_PHASE);
                return gameplay.getCurrentPhase().toString();
            case END_PHASE:
                gameplay.setCurrentPhase(Phase.DRAW_PHASE);
                return gameplay.getCurrentPhase().toString();
        }
        return null;
    }

    public void goToEndPhase() throws DeckEmptiedException {
        gameplay.setCurrentPhase(Phase.END_PHASE);
        System.out.println(gameplay.getCurrentPhase().toString());
        doPhaseAction();
    }

    public void setStartingPlayer() {
        int firstPlayerPick = new Random().nextInt(3);
        int secondPlayerPick = new Random().nextInt(3);
        if (firstPlayerPick > secondPlayerPick) {
            gameplay.setCurrentPlayer(gameplay.getPlayerOne());
            gameplay.setOpponentPlayer(gameplay.getPlayerTwo());
        } else if (secondPlayerPick > firstPlayerPick) {
            gameplay.setCurrentPlayer(gameplay.getPlayerTwo());
            gameplay.setOpponentPlayer(gameplay.getPlayerOne());
        } else setStartingPlayer();
    }

    public void dealCardsAtBeginning() {
        for (int i = 0; i < 5; i++) {
            moveTopCardFromDeckToHand(gameplay.getCurrentPlayer());
            moveTopCardFromDeckToHand(gameplay.getOpponentPlayer());
        }
    }

    public Card drawCard() {
        return moveTopCardFromDeckToHand(gameplay.getCurrentPlayer());
    }

    public void switchTurn() {
        gameplay.getCurrentPlayer().getField().endTurnActions();
        Player temp = gameplay.getOpponentPlayer();
        gameplay.setOpponentPlayer(gameplay.getCurrentPlayer());
        gameplay.setCurrentPlayer(temp);
        gameplay.setSelectedField(null);
        gameplay.setOwnsSelectedCard(null);
        gameplay.setHasPlacedMonster(false);
        GameplayView.getInstance().setFirstOfGame(false);
        GUI.GameplayView.getInstance().hideOpponentHands(getGameplay());
    }

    public void temporarySwitchTurn() {
        System.out.println("now it will be " + gameplay.getOpponentPlayer().getUser().getUsername() + "'s turn");
        Player temp = gameplay.getOpponentPlayer();
        gameplay.setOpponentPlayer(gameplay.getCurrentPlayer());
        gameplay.setCurrentPlayer(temp);
        gameplay.setSelectedField(null);
        gameplay.setOwnsSelectedCard(null);
        GUI.GameplayView.getInstance().hideOpponentHands(getGameplay());
    }

    public String endWholeMatch(Player winner, Player loser) {
        int multiplier = gameplay.getRounds();
        winner.getUser().increaseScore(1000 * multiplier);
        winner.getUser().increaseBalance((1000 + winner.getMaxLP()) * multiplier);
        loser.getUser().increaseBalance(100 * multiplier);
        setGameplay(null);
        ProgramController.getInstance().setCurrentMenu(Menu.MAIN_MENU);
        return winner.getUser().getUsername() + " won the whole match with score: " + winner.getUser().getScore();
    }

    public String endARound(Player winner, Player loser) {
        winner.setMaxLP(winner.getLifePoints());
        if (winner.equals(gameplay.getPlayerOne())) gameplay.playerOneWins++;
        else gameplay.playerTwoWins++;
        switch (gameplay.getRounds()) {
            case 1:
                return endWholeMatch(winner, loser);
            case 3:
                if (gameplay.playerOneWins == 2 || gameplay.playerTwoWins == 2)
                    return endWholeMatch(winner, loser);
            default:
                gameplay.setCurrentPlayer(winner);
                gameplay.setOpponentPlayer(loser);
                gameplay.setSelectedField(null);
                gameplay.setAttacker(null);
                gameplay.setBeingAttacked(null);
                gameplay.setCurrentRound(gameplay.getCurrentRound() + 1);
                gameplay.setOwnsSelectedCard(null);
                gameplay.setHasPlacedMonster(false);
                setStartingPlayer();
                gameplay.getCurrentPlayer().setField(new Field());
                gameplay.getOpponentPlayer().setField(new Field());
                gameplay.getOpponentPlayer().getPlayingHand().clear();
                gameplay.getCurrentPlayer().getPlayingHand().clear();
                dealCardsAtBeginning();
                GameplayView.getInstance().setFirstOfGame(true);
                gameplay.setCurrentPhase(Phase.DRAW_PHASE);
                return winner.getUser().getUsername() + " won the game and the score is: " + winner.getUser().getScore();
        }
    }

    public String surrender() {
        return endWholeMatch(gameplay.getOpponentPlayer(), gameplay.getCurrentPlayer());
    }

    public void selectCard(String idToCheck, String field, boolean isFromOpponent) throws Exception {
        FieldArea fieldArea;
        int id;
        switch (field) {
            case "-m":
            case "--monster":
                if (isLocationNumberInvalid(idToCheck)) throw new InvalidCardSelectionException();
                id = Integer.parseInt(idToCheck);
                if (isFromOpponent) fieldArea = gameplay.getOpponentPlayer().getField().getMonstersFieldById(id);
                else fieldArea = gameplay.getCurrentPlayer().getField().getMonstersFieldById(id);
                if (fieldArea.getCard() == null) throw new NoCardFoundException();
                gameplay.setSelectedField(fieldArea);
                break;
            case "-s":
            case "--spell":
                if (isLocationNumberInvalid(idToCheck)) throw new InvalidCardSelectionException();
                id = Integer.parseInt(idToCheck);
                if (isFromOpponent) fieldArea = gameplay.getOpponentPlayer().getField().getSpellAndTrapFieldById(id);
                else fieldArea = gameplay.getCurrentPlayer().getField().getSpellAndTrapFieldById(id);
                if (fieldArea.getCard() == null) throw new NoCardFoundException();
                gameplay.setSelectedField(fieldArea);
                break;
            case "-h":
            case "--hand":
                if (isHandLocationInvalid(idToCheck)) throw new InvalidCardSelectionException();
                id = Integer.parseInt(idToCheck);
                fieldArea = gameplay.getCurrentPlayer().getPlayingHand().get(id - 1);
                if (fieldArea == null) throw new NoCardFoundException();
                gameplay.setSelectedField(fieldArea);
                break;
            case "-f":
            case "--field":
                if (isFromOpponent) fieldArea = gameplay.getOpponentPlayer().getField().getFieldZone();
                else fieldArea = gameplay.getCurrentPlayer().getField().getFieldZone();
                if (fieldArea.getCard() == null) throw new NoCardFoundException();
                gameplay.setSelectedField(fieldArea);
                break;
        }
        gameplay.setOwnsSelectedCard(!isFromOpponent);
    }

    public boolean isHandLocationInvalid(String idToCheck) {
        if (idToCheck.matches("^\\d+$")) {
            int id = Integer.parseInt(idToCheck);
            return (gameplay.getCurrentPlayer().getPlayingHand().size() < id);
        }
        return true;
    }

    public void deselectCard() throws NoCardIsSelectedException {
        if (gameplay == null) return;
        if (gameplay.getSelectedField() == null) throw new NoCardIsSelectedException();
        gameplay.setSelectedField(null);
        gameplay.setOwnsSelectedCard(null);
        Rectangle cardView = (Rectangle) GUI.GameplayView.cardDisplay.getChildren().get(0);
        Label description = (Label) GUI.GameplayView.cardDisplay.getChildren().get(1);
        cardView.setFill(new ImagePattern(Card.UNKNOWN_CARD));
        description.setText("");
    }

    public void showCard() throws NoCardIsSelectedException {
        if (gameplay.getSelectedField() == null) throw new NoCardIsSelectedException();
        if (!gameplay.ownsSelectedCard() && !gameplay.getSelectedField().isVisible()) CardView.invisibleCard();
        else CardView.showCardInGame(gameplay.getSelectedField());
    }

    public void activateEffect(SpellAndTrapActivationType type) throws NoCardIsSelectedException, InvalidActivateException, WrongPhaseForSpellException, AlreadyActivatedException, SpecialSummonNotPossibleException, PreparationNotReadyException, ActionNotPossibleException, MonsterZoneFullException, AttackNotPossibleException, RitualSummonNotPossibleException, CommandCancellationException, SpellZoneFullException {
        FieldArea fieldArea = gameplay.getSelectedField();
        if (fieldArea == null) throw new NoCardIsSelectedException();
        if (fieldArea.getCard() instanceof Monster) throw new InvalidActivateException();
        if (type.equals(SpellAndTrapActivationType.NORMAL) && !gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_ONE) && !gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_TW0))
            throw new WrongPhaseForSpellException();
        if (fieldArea.visibility()) throw new AlreadyActivatedException();
        if (fieldArea.getCard() instanceof Trap) activateTrapEffect(type);
        else activateSpellEffect(fieldArea);
    }

    public void activateSpellEffect()  throws ActionNotPossibleException, NoCardIsSelectedException, AttackNotPossibleException, SpecialSummonNotPossibleException, MonsterZoneFullException, PreparationNotReadyException {
        FieldArea fieldArea = gameplay.getSelectedField();
        if (((Spell) fieldArea.getCard()).getIcon().equals(Icon.RITUAL)) {
            effectSpellAndTraps.add(fieldArea);
            gameState = GameState.RITUAL_SPELL_ACTIVATED_MODE;
        } else if (fieldArea.canBePutOnBoard()) {
            if (((Spell) fieldArea.getCard()).getIcon().equals(Icon.FIELD)) {
                gameplay.getCurrentPlayer().getField().getFieldZone().putCard(fieldArea.getCard(), true);
                gameplay.getCurrentPlayer().getField().getHandFieldArea().getChildren().remove(fieldArea);
                gameplay.getCurrentPlayer().getPlayingHand().remove(fieldArea);
            }
            if (((Spell) fieldArea.getCard()).getIcon().equals(Icon.EQUIP)) {
                if (getNumberOfPlayerMonsters(gameplay.getCurrentPlayer()) == 0)
                    throw new PreparationNotReadyException();
                gameState = GameState.EQUIP_ACTIVATION_MODE;
            } else if (((Spell) fieldArea.getCard()).spellEffect != null) {
                ((Spell) fieldArea.getCard()).spellEffect.execute();
                destroyHandFieldCard(gameplay.getCurrentPlayer(), (HandFieldArea) fieldArea);
            }
        } else if (fieldArea instanceof SpellAndTrapFieldArea) {
            if (((Spell) fieldArea.getCard()).getIcon().equals(Icon.FIELD)) {
                ((SpellAndTrapFieldArea) fieldArea).activateChangePosition();
            }
            if (((Spell) fieldArea.getCard()).getIcon().equals(Icon.EQUIP)) {
                if (getNumberOfPlayerMonsters(gameplay.getCurrentPlayer()) == 0)
                    throw new PreparationNotReadyException();
                gameState = GameState.EQUIP_ACTIVATION_MODE;
            } else if (((Spell) fieldArea.getCard()).spellEffect != null) {
                ((Spell) fieldArea.getCard()).spellEffect.execute();
                destroySpellAndTrapCard(gameplay.getCurrentPlayer(), (SpellAndTrapFieldArea) fieldArea);
            }
        }
    }
    private void activateSpellEffect(FieldArea fieldArea) throws RitualSummonNotPossibleException, ActionNotPossibleException, NoCardIsSelectedException, CommandCancellationException, SpellZoneFullException, AttackNotPossibleException, SpecialSummonNotPossibleException, MonsterZoneFullException, PreparationNotReadyException {
        boolean trapExists;
        if (((Spell) fieldArea.getCard()).getIcon().equals(Icon.RITUAL)) {
            if (!hasRitualMonsterInHand()) throw new RitualSummonNotPossibleException();
            if (!sumMeetsRitualMonsterLevel()) throw new RitualSummonNotPossibleException();
            trapExists = onSpellActivationTraps(fieldArea);
            if (trapExists) return;
            effectSpellAndTraps.add(fieldArea);
            gameState = GameState.RITUAL_SPELL_ACTIVATED_MODE;
        } else if (fieldArea.canBePutOnBoard()) {
            SpellAndTrapFieldArea spell = gameplay.getCurrentPlayer().getField().getFreeSpellFieldArea();
            if (spell == null) throw new SpellZoneFullException();
            trapExists = onSpellActivationTraps(fieldArea);
            if (trapExists) return;
            if (((Spell) fieldArea.getCard()).getIcon().equals(Icon.FIELD)) {
                gameplay.getCurrentPlayer().getField().getFieldZone().putCard(fieldArea.getCard(), true);
                gameplay.getCurrentPlayer().getField().getHandFieldArea().getChildren().remove(fieldArea);
                gameplay.getCurrentPlayer().getPlayingHand().remove(fieldArea);
            }
            if (((Spell) fieldArea.getCard()).getIcon().equals(Icon.EQUIP)) {
                if (getNumberOfPlayerMonsters(gameplay.getCurrentPlayer()) == 0)
                    throw new PreparationNotReadyException();
                gameState = GameState.EQUIP_ACTIVATION_MODE;
            } else if (((Spell) fieldArea.getCard()).spellEffect != null) {
                ((Spell) fieldArea.getCard()).spellEffect.execute();
                destroyHandFieldCard(gameplay.getCurrentPlayer(), (HandFieldArea) fieldArea);
            }
        } else if (fieldArea instanceof SpellAndTrapFieldArea) {
            if (!((SpellAndTrapFieldArea) fieldArea).isCanBeActivated())
                throw new PreparationNotReadyException();
            trapExists = onSpellActivationTraps(fieldArea);
            if (trapExists) return;
            if (((Spell) fieldArea.getCard()).getIcon().equals(Icon.FIELD)) {
                ((SpellAndTrapFieldArea) fieldArea).activateChangePosition();
            }
            if (((Spell) fieldArea.getCard()).getIcon().equals(Icon.EQUIP)) {
                if (getNumberOfPlayerMonsters(gameplay.getCurrentPlayer()) == 0)
                    throw new PreparationNotReadyException();
                gameState = GameState.EQUIP_ACTIVATION_MODE;
            } else if (((Spell) fieldArea.getCard()).spellEffect != null) {
                ((Spell) fieldArea.getCard()).spellEffect.execute();
                destroySpellAndTrapCard(gameplay.getCurrentPlayer(), (SpellAndTrapFieldArea) fieldArea);
            }
        }
    }

    public void activateEquip(MonsterFieldArea toEquipMonster) {
        FieldArea equipSpell = gameplay.getSelectedField();
        equipSpell.getCard().equipEffect.activate(toEquipMonster);
        if (equipSpell instanceof HandFieldArea) {
            SpellAndTrapFieldArea freeSpellFieldArea = gameplay.getCurrentPlayer().getField().getFreeSpellFieldArea();
            freeSpellFieldArea.putCard(equipSpell.getCard(), true);
            gameplay.getCurrentPlayer().getPlayingHand().remove(equipSpell);
            gameplay.getCurrentPlayer().getField().getChildren().remove(equipSpell);
            gameplay.getCurrentPlayer().getEquippedMonsters().put(freeSpellFieldArea, toEquipMonster);
        }
        if (equipSpell instanceof SpellAndTrapFieldArea) {
            ((SpellAndTrapFieldArea) equipSpell).activateChangePosition();
            ((SpellAndTrapFieldArea) equipSpell).setCanBeActivated(false);
            gameplay.getCurrentPlayer().getEquippedMonsters().put((SpellAndTrapFieldArea) equipSpell, toEquipMonster);
        }
    }

    private void activateTrapEffect(SpellAndTrapActivationType type) throws
            PreparationNotReadyException, AttackNotPossibleException, ActionNotPossibleException, SpecialSummonNotPossibleException, MonsterZoneFullException, NoCardIsSelectedException {
        FieldArea fieldArea = gameplay.getSelectedField();
        if (fieldArea instanceof HandFieldArea) throw new PreparationNotReadyException();
        if (!((SpellAndTrapFieldArea) fieldArea).isCanBeActivated()) throw new PreparationNotReadyException();
        Card temp = fieldArea.getCard();
        ((SpellAndTrapFieldArea) fieldArea).setCanBeActivated(false);
        destroySpellAndTrapCard(gameplay.getCurrentPlayer(), (SpellAndTrapFieldArea) fieldArea);
        switch (type) {
            case ON_ATTACKED:
                temp.onBeingAttacked.execute();
                break;
            case ON_SUMMON:
                temp.onSummon.execute();
                break;
            case ON_SPELL:
                temp.onSpellActivation.execute();
                break;
            case ON_STANDBY:
                temp.inStandbyPhase.execute();
        }
    }

    public void set() throws Exception {
        FieldArea fieldArea = gameplay.getSelectedField();
        if (fieldArea == null) throw new NoCardIsSelectedException();
        if (!fieldArea.canBePutOnBoard() || !gameplay.ownsSelectedCard())
            throw new InvalidSetException();
        if (!gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_ONE) && !gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_TW0))
            throw new WrongPhaseException();
        if (fieldArea.getCard() instanceof Monster) setInMonsterCondition(fieldArea);
        else if (fieldArea.getCard() instanceof Spell || fieldArea.getCard() instanceof Trap)
            setInSpellCondition(fieldArea);
    }

    private void setInSpellCondition(FieldArea fieldArea) throws SpellZoneFullException, NoCardIsSelectedException {
        SpellAndTrapFieldArea potentialSpellField;
        if (((SpellAndTrap) fieldArea.getCard()).getIcon().equals(Icon.FIELD))
            potentialSpellField = gameplay.getCurrentPlayer().getField().getFieldZone();
        else potentialSpellField = gameplay.getCurrentPlayer().getField().getFreeSpellFieldArea();
        if (potentialSpellField == null) throw new SpellZoneFullException();
        if (fieldArea.getCard() instanceof Spell) potentialSpellField.setCanBeActivated(true);
        setSpellCard(potentialSpellField, (HandFieldArea) fieldArea);
        deselectCard();
    }

    private void setInMonsterCondition(FieldArea fieldArea) throws Exception {
        MonsterFieldArea monster = gameplay.getCurrentPlayer().getField().getFreeMonsterFieldArea();
        if (monster == null) throw new MonsterZoneFullException();
        if (gameplay.hasPlacedMonster()) throw new AlreadySummonedException();
        if (((Monster) fieldArea.getCard()).getCardType().equals(CardType.RITUAL)) {
            checkForRitual(false);
            return;
        } else if (((Monster) fieldArea.getCard()).getLevel() > 4 && toTributeCards.isEmpty()) {
            if (getNumberOfPlayerMonsters(gameplay.getCurrentPlayer()) < ((Monster) fieldArea.getCard()).getNumberOfTributes())
                throw new NotEnoughCardsException();
            gameState = GameState.TRIBUTE_SET_MODE;
            tributeCount = ((Monster) fieldArea.getCard()).getNumberOfTributes();
            return;
        }
        toTributeCards.clear();
        setMonsterCard(monster, (HandFieldArea) fieldArea);
        deselectCard();
    }

    public void summon() throws Exception {
        FieldArea fieldArea = gameplay.getSelectedField();
        if (fieldArea == null) throw new NoCardIsSelectedException();
        if (fieldArea instanceof MonsterFieldArea) throw new InvalidSummonException();
        if (!(fieldArea.getCard() instanceof Monster) || !gameplay.ownsSelectedCard())
            throw new InvalidSummonException();
        if (!gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_ONE) && !gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_TW0))
            throw new WrongPhaseException();
        MonsterFieldArea monsterFieldArea = gameplay.getCurrentPlayer().getField().getFreeMonsterFieldArea();
        if (monsterFieldArea == null) throw new MonsterZoneFullException();
        if (gameplay.hasPlacedMonster()) throw new AlreadySummonedException();
        if (((Monster) fieldArea.getCard()).getCardType().equals(CardType.RITUAL)) {
            checkForRitual(true);
            return;
        } else if (fieldArea.getCard().uniqueSummon != null) fieldArea.getCard().uniqueSummon.summon();
        else if (((Monster) fieldArea.getCard()).getLevel() > 4 && toTributeCards.isEmpty()) {
            if (getNumberOfPlayerMonsters(gameplay.getCurrentPlayer()) < ((Monster) fieldArea.getCard()).getNumberOfTributes())
                throw new NotEnoughCardsException();
            gameState = GameState.TRIBUTE_SUMMON_MODE;
            tributeCount = ((Monster) fieldArea.getCard()).getNumberOfTributes();
            return;
        }
        toTributeCards.clear();
        normalSummon((HandFieldArea) fieldArea, monsterFieldArea);
        gameplay.setRecentlySummonedMonster(monsterFieldArea);
        onSummonTraps();
        if (gameplay.getSelectedField() != null) deselectCard();
    }

    private void checkForRitual(boolean isSummon) throws Exception {
        FieldArea ritualSpell = null;
        for (FieldArea spellAndTrap :
                effectSpellAndTraps) {
            ritualSpell = spellAndTrap;
            if (spellAndTrap.getCard() instanceof Spell)
                if (!((Spell) spellAndTrap.getCard()).getIcon().equals(Icon.RITUAL))
                    throw new InvalidSummonException();
        }
        if (ritualSpell == null) throw new InvalidSummonException();
        if (isSummon) gameState = GameState.RITUAL_SUMMON_MODE;
        else gameState = GameState.RITUAL_SET_MODE;
        GUI.GameplayView.ritualTribute(gameplay.getCurrentPlayer(), ritualSpell, isSummon);
    }

    public void tributeCards() {
        for (MonsterFieldArea monsterFieldArea : toTributeCards) {
            destroyMonsterCard(gameplay.getCurrentPlayer(), monsterFieldArea);
        }
        tributeCount = 0;
    }

    public void ritualTribute(String[] ids, FieldArea ritualSpell, boolean isSummon) {
        removeTributesFromHand(ids);
        DeckMenuController.getInstance().shuffleDeck(gameplay.getCurrentPlayer().getPlayingDeck());
        effectSpellAndTraps.remove(ritualSpell);
        if (ritualSpell instanceof HandFieldArea) {
            moveCardToGraveyard(gameplay.getCurrentPlayer(), ritualSpell.getCard());
            gameplay.getCurrentPlayer().getPlayingHand().remove(ritualSpell);
            gameplay.getCurrentPlayer().getField().getHandFieldArea().getChildren().remove(ritualSpell);
        }
        if (ritualSpell instanceof SpellAndTrapFieldArea)
            destroySpellAndTrapCard(gameplay.getCurrentPlayer(), (SpellAndTrapFieldArea) ritualSpell);
        DeckMenuController.getInstance().shuffleDeck(gameplay.getCurrentPlayer().getPlayingDeck());
        if (isSummon)
            normalSummon((HandFieldArea) gameplay.getSelectedField(), gameplay.getCurrentPlayer().getField().getFreeMonsterFieldArea());
        else
            setMonsterCard(gameplay.getCurrentPlayer().getField().getFreeMonsterFieldArea(), (HandFieldArea) gameplay.getSelectedField());
        gameState = GameState.NORMAL_MODE;
    }

    private void removeTributesFromHand(String[] ids) {
        ArrayList<Card> mainCards = gameplay.getCurrentPlayer().getPlayingDeck().getMainCards();
        ArrayList<Card> cardsToRemove = new ArrayList<>();
        for (String idString :
                ids) {
            int id = Integer.parseInt(idString);
            GameplayController.getInstance().moveCardToGraveyard(gameplay.getCurrentPlayer(), mainCards.get(id - 1));
            cardsToRemove.add(mainCards.get(id - 1));
            System.out.println("Card to remove: " + mainCards.get(id - 1).getName());
        }
        for (Card card : cardsToRemove) {
            mainCards.remove(card);
        }
    }

    public void flipSummon() throws Exception {
        FieldArea fieldArea = gameplay.getSelectedField();
        if (fieldArea == null) throw new NoCardIsSelectedException();
        if (!(fieldArea instanceof MonsterFieldArea) || !gameplay.ownsSelectedCard())
            throw new InvalidChangePositionException();
        if (!gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_ONE) && !gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_TW0))
            throw new WrongPhaseException();
        if (((MonsterFieldArea) fieldArea).hasSwitchedMode()) throw new AlreadySetPositionException();
        if (((MonsterFieldArea) fieldArea).isAttack() || fieldArea.visibility()) throw new InvalidFlipSummonException();
        if (fieldArea.getCard().onFlipSummon != null) fieldArea.getCard().onFlipSummon.execute();
        ((MonsterFieldArea) fieldArea).changePosition();
        deselectCard();
    }

    public void changePosition(boolean isAttack) throws Exception {
        FieldArea fieldArea = gameplay.getSelectedField();
        if (fieldArea == null) throw new NoCardIsSelectedException();
        if (!(fieldArea instanceof MonsterFieldArea) || !gameplay.ownsSelectedCard())
            throw new InvalidChangePositionException();
        if (!gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_ONE) && !gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_TW0))
            throw new WrongPhaseException();
        if (((MonsterFieldArea) fieldArea).isAttack() == isAttack) throw new AlreadyInPositionException();
        if (((MonsterFieldArea) fieldArea).hasSwitchedMode()) throw new AlreadySetPositionException();
        ((MonsterFieldArea) fieldArea).changePosition();
        deselectCard();
    }

    public StringBuilder attack(String number) throws Exception {
        FieldArea attacker = gameplay.getSelectedField();
        MonsterFieldArea attackTarget;
        Player opponent = gameplay.getOpponentPlayer();
        if (attacker == null) throw new NoCardIsSelectedException();
        if (!(attacker instanceof MonsterFieldArea) || !gameplay.ownsSelectedCard())
            throw new AttackNotPossibleException();
        if (!gameplay.getCurrentPhase().equals(Phase.BATTLE_PHASE))
            throw new WrongPhaseException();
        if (((MonsterFieldArea) attacker).hasAttacked()) throw new AlreadyAttackedException();
        if (isLocationNumberInvalid(number)) throw new InvalidCardSelectionException();
        int id = Integer.parseInt(number);
        if ((attackTarget = opponent.getField().getMonstersFieldById(id)).getCard() == null)
            throw new NoCardFoundException();
        gameplay.setAttacker(attacker);
        gameplay.setBeingAttacked(attackTarget);
        StringBuilder temp;
        boolean trapExists = onAttackTraps();
        possibleChainChecked = true;
        if (trapExists) return null;
        if (attackTarget.getCard() != null && attackTarget.getCard().onBeingAttacked != null)
            attackTarget.getCard().onBeingAttacked.execute();
        temp = calculateDamage((MonsterFieldArea) attacker, attackTarget);
        ((MonsterFieldArea) attacker).setHasAttacked(true);
        if (gameplay.getSelectedField() != null) deselectCard();
        gameplay.setAttacker(null);
        gameplay.setBeingAttacked(null);
        return temp;
    }

    public StringBuilder attack() throws Exception {
        FieldArea attacker = gameplay.getAttacker();
        FieldArea attackTarget = gameplay.getBeingAttacked();
        if (attackTarget.getCard() != null && attackTarget.getCard().onBeingAttacked != null)
            attackTarget.getCard().onBeingAttacked.execute();
        StringBuilder temp = calculateDamage((MonsterFieldArea) attacker, (MonsterFieldArea) attackTarget);
        ((MonsterFieldArea) attacker).setHasAttacked(true);
        if (gameplay.getSelectedField() != null) deselectCard();
        gameplay.setAttacker(null);
        gameplay.setBeingAttacked(null);
        return temp;
    }
    public void resetChainSituation() {
        possibleChainChecked = false;
    }

    public FieldArea toActivateSpell;
    private boolean onSpellActivationTraps(FieldArea toActivateSpell) {
        boolean spellTrapExists = false;
        for (SpellAndTrapFieldArea trap :
                gameplay.getOpponentPlayer().getField().getSpellAndTrapField()) {
            if (trap.getCard() != null && trap.getCard().onSpellActivation != null) {
                spellTrapExists = true;
                trap.setCanBeActivated(true);
            }
        }
        if (spellTrapExists) {
            disableOpponentsSpellsForChain();
            temporarySwitchTurn();
            setChainType(SpellAndTrapActivationType.ON_SPELL);
            GUI.GameplayView.chainPrompt();
            this.toActivateSpell = toActivateSpell;
            return true;
        }
        return false;
    }

    private void onSummonTraps() {
        boolean summonTrapExists = false;
        for (SpellAndTrapFieldArea trap :
                gameplay.getOpponentPlayer().getField().getSpellAndTrapField()) {
            if (trap.getCard() != null)
                if (trap.getCard().onSummon != null) {
                    summonTrapExists = true;
                    trap.setCanBeActivated(true);
                }
        }
        if (summonTrapExists) {
            disableOpponentsSpellsForChain();
            temporarySwitchTurn();
            GameplayController.setChainType(SpellAndTrapActivationType.ON_SUMMON);
            GUI.GameplayView.chainPrompt();
            }
    }

    public void onStandbyTraps() {
        if (gameplay.getCurrentPhase() != Phase.STANDBY_PHASE) return;
        boolean opponentStandbyTrapExists = false;
        for (SpellAndTrapFieldArea trap :
                gameplay.getOpponentPlayer().getField().getSpellAndTrapField()) {
            if (trap.getCard() != null)
                if (trap.getCard().inStandbyPhase != null) {
                    opponentStandbyTrapExists = true;
                    trap.setCanBeActivated(true);
                }
        }
        if (opponentStandbyTrapExists) {
            disableOpponentsSpellsForChain();
            temporarySwitchTurn();
            setChainType(SpellAndTrapActivationType.ON_STANDBY);
            GUI.GameplayView.chainPrompt();
        }
    }

    private boolean onAttackTraps() {
        boolean attackTrapExists = false;
        for (SpellAndTrapFieldArea trap :
                gameplay.getOpponentPlayer().getField().getSpellAndTrapField()) {
            if (trap.getCard() != null && trap.getCard().onBeingAttacked != null) {
                attackTrapExists = true;
                trap.setCanBeActivated(true);
            }
        }
        if (attackTrapExists) {
            disableOpponentsSpellsForChain();
            temporarySwitchTurn();
            GameplayController.setChainType(SpellAndTrapActivationType.ON_ATTACKED);
            GUI.GameplayView.chainPrompt();
            return true;
        }
        return false;
    }

    private void disableOpponentsSpellsForChain() {
        for (SpellAndTrapFieldArea spell :
                gameplay.getOpponentPlayer().getField().getSpellAndTrapField()) {
            if (spell.getCard() != null && spell.getCard() instanceof Spell) spell.setCanBeActivated(false);
        }
    }

    public void resetOpponentTrapsAndSpells(SpellAndTrapActivationType type) {
        switch (type) {
            case ON_ATTACKED:
                for (SpellAndTrapFieldArea trap :
                        gameplay.getOpponentPlayer().getField().getSpellAndTrapField()) {
                    if (trap.getCard() != null)
                        if (trap.getCard().onBeingAttacked != null) {
                            trap.setCanBeActivated(false);
                        }
                }
                break;
            case ON_SUMMON:
                for (SpellAndTrapFieldArea trap :
                        gameplay.getOpponentPlayer().getField().getSpellAndTrapField()) {
                    if (trap.getCard() != null)
                        if (trap.getCard().onSummon != null) {
                            trap.setCanBeActivated(false);
                        }
                }
                break;
            case ON_SPELL:
                for (SpellAndTrapFieldArea trap :
                        gameplay.getOpponentPlayer().getField().getSpellAndTrapField()) {
                    if (trap.getCard() != null)
                        if (trap.getCard().onSpellActivation != null) {
                            trap.setCanBeActivated(false);
                        }
                }
                break;
            case NORMAL:
                break;
            case ON_STANDBY:
                for (SpellAndTrapFieldArea trap :
                        gameplay.getOpponentPlayer().getField().getSpellAndTrapField()) {
                    if (trap.getCard() != null)
                        if (trap.getCard().inStandbyPhase != null) {
                            trap.setCanBeActivated(false);
                        }
                }
                break;
        }
        for (SpellAndTrapFieldArea spell :
                gameplay.getOpponentPlayer().getField().getSpellAndTrapField()) {
            if (spell.getCard() != null && spell.getCard() instanceof Spell) spell.setCanBeActivated(true);
        }
    }

    public String directAttack() throws Exception {
        FieldArea fieldArea = gameplay.getSelectedField();
        if (fieldArea == null) throw new NoCardIsSelectedException();
        if (!(fieldArea instanceof MonsterFieldArea)) throw new AttackNotPossibleException();
        if (!gameplay.getCurrentPhase().equals(Phase.BATTLE_PHASE)) throw new WrongPhaseException();
        if (((MonsterFieldArea) fieldArea).hasAttacked()) throw new AlreadyAttackedException();
        if (!isOpponentFieldEmpty()) throw new DirectAttackNotPossibleException();
        String temp = calculateDirectDamage((MonsterFieldArea) fieldArea);
        ((MonsterFieldArea) fieldArea).setHasAttacked(true);
        deselectCard();
        return temp;
    }

    private StringBuilder calculateDamage(MonsterFieldArea attackingMonster, MonsterFieldArea beingAttackedMonster) throws Exception {
        StringBuilder message = new StringBuilder();
        boolean initialVisibility = beingAttackedMonster.visibility();
        if (beingAttackedMonster.isAttack()) {
            int damage = calculateAttackVsAttackSituation(attackingMonster, beingAttackedMonster);
            if (damage > 0)
                message.append("your opponent’s monster is destroyed and your opponent receives").append(damage).append(" battle damage");
            if (damage < 0)
                message.append("Your monster card is destroyed and you received ").append(-damage).append(" battle damage");
            if (damage == 0)
                message.append("both you and your opponent monster cards are destroyed and no one receives damage");
        } else {
            if (!beingAttackedMonster.visibility()) {
                message.append("opponent’s monster card was ").append(beingAttackedMonster.getCard().getName()).append(" and ");
                makeCardVisible(beingAttackedMonster);
            }
            int damage = calculateAttackVsDefenseSituation(beingAttackedMonster, attackingMonster);
            if (damage > 0) message.append("the defense position monster is destroyed");
            if (damage < 0)
                message.append("no card is destroyed and you received ").append(-damage).append(" battle damage");
            if (damage == 0) message.append("no card is destroyed");
        }
        if (beingAttackedMonster.getCard() != null && beingAttackedMonster.getCard().afterDamageCalculation != null)
            beingAttackedMonster.getCard().afterDamageCalculation.execute(initialVisibility);
        return message;
    }

    private int calculateAttackVsDefenseSituation(MonsterFieldArea defense, MonsterFieldArea attack) {
        int attackMonsterPoint = attack.getAttackPoint();
        int defenseMonsterPoint = defense.getDefensePoint();
        int damage = attackMonsterPoint - defenseMonsterPoint;
        if (damage > 0) destroyMonsterCard(gameplay.getOpponentPlayer(), defense);
        if (damage < 0) {
            int newLP = gameplay.getCurrentPlayer().getLifePoints() + damage;
            gameplay.getCurrentPlayer().setLifePoints(newLP);
        }
        return damage;
    }

    private int calculateAttackVsAttackSituation(MonsterFieldArea attack, MonsterFieldArea defense) {
        int attackMonsterPoint = attack.getAttackPoint();
        int defenseMonsterPoint = defense.getAttackPoint();
        int damage = attackMonsterPoint - defenseMonsterPoint;
        Effect defenseCalculationEffect = defense.getCard().onDamageCalculation;
        Effect attackCalculationEffect = attack.getCard().onDamageCalculation;
        try {
            if (damage > 0) {
                destroyMonsterCard(gameplay.getOpponentPlayer(), defense);
                if (defenseCalculationEffect != null) defenseCalculationEffect.execute();
                int newLP = gameplay.getOpponentPlayer().getLifePoints() - damage;
                gameplay.getOpponentPlayer().setLifePoints(newLP);
            } else if (damage < 0) {
                destroyMonsterCard(gameplay.getCurrentPlayer(), attack);
                if (attackCalculationEffect != null) attackCalculationEffect.execute();
                int newLP = gameplay.getCurrentPlayer().getLifePoints() + damage;
                gameplay.getCurrentPlayer().setLifePoints(newLP);
            } else {
                destroyMonsterCard(gameplay.getOpponentPlayer(), defense);
                destroyMonsterCard(gameplay.getCurrentPlayer(), attack);
            }
        } catch (Exception ignored) {
        }
        return damage;
    }

    private String calculateDirectDamage(MonsterFieldArea monster) {
        int damage = monster.getAttackPoint();
        int newLp = gameplay.getOpponentPlayer().getLifePoints() - damage;
        gameplay.getOpponentPlayer().setLifePoints(newLp);
        return "you opponent receives " + damage + " battle damage";
    }

    public void destroyMonsterCard(Player player, MonsterFieldArea monster) {
        try {
            for (SpellAndTrapFieldArea equipSpell :
                    gameplay.getCurrentPlayer().getEquippedMonsters().keySet()) {
                if (gameplay.getCurrentPlayer().getEquippedMonsters().get(equipSpell).equals(monster)) {
                    gameplay.getCurrentPlayer().getEquippedMonsters().remove(equipSpell);
                    destroySpellAndTrapCard(gameplay.getCurrentPlayer(), equipSpell);
                }
            }
            for (SpellAndTrapFieldArea equipSpell :
                    gameplay.getOpponentPlayer().getEquippedMonsters().keySet()) {
                if (gameplay.getOpponentPlayer().getEquippedMonsters().get(equipSpell).equals(monster)) {
                    gameplay.getOpponentPlayer().getEquippedMonsters().remove(equipSpell);
                    destroySpellAndTrapCard(gameplay.getOpponentPlayer(), equipSpell);
                }
            }
            if ((monster.getCard()).onDestruction != null)
                (monster.getCard()).onDestruction.execute(player);
            moveCardToGraveyard(player, monster.getCard());
            monster.setVisibility(false);
            monster.setHasAttacked(false);
            monster.setHasSwitchedMode(false);
            monster.setAttackPoint(0);
            monster.setDefensePoint(0);
            monster.putCard(null, false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void destroySpellAndTrapCard(Player player, SpellAndTrapFieldArea area) {
        try {
            if (player.getEquippedMonsters().containsKey(area)) {
                area.getCard().equipEffect.deactivate(player.getEquippedMonsters().get(area));
                player.getEquippedMonsters().remove(area);
            }
            if ((area.getCard()).onDestruction != null)
                (area.getCard()).onDestruction.execute(player);
            moveCardToGraveyard(player, area.getCard());
            area.setVisibility(false);
            area.putCard(null, false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void destroyHandFieldCard(Player player, HandFieldArea area) {
        try {
            if ((area.getCard()).onDestruction != null)
                (area.getCard()).onDestruction.execute(player);
            moveCardToGraveyard(player, area.getCard());
            gameplay.getCurrentPlayer().getField().getHandFieldArea().getChildren().remove(area);
            gameplay.getCurrentPlayer().getPlayingHand().remove(area);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void moveCardToGraveyard(Player player, Card card) {
        player.getField().getGraveyard().add(card);
        player.getField().getGraveyardField().getCardView().setFill(card.getFill());
    }

    private Card moveTopCardFromDeckToHand(Player player) {
        Card card = player.getPlayingDeck().getMainCards().get(0);
        HandFieldArea handFieldArea = new HandFieldArea(card);
        player.getPlayingHand().add(handFieldArea);
        player.getPlayingDeck().getMainCards().remove(card);
        player.getField().getHandFieldArea().getChildren().add(handFieldArea);
        return card;
    }

    public void discardSelectedCard() throws InvalidCardSelectionException {
        if (gameplay.getSelectedField() == null) return;
        if (!gameplay.ownsSelectedCard()) throw new InvalidCardSelectionException();
        moveCardToGraveyard(gameplay.getCurrentPlayer(), gameplay.getSelectedField().getCard());
        gameplay.getCurrentPlayer().getPlayingHand().remove(gameplay.getSelectedField());
        gameplay.getCurrentPlayer().getField().getHandFieldArea().getChildren().remove(gameplay.getSelectedField());
    }

    private void setMonsterCard(MonsterFieldArea monsterFieldArea, HandFieldArea fieldArea) {
        monsterFieldArea.putCard(fieldArea.getCard(), false);
        gameplay.getCurrentPlayer().getPlayingHand().remove(fieldArea);
        gameplay.getCurrentPlayer().getField().getHandFieldArea().getChildren().remove(fieldArea);
        gameplay.setHasPlacedMonster(true);
    }

    private void setSpellCard(SpellAndTrapFieldArea spellAndTrapFieldArea, HandFieldArea handFieldArea) {
        if (spellAndTrapFieldArea instanceof FieldZoneArea && spellAndTrapFieldArea.getCard() != null)
            destroySpellAndTrapCard(gameplay.getCurrentPlayer(), spellAndTrapFieldArea);
        spellAndTrapFieldArea.putCard(handFieldArea.getCard(), false);
        gameplay.getCurrentPlayer().getPlayingHand().remove(handFieldArea);
        gameplay.getCurrentPlayer().getField().getHandFieldArea().getChildren().remove(handFieldArea);

    }


    private void normalSummon(HandFieldArea fieldArea, MonsterFieldArea monsterFieldArea) {
        monsterFieldArea.putCard(fieldArea.getCard(), true);
        gameplay.getCurrentPlayer().getPlayingHand().remove(fieldArea);
        gameplay.getCurrentPlayer().getField().getHandFieldArea().getChildren().remove(fieldArea);
        gameplay.setHasPlacedMonster(true);
    }

    public void specialSummon(Card card) {
        MonsterFieldArea toPutOn = GameplayController.getInstance().getGameplay().getCurrentPlayer().getField().getFreeMonsterFieldArea();
        toPutOn.putCard(card, true);
    }

    public String checkWinningConditions() {
        if (gameplay == null) return null;
        String message = null;
        if (gameplay.getOpponentPlayer().getLifePoints() <= 0)
            message = endARound(gameplay.getCurrentPlayer(), gameplay.getOpponentPlayer());
        else if (gameplay.getCurrentPlayer().getLifePoints() <= 0)
            message = endARound(gameplay.getOpponentPlayer(), gameplay.getCurrentPlayer());
        return message;
    }

    private boolean hasRitualMonsterInHand() {
        for (HandFieldArea hand :
                gameplay.getCurrentPlayer().getPlayingHand()) {
            if (hand.getCard() instanceof Monster)
                if (((Monster) hand.getCard()).getCardType().equals(CardType.RITUAL)) return true;
        }
        return false;
    }

    public boolean isLocationNumberInvalid(String string) {
        if (string.matches("^\\d+$")) {
            int id = Integer.parseInt(string);
            return (id <= 0 || id >= 6);
        }
        return true;
    }

    public boolean isOpponentFieldEmpty() {
        MonsterFieldArea[] monsterFieldAreas = gameplay.getOpponentPlayer().getField().getMonstersField();
        for (MonsterFieldArea monster : monsterFieldAreas) {
            if (monster.getCard() != null) return false;
        }
        return true;
    }

    private boolean hasAttackMonster() {
        MonsterFieldArea[] monsterFieldAreas = gameplay.getCurrentPlayer().getField().getMonstersField();
        for (MonsterFieldArea monster :
                monsterFieldAreas) {
            if (monster.isAttack()) return true;
        }
        return false;
    }

    private void makeCardVisible(MonsterFieldArea defendingMonster) {
        defendingMonster.setVisibility(true);
    }

    private boolean sumMeetsRitualMonsterLevel() {
        for (HandFieldArea hand :
                gameplay.getCurrentPlayer().getPlayingHand()) {
            if (hand.getCard() instanceof Monster)
                if (((Monster) hand.getCard()).getCardType().equals(CardType.RITUAL)) {
                    int levelSum = 0;
                    for (Card card :
                            gameplay.getCurrentPlayer().getPlayingDeck().getMainCards()) {
                        if (card instanceof Monster) levelSum += ((Monster) card).getLevel();
                        if (levelSum >= ((Monster) hand.getCard()).getLevel()) return true;
                    }
                }
        }
        return false;
    }

    public void forceAddCard(String cardName) throws InvalidCardNameException {
        Card c = Card.getCardByName(cardName);
        if (c == null) throw new InvalidCardNameException(cardName);
        HandFieldArea h = new HandFieldArea(c);
        for (Card card :
                gameplay.getCurrentPlayer().getPlayingDeck().getMainCards()) {
            if (card.getName().equals(c.getName())) {
                gameplay.getCurrentPlayer().getPlayingDeck().getMainCards().remove(card);
                return;
            }
        }
        gameplay.getCurrentPlayer().getPlayingHand().add(h);
        gameplay.getCurrentPlayer().getField().getHandFieldArea().getChildren().add(h);
    }

    public String setWinnerCheat(String nickname) {
        User userToWin = User.getUserByNickname(nickname);
        if (userToWin == null) return null;
        if (gameplay.getCurrentPlayer().getUser().equals(userToWin)) {
            return endWholeMatch(gameplay.getCurrentPlayer(), gameplay.getOpponentPlayer());
        } else if (gameplay.getOpponentPlayer().getUser().equals(userToWin)) {
            return endWholeMatch(gameplay.getOpponentPlayer(), gameplay.getCurrentPlayer());
        }
        return null;
    }

    public void increaseLifePointsCheat(int amount) {
        gameplay.getCurrentPlayer().setLifePoints(gameplay.getCurrentPlayer().getLifePoints() + amount);
    }

    public boolean isRitualInputsValid(String[] ids) {
        ArrayList<Card> mainCards = gameplay.getCurrentPlayer().getPlayingDeck().getMainCards();
        int levelSum = 0;
        int id;
        for (String idString :
                ids) {
            if ((id = Integer.parseInt(idString)) > mainCards.size()) return false;
            if (!(mainCards.get(id - 1) instanceof Monster)) return false;
            levelSum += ((Monster) mainCards.get(id - 1)).getLevel();
        }
        return levelSum >= ((Monster) gameplay.getSelectedField().getCard()).getLevel();
    }

    private void resetFieldZoneEffects() {
        MonsterFieldArea[] myField = gameplay.getCurrentPlayer().getField().getMonstersField();
        MonsterFieldArea[] opponentField = gameplay.getOpponentPlayer().getField().getMonstersField();
        for (MonsterFieldArea i : myField) {
            if (i.getCard() != null) {
                i.setAttackPoint(((Monster) i.getCard()).getAttackPoints());
                i.setDefensePoint(((Monster) i.getCard()).getDefensePoints());
            }
        }
        for (MonsterFieldArea j : opponentField) {
            if (j.getCard() != null) {
                j.setAttackPoint(((Monster) j.getCard()).getAttackPoints());
                j.setDefensePoint(((Monster) j.getCard()).getDefensePoints());
            }
        }
        for (SpellAndTrapFieldArea equipSpell :
                gameplay.getCurrentPlayer().getEquippedMonsters().keySet()) {
            equipSpell.getCard().equipEffect.activate(gameplay.getCurrentPlayer().getEquippedMonsters().get(equipSpell));
        }
        for (SpellAndTrapFieldArea equipSpell :
                gameplay.getOpponentPlayer().getEquippedMonsters().keySet()) {
            equipSpell.getCard().equipEffect.activate(gameplay.getOpponentPlayer().getEquippedMonsters().get(equipSpell));
        }
    }

    public void calculateFieldZoneEffects() {
        if (gameplay == null) return;
        resetFieldZoneEffects();
        if (gameplay.getCurrentPlayer().getField().getFieldZone().getCard() != null &&
            gameplay.getCurrentPlayer().getField().getFieldZone().visibility())
            ((Spell) gameplay.getCurrentPlayer().getField().getFieldZone().getCard()).fieldZoneEffect.activate();
        if (gameplay.getOpponentPlayer().getField().getFieldZone().getCard() != null &&
            gameplay.getOpponentPlayer().getField().getFieldZone().visibility())
            ((Spell) gameplay.getOpponentPlayer().getField().getFieldZone().getCard()).fieldZoneEffect.activate();
    }

    public void checkForAfterSummon() {
        if (gameplay.getRecentlySummonedMonster().getCard().afterSummon != null) {
            try {
                gameplay.getRecentlySummonedMonster().getCard().afterSummon.execute();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
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
}