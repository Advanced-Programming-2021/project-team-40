package Controller.ProgramController;

public enum Menu {
    LOGIN_MENU,
    MAIN_MENU,
    SHOP_MENU,
    DECK_MENU,
    PROFILE_MENU,
    IMPORT_EXPORT_MENU,
    EXIT,
    DUEL_MENU,
    SCOREBOARD_MENU,
    GAMEPLAY;

    @Override
    public String toString() {
        return switch (this) {
            case LOGIN_MENU -> "Login";
            case MAIN_MENU -> "Main";
            case SHOP_MENU -> "Shop";
            case DECK_MENU -> "Deck";
            case PROFILE_MENU -> "Profile";
            case IMPORT_EXPORT_MENU -> "Import/Export";
            case DUEL_MENU -> "Duel";
            case SCOREBOARD_MENU -> "Scoreboard";
            case GAMEPLAY -> "Gameplay";
            default -> "";
        };
    }
}
