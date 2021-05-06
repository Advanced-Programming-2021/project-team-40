package Controller.DuelController;


import Database.Cards.Card;
import Database.Cards.Monster;
import Database.Cards.Spell;
import Database.Cards.Trap;
import Gameplay.FieldArea;
import Gameplay.Gameplay;
import Gameplay.MonsterFieldArea;
import Gameplay.SpellAndTrapFieldArea;
import Gameplay.HandFieldArea;
import Gameplay.Phase;
import Gameplay.Player;
import View.CardView;
import View.Exceptions.*;
import View.GameplayView;

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
                Card newCard = drawCard();
                System.out.println("new card added to hand: " + newCard.getName());
                break;
        }
    }

    public void setStartingPlayer() {

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

    }

    public void endGame() {

    }

    public void surrender() {

    }

    public void selectCard(String idToCheck, String field, boolean isFromOpponent) throws Exception {
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

    public void set() throws Exception {
        FieldArea fieldArea = gameplay.getSelectedField();
        if (fieldArea.getCard() == null) throw new NoCardIsSelectedException();
        if (!fieldArea.canBePutOnBoard() || !gameplay.isOwnsSelectedCard())
            throw new InvalidSetException();
        if (!gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_ONE) && !gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_TW0))
            throw new WrongPhaseException();
        if (fieldArea.getCard() instanceof Monster) {
            MonsterFieldArea m = gameplay.getCurrentPlayer().getField().getFreeMonsterFieldArea();
            if (m == null) throw new MonsterZoneFullException();
            setMonsterCard(m, fieldArea);
        }
        if (fieldArea.getCard() instanceof Spell || fieldArea.getCard() instanceof Trap) {
            SpellAndTrapFieldArea s = gameplay.getCurrentPlayer().getField().getFreeSpellFieldArea();
            if (s == null) throw new SpellZoneFullException();
            setSpellCard(s, fieldArea);
        }
        deselectCard();
    }

    public void summon() throws Exception {
        FieldArea fieldArea = gameplay.getSelectedField();
        if (fieldArea.getCard() == null) throw new NoCardIsSelectedException();
        if (!(fieldArea.getCard() instanceof Monster) || !gameplay.isOwnsSelectedCard())
            throw new InvalidSummonException();
        if (!gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_ONE) && !gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_TW0))
            throw new WrongPhaseException();
        MonsterFieldArea monsterFieldArea = gameplay.getCurrentPlayer().getField().getFreeMonsterFieldArea();
        if (monsterFieldArea == null) throw new MonsterZoneFullException();
        if (gameplay.hasPlacedMonster()) throw new AlreadySummonedException();
        int cardLevel = ((Monster) fieldArea.getCard()).getLevel();
        if (cardLevel == 5 || cardLevel == 6) {
            if (getMonsterFieldCount() < 1) throw new NotEnoughCardsException();
            GameplayView.getInstance().setTributeMode(true);
        }
        if (cardLevel == 7 || cardLevel == 8) {
            if (getMonsterFieldCount() < 2) throw new NotEnoughCardsException();
            GameplayView.getInstance().setTributeMode(true);

        } else normalSummon(fieldArea, monsterFieldArea);
        deselectCard();
    }

    public void tributeSummon(ArrayList<String> toTributeIds) throws Exception {
        for (String idToCheck : toTributeIds
        ) {
            if (isNumberInvalid(idToCheck)) throw new InvalidCardSelectionException();
            int id = Integer.parseInt(idToCheck);
            if (gameplay.getCurrentPlayer().getField().getMonstersFieldById(id) == null)
                throw new InvalidCardAddressException();
        }
        for (String idToCheck : toTributeIds
        ) {
            int id = Integer.parseInt(idToCheck);
            MonsterFieldArea m = gameplay.getCurrentPlayer().getField().getMonstersFieldById(id);
            destroyMonsterCard(gameplay.getCurrentPlayer(), m);
            m.putCard(gameplay.getSelectedField().getCard(), true);
        }
        MonsterFieldArea monsterFieldArea = gameplay.getCurrentPlayer().getField().getFreeMonsterFieldArea();
        normalSummon(gameplay.getSelectedField(), monsterFieldArea);
    }

    public void ritualSummon() {

    }

    public void flipSummon() throws Exception {
        FieldArea fieldArea = gameplay.getSelectedField();
        if (fieldArea.getCard() == null) throw new NoCardIsSelectedException();
        if (!(fieldArea instanceof MonsterFieldArea) || !gameplay.isOwnsSelectedCard())
            throw new InvalidChangePositionException();
        if (!gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_ONE) && !gameplay.getCurrentPhase().equals(Phase.MAIN_PHASE_TW0))
            throw new WrongPhaseException();
        //TODO: fix doesn't handle same round flip-summon error
        if (((MonsterFieldArea) fieldArea).isAttack() || fieldArea.isVisible()) throw new InvalidFlipSummonException();

    }

    public void changePosition(boolean isAttack) throws Exception {
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
        if (!gameplay.getCurrentPhase().equals(Phase.BATTLE_PHASE))
            throw new WrongPhaseException();
        if (((MonsterFieldArea) fieldArea).hasAttacked()) throw new AlreadyAttackedException();
        if (isNumberInvalid(number)) throw new InvalidCardSelectionException();
        int id = Integer.parseInt(number);
        if (opponent.getField().getMonstersFieldById(id) == null) throw new NoCardToAttackException();
        StringBuilder temp = calculateDamage(id);
        ((MonsterFieldArea) fieldArea).setHasAttacked(true);
        deselectCard();
        checkWinningConditions();
        return temp;
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
        return temp;
    }

    private StringBuilder calculateDamage(int attackingFieldId) {
        MonsterFieldArea attackingMonster = (MonsterFieldArea) gameplay.getSelectedField();
        MonsterFieldArea defendingMonster;
        defendingMonster = gameplay.getOpponentPlayer().getField().getMonstersFieldById(attackingFieldId);
        if (defendingMonster.isAttack()) return calculateAttackVsAttackSituation(attackingMonster, defendingMonster);
        else return calculateAttackVsDefenceSituation(defendingMonster, attackingMonster);
    }

    private StringBuilder calculateAttackVsDefenceSituation(MonsterFieldArea defense, MonsterFieldArea attack) {
        StringBuilder message = new StringBuilder();
        int attackMonsterPoint = attack.getAttackPoint();
        int defenceMonsterPoint = defense.getDefensePoint();
        int damage = attackMonsterPoint - defenceMonsterPoint;
        if (!defense.isVisible()) {
            message.append("opponent’s monster card was ").append(defense.getCard().getName()).append(" and ");
            flipCard(defense);
        }
        if (damage > 0) {
            destroyMonsterCard(gameplay.getOpponentPlayer(), defense);
            message.append("the defense position monster is destroyed");
        }
        if (damage < 0) {
            int newLP = gameplay.getCurrentPlayer().getLifePoints() + damage;
            gameplay.getCurrentPlayer().setLifePoints(newLP);
            message.append("no card is destroyed and you received ").append(-damage).append(" battle damage");
        }
        if (damage == 0) message.append("no card is destroyed");
        return message;
    }

    private StringBuilder calculateAttackVsAttackSituation(MonsterFieldArea attack, MonsterFieldArea defense) {
        StringBuilder message = new StringBuilder();
        int attackMonsterPoint = attack.getAttackPoint();
        int defenceMonsterPoint = defense.getAttackPoint();
        int damage = attackMonsterPoint - defenceMonsterPoint;
        if (damage > 0) {
            destroyMonsterCard(gameplay.getOpponentPlayer(), defense);
            int newLP = gameplay.getOpponentPlayer().getLifePoints() - damage;
            gameplay.getOpponentPlayer().setLifePoints(newLP);
            message.append("your opponent’s monster is destroyed and your opponent receives")
                    .append(damage).append(" battle damage");
        }
        if (damage < 0) {
            destroyMonsterCard(gameplay.getCurrentPlayer(), attack);
            int newLP = gameplay.getCurrentPlayer().getLifePoints() + damage;
            gameplay.getCurrentPlayer().setLifePoints(newLP);
            message.append("Your monster card is destroyed and you received ").append(-damage).append(" battle damage");
        }
        if (damage == 0) {
            destroyMonsterCard(gameplay.getOpponentPlayer(), defense);
            destroyMonsterCard(gameplay.getCurrentPlayer(), attack);
            message.append("both you and your opponent monster cards are destroyed and no one receives damage");
        }
        return message;
    }

    private String calculateDirectDamage(MonsterFieldArea monster) {
        int damage = monster.getAttackPoint();
        int newLp = gameplay.getOpponentPlayer().getLifePoints() - damage;
        gameplay.getOpponentPlayer().setLifePoints(newLp);
        return "you opponent receives " + damage + " battle damage";
    }

    private void destroyMonsterCard(Player player, MonsterFieldArea monster) {
        moveCardToGraveyard(player, monster.getCard());
    }

    private void moveCardToGraveyard(Player player, Card card) {
        player.getField().getGraveyard().add(card);
    }

    private Card moveTopCardFromDeckToHand(Player player) {
        Card card = player.getPlayingDeck().getMainCards().get(0);
        HandFieldArea handFieldArea = new HandFieldArea(card);
        player.getPlayingHand().add(handFieldArea);
        player.getPlayingDeck().getMainCards().remove(card);
        return card;
    }

    private void setMonsterCard(MonsterFieldArea m, FieldArea fieldArea) {
        m.putCard(fieldArea.getCard(), false);
        m.setAttack(false);
        gameplay.getCurrentPlayer().getPlayingHand().remove(fieldArea);
    }
    private void setSpellCard(SpellAndTrapFieldArea s, FieldArea fieldArea) {
        s.putCard(fieldArea.getCard(), false);
        gameplay.getCurrentPlayer().getPlayingHand().remove(fieldArea);
    }

    private void normalSummon(FieldArea fieldArea, MonsterFieldArea monsterFieldArea) {
        monsterFieldArea.putCard(fieldArea.getCard(), true);
        monsterFieldArea.setAttack(true);
        gameplay.getCurrentPlayer().getPlayingHand().remove(fieldArea);
    }

    private void checkWinningConditions() {
        if (gameplay.getCurrentPlayer().getLifePoints() <= 0) {

        }

    }

    private boolean isNumberInvalid(String string) {
        if (string.matches("^\\d+$")) {
            int id = Integer.parseInt(string);
            return id <= 0 || id >= 6;
        }
        return true;
    }

    private boolean isOpponentFieldEmpty() {
        MonsterFieldArea[] monsterFieldAreas = gameplay.getOpponentPlayer().getField().getMonstersField();
        for (MonsterFieldArea monster : monsterFieldAreas
        ) {
            if (monster != null) return false;
        }
        return true;
    }

    private void flipCard(MonsterFieldArea defendingMonster) {
        defendingMonster.setVisibility(true);
    }

    private int getMonsterFieldCount() {
        int count = 0;
        for (MonsterFieldArea m : gameplay.getCurrentPlayer().getField().getMonstersField()
        ) {
            if (m.getCard() != null) count++;
        }
        return count;
    }
}