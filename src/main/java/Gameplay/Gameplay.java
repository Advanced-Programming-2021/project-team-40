package Gameplay;

import Database.Cards.Card;
import View.Exceptions.InvalidSideSwitchException;

import java.util.regex.Matcher;

import java.util.ArrayList;
import java.util.HashMap;

public class Gameplay {
    private Player playerOne;
    public int playerOneWins = 0;
    private Player playerTwo;
    public int playerTwoWins = 0;
    private Player currentPlayer;
    private Player opponentPlayer;
    private FieldArea selectedField = null;
    private FieldArea attacker;
    private FieldArea beingAttacked;
    private Phase currentPhase = Phase.DRAW_PHASE;
    private int currentRound;
    private int rounds;
    private Boolean ownsSelectedCard;
    private boolean hasPlacedMonster;
    private MonsterFieldArea recentlySummonedMonster;

    public Gameplay(Player playerOne, Player playerTwo, int rounds) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.rounds = rounds;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getOpponentPlayer() {
        return opponentPlayer;
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(Phase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public FieldArea getSelectedField() {
        return selectedField;
    }

    public void setSelectedField(FieldArea selectedField) {
        this.selectedField = selectedField;
    }

    public void setOpponentPlayer(Player opponentPlayer) {
        this.opponentPlayer = opponentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public boolean hasPlacedMonster() {
        return hasPlacedMonster;
    }

    public void setHasPlacedMonster(boolean hasPlacedMonster) {
        this.hasPlacedMonster = hasPlacedMonster;
    }

    public Boolean ownsSelectedCard() {
        return ownsSelectedCard;
    }

    public void setOwnsSelectedCard(Boolean ownsSelectedCard) {
        this.ownsSelectedCard = ownsSelectedCard;
    }

    public FieldArea getAttacker() {
        return attacker;
    }

    public void setAttacker(FieldArea attacker) {
        this.attacker = attacker;
    }

    public FieldArea getBeingAttacked() {
        return beingAttacked;
    }

    public void setBeingAttacked(FieldArea beingAttacked) {
        this.beingAttacked = beingAttacked;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public MonsterFieldArea getRecentlySummonedMonster() {
        return recentlySummonedMonster;
    }

    public void setRecentlySummonedMonster(MonsterFieldArea recentlySummonedMonster) {
        this.recentlySummonedMonster = recentlySummonedMonster;
    }

    public void switchCards(Matcher matcher, Player player) throws InvalidSideSwitchException {
        int mainDeck = Integer.parseInt(matcher.group("main"));
        int sideDeck = Integer.parseInt(matcher.group("side"));
        if (player.getPlayingDeck().getMainCards().size() < mainDeck || player.getPlayingDeck().getSideCards().size() < sideDeck) {
            throw new InvalidSideSwitchException();
        }
        System.out.println("Switching main deck card " + mainDeck + " with side deck card " + sideDeck);
        Card tempCard = player.getPlayingDeck().getSideCards().get(sideDeck - 1);
        player.getPlayingDeck().getSideCards().remove(tempCard);
        player.getPlayingDeck().getMainCards().add(tempCard);
        tempCard = player.getPlayingDeck().getMainCards().get(mainDeck - 1);
        player.getPlayingDeck().getMainCards().remove(tempCard);
        player.getPlayingDeck().getSideCards().add(tempCard);
    }
}
