package Gameplay;

public class Gameplay {
    private Player playerOne;
    private Player playerTwo;
    private Player currentPlayer;
    private Player opponentPlayer;
    private FieldArea selectedField;
    private Phase currentPhase;
    private int rounds;
    private boolean hasPlacedMonster;

    public Gameplay(Player playerOne, Player playerTwo, int rounds) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.rounds = rounds;
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
}
