package Gameplay;

public enum Phase {
    DRAW_PHASE,
    STANDBY_PHASE,
    MAIN_PHASE_ONE,
    BATTLE_PHASE,
    MAIN_PHASE_TW0,
    END_PHASE;

    @Override
    public String toString() {
        switch (this) {
            case DRAW_PHASE:
                return "phase: draw phase";
            case STANDBY_PHASE:
                return "phase: standby phase";
            case MAIN_PHASE_ONE:
                return "phase: main phase 1";
            case BATTLE_PHASE:
                return "phase: battle phase";
            case MAIN_PHASE_TW0:
                return "phase: main phase 2";
            case END_PHASE:
                return "phase: end phase";
            default:
                throw new IllegalArgumentException();
        }
    }
}
