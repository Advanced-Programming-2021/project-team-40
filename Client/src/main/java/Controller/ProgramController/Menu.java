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
        switch (this) {
            case LOGIN_MENU:
                return "Login";
            case MAIN_MENU:
                return "Main";
            case SHOP_MENU:
                return "Shop";
            case DECK_MENU:
                return "Deck";
            case PROFILE_MENU:
                return "Profile";
            case IMPORT_EXPORT_MENU:
                return "Import/Export";
            case DUEL_MENU:
                return "Duel";
            case SCOREBOARD_MENU:
                return "ScoreboardMenu";
            case GAMEPLAY:
                return "Gameplay";
            default:
                return "";
        }
    }
}
