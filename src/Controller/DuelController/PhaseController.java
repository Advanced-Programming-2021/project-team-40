package Controller.DuelController;

import GamePlay.Gameplay;
import GamePlay.Phase;

public class PhaseController {
    private static PhaseController phaseController;

    private PhaseController() {

    }

    public static PhaseController getInstance() {
        if (phaseController == null) phaseController = new PhaseController();
        return phaseController;
    }
    public void constantPhaseActions(Phase currentPhase){
        Gameplay gameplay = GameplayController.getInstance().getGameplay();
        switch (currentPhase){

            case DRAW_PHASE -> {
                PlayerController.getInstance().drawACard(gameplay.getCurrentPlayer());
                if (gameplay.getCurrentPlayer().getPlayingDeck().getMainCards().size() == 0) ;//TODO declare winner
//                PhaseController.getInstance().goToNextPhase(gameplay.getCurrentPhase());
            }
            case STANDBY_PHASE -> {
//                PhaseController.getInstance().goToNextPhase(gameplay.getCurrentPhase());

            }
            case END_PHASE -> {

            }
        }

    }

    public Phase goToNextPhase(Phase phase) {
        switch (phase) {

            case DRAW_PHASE -> {
                System.out.println("phase: standby phase");
                return Phase.STANDBY_PHASE;
            }
            case STANDBY_PHASE -> {
                System.out.println("phase: main phase one");
                return Phase.MAIN_PHASE_ONE;
            }
            case MAIN_PHASE_ONE -> {
                System.out.println("phase: battle phase");
                return Phase.BATTLE_PHASE;
            }
            case BATTLE_PHASE -> {
                System.out.println("phase: main phase two");
                return Phase.MAIN_PHASE_TW0;
            }
            case MAIN_PHASE_TW0 -> {
                System.out.println("phase: end phase");
                return Phase.END_PHASE;
            }
            case END_PHASE -> {
                System.out.println("your turn is completed");
                GameplayController.getInstance().changeTurn();
                return Phase.DRAW_PHASE;
            }
        }
    return null;
    }
}