package Controller.DuelController;


import Controller.MenuController.DeckMenuController;
import Controller.ProgramController.Menu;
import Controller.ProgramController.ProgramController;
import Controller.ProgramController.Regex;
import Database.Cards.*;
import Database.Cards.Card;
import Database.Cards.Monster;
import Database.Cards.Spell;
import Database.Cards.Trap;
import Database.User;
import Gameplay.*;
import View.CardView;
import View.Exceptions.*;
import View.GameplayView;

import javax.swing.text.FieldView;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;

public class GameplayController {
    private static GameplayController gameplayController = null;
    private ArrayList<Card> cardsToDeactivateAtEndPhase = new ArrayList<>();
    private ArrayList<Card> continuousEffectCards = new ArrayList<>();
    private ArrayList<FieldArea> effectSpellAndTraps = new ArrayList<>();
    public Gameplay gameplay;

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

    public Gameplay getGameplay() {
        return gameplay;
    }

    public void doPhaseAction() {
        switch (gameplay.getCurrentPhase()) {
            case DRAW_PHASE:
                Card card = drawCard();
                System.out.println("new card added to the hand : " + card.getName());
                goToNextPhase();
                break;
            case STANDBY_PHASE:
                try {
                    onStandbyTraps();
                } catch (ActionNotPossibleException | AttackNotPossibleException e) {
                    System.out.println(e.getMessage());
                }
                goToNextPhase();
                break;
            case END_PHASE:
                checkWinningConditions();
                switchTurn();
                goToNextPhase();
                break;
        }
    }

    public void goToNextPhase() {
        if (GameplayController.getInstance().getGameplay().getSelectedField() != null) {
            try {
                deselectCard();
            } catch (NoCardIsSelectedException ignored) {
            }
        }
        switch (gameplay.getCurrentPhase()) {
            case DRAW_PHASE:
                gameplay.setCurrentPhase(Phase.STANDBY_PHASE);
                System.out.println(gameplay.getCurrentPhase().toString());
                doPhaseAction();
                break;
            case STANDBY_PHASE:
                gameplay.setCurrentPhase(Phase.MAIN_PHASE_ONE);
                System.out.println(gameplay.getCurrentPhase().toString());
                doPhaseAction();
                break;
            case MAIN_PHASE_ONE:
                if (GameplayView.getInstance().isFirstOfGame()) gameplay.setCurrentPhase(Phase.END_PHASE);
                else if (!hasAttackMonster()) gameplay.setCurrentPhase(Phase.END_PHASE);
                else gameplay.setCurrentPhase(Phase.BATTLE_PHASE);
                System.out.println(gameplay.getCurrentPhase().toString());
                doPhaseAction();
                break;
            case BATTLE_PHASE:
                if (!hasAttackMonster()) gameplay.setCurrentPhase(Phase.END_PHASE);
                else gameplay.setCurrentPhase(Phase.MAIN_PHASE_TW0);
                System.out.println(gameplay.getCurrentPhase().toString());
                doPhaseAction();
                break;
            case MAIN_PHASE_TW0:
                gameplay.setCurrentPhase(Phase.END_PHASE);
                System.out.println(gameplay.getCurrentPhase().toString());
                doPhaseAction();
                break;
            case END_PHASE:
                gameplay.setCurrentPhase(Phase.DRAW_PHASE);
                System.out.println(gameplay.getCurrentPhase().toString());
                doPhaseAction();
                break;
        }
    }

    public void goToEndPhase() {
        gameplay.setCurrentPhase(Phase.END_PHASE);
        System.out.println(gameplay.getCurrentPhase().toString());
        doPhaseAction();
    }

    public void setStartingPlayer() {
        int firstPlayerPick = new Random().nextInt(3);
        int secondPlayerPick = new Random().nextInt(3);
        if (firstPlayerPick == secondPlayerPick) {
            setStartingPlayer();
        } else {
            if (firstPlayerPick > secondPlayerPick) {
                gameplay.setCurrentPlayer(gameplay.getPlayerOne());
                gameplay.setOpponentPlayer(gameplay.getPlayerTwo());
            } else {
                gameplay.setCurrentPlayer(gameplay.getPlayerTwo());
                gameplay.setOpponentPlayer(gameplay.getPlayerOne());
            }
            System.out.println("its " + gameplay.getCurrentPlayer().getUser().getUsername() + "'s turn");
        }
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
        //TODO decrease remaining effect rounds
        gameplay.getCurrentPlayer().getField().endTurnActions();
        Player temp = gameplay.getOpponentPlayer();
        gameplay.setOpponentPlayer(gameplay.getCurrentPlayer());
        gameplay.setCurrentPlayer(temp);
        gameplay.setSelectedField(null);
        gameplay.setOwnsSelectedCard(null);
        gameplay.setHasPlacedMonster(false);
        GameplayView.getInstance().setFirstOfGame(false);
        System.out.println("its " + gameplay.getCurrentPlayer().getUser().getUsername() + "'s turn");
    }

    public void temporarySwitchTurn() {
        System.out.println("now it will be " + gameplay.getOpponentPlayer().getUser().getUsername() + "'s turn");
        Player temp = gameplay.getOpponentPlayer();
        gameplay.setOpponentPlayer(gameplay.getCurrentPlayer());
        gameplay.setCurrentPlayer(temp);
        gameplay.setSelectedField(null);
        gameplay.setOwnsSelectedCard(null);
        GameplayView.getInstance().showBoard();
    }

    public void endGame(Player winner, Player loser) {
        int multiplier = gameplay.getRounds();
        winner.getUser().increaseScore(1000 * multiplier);
        winner.getUser().increaseBalance((1000 + winner.getMaxLP()) * multiplier);
        loser.getUser().increaseBalance(100 * multiplier);
        setGameplay(null);
        ProgramController.getInstance().setCurrentMenu(Menu.MAIN_MENU);
    }

    public void endMatch(Player winner, Player loser) {
        winner.setMaxLP(winner.getLifePoints());
        if (winner.equals(gameplay.getPlayerOne())) gameplay.playerOneWins++;
        else gameplay.playerTwoWins++;
        switch (gameplay.getRounds()) {
            case 1:
                endGame(winner, loser);
                break;
            case 3:
                if (gameplay.playerOneWins == 2 || gameplay.playerTwoWins == 2)
                    endGame(winner, loser);
                break;
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
                dealCardsAtBeginning();
                GameplayView.getInstance().setFirstOfGame(true);
        }
    }

    public void surrender() {
        endMatch(gameplay.getOpponentPlayer(), gameplay.getCurrentPlayer());
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
                if (isFromOpponent)
                    fieldArea = gameplay.getOpponentPlayer().getField().getSpellAndTrapFieldById(id);
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
        int id = Integer.parseInt(idToCheck);
        return (gameplay.getCurrentPlayer().getPlayingHand().size() < id);
    }

    public void deselectCard() throws NoCardIsSelectedException {
        if (gameplay.getSelectedField() == null) throw new NoCardIsSelectedException();
        gameplay.setSelectedField(null);
        gameplay.setOwnsSelectedCard(null);
    }

    public void showCard() throws NoCardIsSelectedException {
        if (gameplay.getSelectedField() == null) throw new NoCardIsSelectedException();
        if (!gameplay.ownsSelectedCard() && !gameplay.getSelectedField().isVisible()) {
            CardView.invisibleCard();
        } else CardView.showCard(gameplay.getSelectedField().getCard());
    }

    public void activateEffect(SpellAndTrapActivationType type) throws NoCardIsSelectedException, InvalidActivateException, WrongPhaseForSpellException, AlreadyActivatedException, SpecialSummonNotPossibleException, PreparationNotReadyException, ActionNotPossibleException, MonsterZoneFullException, AttackNotPossibleException, RitualSummonNotPossibleException, CommandCancellationException, SpellZoneFullException {
        FieldArea fieldArea = gameplay.getSelectedField();
        if (fieldArea == null) throw new NoCardIsSelectedException();
        if (fieldArea.getCard() instanceof Monster) throw new InvalidActivateException();
        if (type.equals(SpellAndTrapActivationType.NORMAL) && !gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_ONE) && !gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_TW0))
            throw new WrongPhaseForSpellException();
        if (fieldArea.isVisible()) throw new AlreadyActivatedException();
        if (fieldArea.getCard() instanceof Trap) activateTrapEffect(type);
        else {
            try {
                if (((Spell) fieldArea.getCard()).getIcon().equals(Icon.RITUAL)) {
                    if (!hasRitualMonsterInHand()) throw new RitualSummonNotPossibleException();
                    if (!sumMeetsRitualMonsterLevel()) throw new RitualSummonNotPossibleException();
                    onSpellActivationTraps();
                    effectSpellAndTraps.add(fieldArea);
                    GameplayView.getInstance().selectRitualMonster();
                } else if (fieldArea.canBePutOnBoard()) {
                    SpellAndTrapFieldArea spell = gameplay.getCurrentPlayer().getField().getFreeSpellFieldArea();
                    if (spell == null) throw new SpellZoneFullException();

                    onSpellActivationTraps();
                    //TODO: set and activate at the same time
                }
            } catch (ActionNotPossibleException | AttackNotPossibleException e) {
                temporarySwitchTurn();
                if (e.getMessage().equals("Opponent's magic jammer destroyed your card!"))
                    destroySpellAndTrapCard(gameplay.getCurrentPlayer(), (SpellAndTrapFieldArea) fieldArea);
                System.out.println(e.getMessage());
            }
        }
    }

    private void activateTrapEffect(SpellAndTrapActivationType type) throws
            PreparationNotReadyException, AttackNotPossibleException, ActionNotPossibleException, SpecialSummonNotPossibleException, MonsterZoneFullException, NoCardIsSelectedException {
        FieldArea fieldArea = gameplay.getSelectedField();
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
            case NORMAL:
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
        if (fieldArea.getCard() instanceof Monster) {
            MonsterFieldArea monster = gameplay.getCurrentPlayer().getField().getFreeMonsterFieldArea();
            if (monster == null) throw new MonsterZoneFullException();
            if (gameplay.hasPlacedMonster()) throw new AlreadySummonedException();
            if (((Monster) fieldArea.getCard()).getCardType().equals(CardType.RITUAL)) {
                FieldArea ritualSpell = null;
                for (FieldArea spellAndTrap : effectSpellAndTraps) {
                    ritualSpell = spellAndTrap;
                    if (spellAndTrap.getCard() instanceof Spell)
                        if (!((Spell) spellAndTrap.getCard()).getIcon().equals(Icon.RITUAL))
                            throw new InvalidSummonException();
                }
                if (ritualSpell == null) throw new InvalidSummonException();
                ritualSet(GameplayView.getInstance().ritualTribute());
            }
            if (((Monster) fieldArea.getCard()).getLevel() > 4)
                tributeCards(GameplayView.getInstance().getTributes(((Monster) fieldArea.getCard()).getNumberOfTributes()));
            setMonsterCard(monster, (HandFieldArea) fieldArea);
            deselectCard();
        } else if (fieldArea.getCard() instanceof Spell || fieldArea.getCard() instanceof Trap) {
            SpellAndTrapFieldArea s;
            if (((SpellAndTrap) fieldArea.getCard()).getIcon().equals(Icon.FIELD))
                s = gameplay.getCurrentPlayer().getField().getFieldZone();
            else s = gameplay.getCurrentPlayer().getField().getFreeSpellFieldArea();
            if (s == null) throw new SpellZoneFullException();
            setSpellCard(s, (HandFieldArea) fieldArea);
            deselectCard();
        }
    }

    public void summon() throws Exception {
        FieldArea fieldArea = gameplay.getSelectedField();
        if (fieldArea == null) throw new NoCardIsSelectedException();
        if (!(fieldArea.getCard() instanceof Monster) || !gameplay.ownsSelectedCard())
            throw new InvalidSummonException();
        if (!gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_ONE) && !gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_TW0))
            throw new WrongPhaseException();
        MonsterFieldArea monsterFieldArea = gameplay.getCurrentPlayer().getField().getFreeMonsterFieldArea();
        if (monsterFieldArea == null) throw new MonsterZoneFullException();
        if (gameplay.hasPlacedMonster()) throw new AlreadySummonedException();
        if (((Monster) fieldArea.getCard()).getCardType().equals(CardType.RITUAL)) checkForRitual();
        if (fieldArea.getCard().uniqueSummon != null) fieldArea.getCard().uniqueSummon.summon();
        else if (((Monster) fieldArea.getCard()).getLevel() > 4)
            tributeCards(GameplayView.getInstance().getTributes(((Monster) fieldArea.getCard()).getNumberOfTributes()));
        normalSummon((HandFieldArea) fieldArea, monsterFieldArea);
        gameplay.setRecentlySummonedMonster(monsterFieldArea);
        onSummonTraps();
        deselectCard();
    }

    private void checkForRitual() throws Exception {
        FieldArea ritualSpell = null;
        for (FieldArea spellAndTrap :
                effectSpellAndTraps) {
            ritualSpell = spellAndTrap;
            if (spellAndTrap.getCard() instanceof Spell)
                if (!((Spell) spellAndTrap.getCard()).getIcon().equals(Icon.RITUAL))
                    throw new InvalidSummonException();
        }
        if (ritualSpell == null) throw new InvalidSummonException();
        ritualSummon(GameplayView.getInstance().ritualTribute(), ritualSpell);
    }

    private void tributeCards(ArrayList<MonsterFieldArea> toTribute) {
        for (MonsterFieldArea monsterFieldArea : toTribute) {
            destroyMonsterCard(gameplay.getCurrentPlayer(), monsterFieldArea);
        }
    }

    private void ritualSummon(String[] ids, FieldArea ritualSpell) {
        ArrayList<Card> mainCards = gameplay.getCurrentPlayer().getPlayingDeck().getMainCards();
        for (String idString :
                ids) {
            int id = Integer.parseInt(idString);
            GameplayController.getInstance().moveCardToGraveyard(gameplay.getCurrentPlayer(), mainCards.get(id));
            mainCards.remove(id);
        }
        effectSpellAndTraps.remove(ritualSpell);
        if (ritualSpell instanceof HandFieldArea)
            gameplay.getCurrentPlayer().getPlayingHand().remove(ritualSpell);
        if (ritualSpell instanceof SpellAndTrapFieldArea)
            destroySpellAndTrapCard(gameplay.getCurrentPlayer(), (SpellAndTrapFieldArea) ritualSpell);
        moveCardToGraveyard(gameplay.getCurrentPlayer(), ritualSpell.getCard());
        DeckMenuController.getInstance().shuffleDeck(gameplay.getCurrentPlayer().getPlayingDeck());
        normalSummon((HandFieldArea) gameplay.getSelectedField(), gameplay.getCurrentPlayer().getField().getFreeMonsterFieldArea());
    }

    private void ritualSet(String[] ids) {
        ArrayList<Card> mainCards = gameplay.getCurrentPlayer().getPlayingDeck().getMainCards();
        for (String idString :
                ids) {
            int id = Integer.parseInt(idString);
            GameplayController.getInstance().moveCardToGraveyard(gameplay.getCurrentPlayer(), mainCards.get(id));
            mainCards.remove(id);
        }
        setMonsterCard(gameplay.getCurrentPlayer().getField().getFreeMonsterFieldArea(), (HandFieldArea) gameplay.getSelectedField());
    }

    public void flipSummon() throws Exception {
        FieldArea fieldArea = gameplay.getSelectedField();
        if (fieldArea == null) throw new NoCardIsSelectedException();
        if (!(fieldArea instanceof MonsterFieldArea) || !gameplay.ownsSelectedCard())
            throw new InvalidChangePositionException();
        if (!gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_ONE) && !gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_TW0))
            throw new WrongPhaseException();
        if (((MonsterFieldArea) fieldArea).hasAttacked()) throw new AlreadySetPositionException();
        if (((MonsterFieldArea) fieldArea).isAttack() || fieldArea.isVisible()) throw new InvalidFlipSummonException();
        //TODO add necessary effects
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
        if ((attackTarget = opponent.getField().getMonstersFieldById(id)) == null) throw new NoCardToAttackException();
        gameplay.setAttacker(attacker);
        gameplay.setBeingAttacked(attackTarget);
        StringBuilder temp = null;
        try {
            onAttackTraps();
            if (attackTarget.getCard().onBeingAttacked != null) attackTarget.getCard().onBeingAttacked.execute();
            temp = calculateDamage((MonsterFieldArea) attacker, attackTarget);
            ((MonsterFieldArea) attacker).setHasAttacked(true);
            deselectCard();
            gameplay.setAttacker(null);
            gameplay.setBeingAttacked(null);
        } catch (Exception e) {
            temporarySwitchTurn();
            System.out.println(e.getMessage());
        } finally {
            for (SpellAndTrapFieldArea trap :
                    gameplay.getOpponentPlayer().getField().getSpellAndTrapField()) {
                if (trap.getCard() != null)
                    if (trap.getCard().onBeingAttacked != null) trap.setCanBeActivated(false);
            }
        }
        return temp;
    }

    private void onSpellActivationTraps() throws ActionNotPossibleException, AttackNotPossibleException {
        boolean spellTrapExists = false;
        for (SpellAndTrapFieldArea trap :
                gameplay.getOpponentPlayer().getField().getSpellAndTrapField()) {
            if (trap.getCard() != null && trap.getCard().onSpellActivation != null) {
                spellTrapExists = true;
                trap.setCanBeActivated(true);
            }
        }
        if (spellTrapExists) {
            temporarySwitchTurn();
            if (GameplayView.getInstance().spellAndTrapActivationPrompt())
                GameplayView.getInstance().spellAndTrapToChainPrompt(SpellAndTrapActivationType.ON_SPELL);
        }
    }

    private void onSummonTraps() throws ActionNotPossibleException, AttackNotPossibleException {
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
            temporarySwitchTurn();
            if (GameplayView.getInstance().spellAndTrapActivationPrompt()) {
                GameplayView.getInstance().spellAndTrapToChainPrompt(SpellAndTrapActivationType.ON_SUMMON);
            }
            temporarySwitchTurn();
        }
        for (SpellAndTrapFieldArea trap :
                gameplay.getOpponentPlayer().getField().getSpellAndTrapField()) {
            if (trap.getCard() != null)
                if (trap.getCard().onSummon != null) trap.setCanBeActivated(false);
        }
    }

    private void onStandbyTraps() throws ActionNotPossibleException, AttackNotPossibleException {
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
            temporarySwitchTurn();
            if (GameplayView.getInstance().spellAndTrapActivationPrompt())
                GameplayView.getInstance().spellAndTrapToChainPrompt(SpellAndTrapActivationType.ON_STANDBY);
            temporarySwitchTurn();
        }
        for (SpellAndTrapFieldArea trap :
                gameplay.getOpponentPlayer().getField().getSpellAndTrapField()) {
            if (trap.getCard() != null)
                if (trap.getCard().inStandbyPhase != null) {
                    trap.setCanBeActivated(false);
                }
        }
    }

    private void onAttackTraps() throws ActionNotPossibleException, AttackNotPossibleException {
        boolean attackTrapExists = false;
        for (SpellAndTrapFieldArea trap :
                gameplay.getOpponentPlayer().getField().getSpellAndTrapField()) {
            if (trap.getCard() != null)
                if (trap.getCard().onBeingAttacked != null) {
                    attackTrapExists = true;
                    trap.setCanBeActivated(true);
                }
        }
        if (attackTrapExists) {
            temporarySwitchTurn();
            if (GameplayView.getInstance().spellAndTrapActivationPrompt()) {
                GameplayView.getInstance().spellAndTrapToChainPrompt(SpellAndTrapActivationType.ON_ATTACKED);
            }
            temporarySwitchTurn();
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
        deselectCard();
        checkWinningConditions();
        //TODO winning blow print may not happen correctly
        return temp;
    }

    private StringBuilder calculateDamage(MonsterFieldArea attackingMonster, MonsterFieldArea beingAttackedMonster) throws Exception {
        StringBuilder message = new StringBuilder();
        boolean initialVisibility = beingAttackedMonster.isVisible();
        if (beingAttackedMonster.isAttack()) {
            int damage = calculateAttackVsAttackSituation(attackingMonster, beingAttackedMonster);
            if (damage > 0)
                message.append("your opponent’s monster is destroyed and your opponent receives").append(damage).append(" battle damage");
            if (damage < 0)
                message.append("Your monster card is destroyed and you received ").append(-damage).append(" battle damage");
            if (damage == 0)
                message.append("both you and your opponent monster cards are destroyed and no one receives damage");
        } else {
            if (!beingAttackedMonster.isVisible()) {
                message.append("opponent’s monster card was ").append(beingAttackedMonster.getCard().getName()).append(" and ");
                makeCardVisible(beingAttackedMonster);
            }
            int damage = calculateAttackVsDefenseSituation(beingAttackedMonster, attackingMonster);
            if (damage > 0) message.append("the defense position monster is destroyed");
            if (damage < 0)
                message.append("no card is destroyed and you received ").append(-damage).append(" battle damage");
            if (damage == 0) message.append("no card is destroyed");
        }
        if (beingAttackedMonster.getCard().afterDamageCalculation != null)
            beingAttackedMonster.getCard().afterDamageCalculation.execute(initialVisibility);
        return message;
    }

    private int calculateAttackVsDefenseSituation(MonsterFieldArea defense, MonsterFieldArea attack) {
        int attackMonsterPoint = attack.getAttackPoint();
        int defenseMonsterPoint = defense.getDefensePoint();
        int damage = attackMonsterPoint - defenseMonsterPoint;
        if (!defense.isVisible()) if (damage > 0) destroyMonsterCard(gameplay.getOpponentPlayer(), defense);
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
        if (damage > 0) {
            destroyMonsterCard(gameplay.getOpponentPlayer(), defense);
            int newLP = gameplay.getOpponentPlayer().getLifePoints() - damage;
            gameplay.getOpponentPlayer().setLifePoints(newLP);
        } else if (damage < 0) {
            destroyMonsterCard(gameplay.getCurrentPlayer(), attack);
            int newLP = gameplay.getCurrentPlayer().getLifePoints() + damage;
            gameplay.getCurrentPlayer().setLifePoints(newLP);
        } else {
            destroyMonsterCard(gameplay.getOpponentPlayer(), defense);
            destroyMonsterCard(gameplay.getCurrentPlayer(), attack);
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
            if ((area.getCard()).onDestruction != null)
                (area.getCard()).onDestruction.execute(player);
            moveCardToGraveyard(player, area.getCard());
            area.setVisibility(false);
            area.putCard(null, false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void moveCardToGraveyard(Player player, Card card) {
        player.getField().getGraveyard().add(card);
    }

    private Card moveTopCardFromDeckToHand(Player player) {
        Card card = player.getPlayingDeck().getMainCards().get(0);
        HandFieldArea handFieldArea = new HandFieldArea(card);
        player.getPlayingHand().add(handFieldArea);
        player.getPlayingDeck().getMainCards().remove(card);
        return card;
    }

    public void discardACard() {
        ArrayList<HandFieldArea> hand = gameplay.getCurrentPlayer().getPlayingHand();
        while (true) {
            try {

                Matcher matcher;
                String command = ProgramController.getInstance().getScanner().nextLine();
                if ((matcher = Regex.getCommandMatcher(command, Regex.selectHandCard)).matches()) {
                    selectCard(matcher.group("id"), "--hand", false);
                    moveCardToGraveyard(gameplay.getCurrentPlayer(), gameplay.getSelectedField().getCard());
                    gameplay.getCurrentPlayer().getPlayingHand().remove(gameplay.getSelectedField());

                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setMonsterCard(MonsterFieldArea monsterFieldArea, HandFieldArea fieldArea) {
        monsterFieldArea.putCard(fieldArea.getCard(), false);
        gameplay.getCurrentPlayer().getPlayingHand().remove(fieldArea);
        gameplay.setHasPlacedMonster(true);
    }

    private void setSpellCard(SpellAndTrapFieldArea spellAndTrapFieldArea, HandFieldArea handFieldArea) {
        if (spellAndTrapFieldArea instanceof FieldZoneArea && spellAndTrapFieldArea.getCard() != null)
            destroySpellAndTrapCard(gameplay.getCurrentPlayer(), spellAndTrapFieldArea);
        spellAndTrapFieldArea.putCard(handFieldArea.getCard(), false);
        gameplay.getCurrentPlayer().getPlayingHand().remove(handFieldArea);
    }


    private void normalSummon(HandFieldArea fieldArea, MonsterFieldArea monsterFieldArea) {
        monsterFieldArea.putCard(fieldArea.getCard(), true);
        gameplay.getCurrentPlayer().getPlayingHand().remove(fieldArea);
        gameplay.setHasPlacedMonster(true);
    }

    public void specialSummon(Card card) {
        MonsterFieldArea toPutOn = GameplayController.getInstance().getGameplay().getCurrentPlayer().getField().getFreeMonsterFieldArea();
        toPutOn.putCard(card, true);

    }

    public void checkWinningConditions() {
        if (gameplay.getOpponentPlayer().getLifePoints() <= 0) {
            endMatch(gameplay.getCurrentPlayer(), gameplay.getOpponentPlayer());
        } else if (gameplay.getCurrentPlayer().getLifePoints() <= 0) {
            endMatch(gameplay.getOpponentPlayer(), gameplay.getCurrentPlayer());
        }
        //TODO check for more possible conditions

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

    private boolean isOpponentFieldEmpty() {
        MonsterFieldArea[] monsterFieldAreas = gameplay.getOpponentPlayer().getField().getMonstersField();
        for (MonsterFieldArea monster : monsterFieldAreas) {
            if (monster != null) return false;
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

    public int getMonsterFieldCount(Player player) {
        int count = 0;
        for (MonsterFieldArea m : player.getField().getMonstersField()
        ) {
            if (m.getCard() != null) count++;
        }
        return count;
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
        gameplay.getCurrentPlayer().getPlayingHand().add(h);
    }

    public void setWinnerCheat(String nickname) {
        User userToWin = User.getUserByNickname(nickname);
        if (gameplay.getCurrentPlayer().getUser().equals(userToWin)){
            endGame(gameplay.getCurrentPlayer(), gameplay.getOpponentPlayer());
        }else if (gameplay.getOpponentPlayer().getUser().equals(userToWin)){
            endGame(gameplay.getOpponentPlayer(), gameplay.getCurrentPlayer());
        }
    }

    public void increaseLifePointsCheat(int amount) {
        gameplay.getCurrentPlayer().setLifePoints(gameplay.getCurrentPlayer().getLifePoints() + amount);
    }

    public ArrayList<FieldArea> getEffectSpellAndTraps() {
        return effectSpellAndTraps;
    }

    public void setEffectSpellAndTraps(ArrayList<FieldArea> effectSpellAndTraps) {
        this.effectSpellAndTraps = effectSpellAndTraps;
    }

    public boolean isRitualInputsValid(String[] ids) {
        String numRegex = "^\\d+$";
        ArrayList<Card> mainCards = gameplay.getCurrentPlayer().getPlayingDeck().getMainCards();
        int levelSum = 0;
        for (String idString :
                ids) {
            if (!idString.matches(numRegex)) return false;
            if (Integer.parseInt(idString) <= mainCards.size()) return false;
            int id = Integer.parseInt(idString);
            if (!(mainCards.get(id) instanceof Monster)) return false;
            levelSum += ((Monster) mainCards.get(id)).getLevel();
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
    }

    public void calculateFieldZoneEffects() {
        resetFieldZoneEffects();
        if (gameplay.getCurrentPlayer().getField().getFieldZone().getCard() != null)
            ((Spell) gameplay.getCurrentPlayer().getField().getFieldZone().getCard()).fieldZoneEffect.activate();
        if (gameplay.getOpponentPlayer().getField().getFieldZone().getCard() != null)
            ((Spell) gameplay.getOpponentPlayer().getField().getFieldZone().getCard()).fieldZoneEffect.activate();
    }

}