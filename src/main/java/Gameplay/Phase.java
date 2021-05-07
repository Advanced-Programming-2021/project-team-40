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
        return switch (this) {
            case DRAW_PHASE -> "phase: draw phase";
            case STANDBY_PHASE -> "phase: standby phase";
            case MAIN_PHASE_ONE -> "phase: main phase 1";
            case BATTLE_PHASE -> "phase: battle phase";
            case MAIN_PHASE_TW0 -> "phase: main phase 2";
            case END_PHASE -> "phase: end phase";
        };
    }
}
