package Controller.DuelController;


import Database.Cards.Card;
import Gameplay.FieldArea;
import Gameplay.Gameplay;
import Gameplay.MonsterFieldArea;
import Gameplay.HandFieldArea;
import Gameplay.Phase;
import Gameplay.Player;
import View.CardView;
import View.Exceptions.*;

import java.util.ArrayList;

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

    public Gameplay getGameplay() {
        return gameplay;
    }

    public void goToNextPhase() {
        switch (gameplay.getCurrentPhase()) {
            case DRAW_PHASE:
                gameplay.setCurrentPhase(Phase.STANDBY_PHASE);
                System.out.println("phase: standby phase");
                break;
            case STANDBY_PHASE:
                gameplay.setCurrentPhase(Phase.MAIN_PHASE_ONE);
                System.out.println("phase: main phase one");
                break;
            case MAIN_PHASE_ONE:
                gameplay.setCurrentPhase(Phase.BATTLE_PHASE);
                System.out.println("phase: battle phase");
                break;
            case BATTLE_PHASE:
                gameplay.setCurrentPhase(Phase.MAIN_PHASE_TW0);
                System.out.println("phase: main phase two");
                break;
            case MAIN_PHASE_TW0:
                gameplay.setCurrentPhase(Phase.END_PHASE);
                System.out.println("phase: end phase");
                break;
            case END_PHASE:
                gameplay.setCurrentPhase(Phase.DRAW_PHASE);
                System.out.println(gameplay.getCurrentPlayer().getUser().getNickname() + "'s turn is completed");
                break;
        }
    }

    public void setStartingPlayer() {

    }

    public void dealCardsAtBeginning() {

    }

    public void drawCard() {

    }

    public void switchTurn() {

    }

    public void endGame() {

    }

    public void surrender() {

    }

    public void selectCard(String idToCheck, String field, boolean isFromOpponent) throws InvalidCardSelectionException, NoCardFoundException {
        FieldArea fieldArea;
        int id;
        switch (field) {
            case "monster":
                if (isNumberInvalid(idToCheck)) throw new InvalidCardSelectionException();
                id = Integer.parseInt(idToCheck) - 1;
                if (isFromOpponent) fieldArea = gameplay.getOpponentPlayer().getField().getMonstersFieldById(id);
                else fieldArea = gameplay.getCurrentPlayer().getField().getMonstersFieldById(id);
                if (fieldArea.getCard() == null) throw new NoCardFoundException();
                gameplay.setSelectedField(fieldArea);
                break;
            case "spell":
                if (isNumberInvalid(idToCheck)) throw new InvalidCardSelectionException();
                id = Integer.parseInt(idToCheck) - 1;
                if (isFromOpponent) fieldArea = gameplay.getOpponentPlayer().getField().getSpellAndTrapFieldById(id);
                else fieldArea = gameplay.getCurrentPlayer().getField().getSpellAndTrapFieldById(id);
                if (fieldArea.getCard() == null) throw new NoCardFoundException();
                gameplay.setSelectedField(fieldArea);
                break;
            case "hand":
                if (isNumberInvalid(idToCheck)) throw new InvalidCardSelectionException();
                id = Integer.parseInt(idToCheck) - 1;
                fieldArea = gameplay.getCurrentPlayer().getPlayingHand().get(id);
                if (fieldArea == null) throw new NoCardFoundException();
                gameplay.setSelectedField(fieldArea);
                break;
            case "fieldZone":
                if (isFromOpponent) fieldArea = gameplay.getOpponentPlayer().getField().getFieldZone();
                else fieldArea = gameplay.getCurrentPlayer().getField().getFieldZone();
                if (fieldArea.getCard() == null) throw new NoCardFoundException();
                gameplay.setSelectedField(fieldArea);
                break;
        }
        gameplay.setOwnsSelectedCard(!isFromOpponent);
    }

    public void deselectCard() throws NoCardIsSelectedException {
        if (gameplay.getSelectedField().getCard() == null) throw new NoCardIsSelectedException();
        gameplay.setSelectedField(null);
        gameplay.setOwnsSelectedCard(null);
    }

    public void showCard() throws NoCardIsSelectedException {
        Card card = gameplay.getSelectedField().getCard();
        if (card == null) throw new NoCardIsSelectedException();
        CardView.showCard(card);
    }

    public void putMonsterCard(boolean isAttack) throws NoCardIsSelectedException, AlreadySummonedException, InvalidSummonException, MonsterZoneFullException, WrongPhaseException {
        //TODO: handle tribute summon and ritual summons
        FieldArea fieldArea = gameplay.getSelectedField();
        if (fieldArea.getCard() == null) throw new NoCardIsSelectedException();
        if (!fieldArea.canBePutOnBoard() || !gameplay.isOwnsSelectedCard() || !(fieldArea instanceof HandFieldArea))//???????????????
            throw new InvalidSummonException();
        if (!gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_ONE) && !gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_TW0))
            throw new WrongPhaseException();
        MonsterFieldArea monsterFieldArea = gameplay.getCurrentPlayer().getField().getFreeMonsterFieldArea();
        if (monsterFieldArea == null) throw new MonsterZoneFullException();
        if (gameplay.hasPlacedMonster()) throw new AlreadySummonedException();
        monsterFieldArea.putCard(fieldArea.getCard(), isAttack);
        monsterFieldArea.setAttack(isAttack);
        gameplay.getCurrentPlayer().getPlayingHand().remove(fieldArea);
        deselectCard();
    }

    public void tribute(ArrayList<Integer> toTributeIds) {

    }

    public void ritualSummon() {

    }

    public void flipSummon() {

    }

    public void changePosition(boolean isAttack) throws NoCardIsSelectedException, InvalidChangePositionException, WrongPhaseException, AlreadyInPositionException, AlreadySetPositionException {
        FieldArea fieldArea = gameplay.getSelectedField();
        if (fieldArea.getCard() == null) throw new NoCardIsSelectedException();
        if (!(fieldArea instanceof MonsterFieldArea) || !gameplay.isOwnsSelectedCard())
            throw new InvalidChangePositionException();
        if (!gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_ONE) && !gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_TW0))
            throw new WrongPhaseException();
        if (((MonsterFieldArea) fieldArea).isAttack() == isAttack) throw new AlreadyInPositionException();
        if (((MonsterFieldArea) fieldArea).hasSwitchedMode()) throw new AlreadySetPositionException();
        ((MonsterFieldArea) fieldArea).changePosition();
        deselectCard();
    }

    public StringBuilder attack(String number) throws Exception {
        FieldArea fieldArea = gameplay.getSelectedField();
        Player opponent = gameplay.getOpponentPlayer();
        if (fieldArea == null) throw new NoCardIsSelectedException();
        if (!(fieldArea instanceof MonsterFieldArea) || !gameplay.isOwnsSelectedCard())
            throw new AttackNotPossibleException();
        if (!GameplayController.getInstance().getGameplay().getCurrentPhase().equals(Phase.BATTLE_PHASE))
            throw new WrongPhaseException();
        if (((MonsterFieldArea) fieldArea).hasAttacked()) throw new AlreadyAttackedException();
        if (isNumberInvalid(number)) throw new InvalidCardSelectionException();
        int id = Integer.parseInt(number);
        if (opponent.getField().getMonstersFieldById(id) == null) throw new NoCardToAttackException();
        StringBuilder temp = calculateDamage(id);
        deselectCard();
        return temp;
    }

    private StringBuilder calculateDamage(int attackingFieldId) {
        MonsterFieldArea attackingMonster = (MonsterFieldArea) gameplay.getSelectedField();
        MonsterFieldArea defendingMonster = gameplay.getOpponentPlayer().getField().getMonstersFieldById(attackingFieldId);
        if (defendingMonster.isAttack()) return calculateAttackVsAttackSituation(attackingMonster, defendingMonster);
        else return calculateAttackVsDefenceSituation(defendingMonster, attackingMonster);
    }

    private StringBuilder calculateAttackVsDefenceSituation(MonsterFieldArea defendingMonster, MonsterFieldArea attackingMonster) {
        StringBuilder message = new StringBuilder();
        int attackMonsterPoint = attackingMonster.getAttackPoint();
        int defenceMonsterPoint = defendingMonster.getDefensePoint();
        int damage = attackMonsterPoint - defenceMonsterPoint;
        if (!defendingMonster.isVisible()) {
            message.append("opponent’s monster card was ").append(defendingMonster.getCard().getName()).append(" and ");
            flipCard(defendingMonster);
        }
        if (damage > 0) {
            destroyMonsterCard(gameplay.getOpponentPlayer(), defendingMonster);
            message.append("the defense position monster is destroyed");
        }
        if (damage < 0) {
            int newLP = gameplay.getCurrentPlayer().getLifePoints() + damage;
            gameplay.getCurrentPlayer().setLifePoints(newLP);
            checkWinningConditions();
            message.append("no card is destroyed and you received ").append(-damage).append(" battle damage");
        }
        if (damage == 0) message.append("no card is destroyed");
        return message;
    }

    private StringBuilder calculateAttackVsAttackSituation(MonsterFieldArea attackingMonster, MonsterFieldArea defendingMonster) {
        StringBuilder message = new StringBuilder();
        int attackMonsterPoint = attackingMonster.getAttackPoint();
        int defenceMonsterPoint = defendingMonster.getAttackPoint();
        int damage = attackMonsterPoint - defenceMonsterPoint;
        if (damage > 0) {
            destroyMonsterCard(gameplay.getOpponentPlayer(), defendingMonster);
            int newLP = gameplay.getOpponentPlayer().getLifePoints() - damage;
            gameplay.getOpponentPlayer().setLifePoints(newLP);
            checkWinningConditions();
            message.append("your opponent’s monster is destroyed and your opponent receives").append(damage).append(" battle damage");
        }
        if (damage < 0) {
            destroyMonsterCard(gameplay.getCurrentPlayer(), attackingMonster);
            int newLP = gameplay.getCurrentPlayer().getLifePoints() + damage;
            gameplay.getCurrentPlayer().setLifePoints(newLP);
            checkWinningConditions();
            message.append("Your monster card is destroyed and you received ").append(-damage).append(" battle damage");
        }
        if (damage == 0) {
            destroyMonsterCard(gameplay.getOpponentPlayer(), defendingMonster);
            destroyMonsterCard(gameplay.getCurrentPlayer(), attackingMonster);
            message.append("both you and your opponent monster cards are destroyed and no one receives damage");
        }
        return message;
    }

    public void directAttack() {

    }

    public void cancelAction() {

    }

    private void destroyMonsterCard(Player player, MonsterFieldArea monster) {
        moveCardToGraveyard(player, monster.getCard());
    }

    private void moveCardToGraveyard(Player player, Card card) {
        player.getField().getGraveyard().add(card);
    }

    private void checkWinningConditions() {

    }

    private boolean isNumberInvalid(String string) {
        if (string.matches("^\\d+$")) {
            int id = Integer.parseInt(string);
            return id <= 0 || id >= 6;
        }
        return true;
    }

    private void flipCard(MonsterFieldArea defendingMonster) {
        defendingMonster.setVisibility(true);
    }
}