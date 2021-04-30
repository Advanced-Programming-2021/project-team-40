package MenuController.ProgramController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    public static String login = "^user login(?=.* --username)(?=.* --password)" +
                                 "(?: --username (?<username>.+?)(?!.*?--username)|" +
                                 " --password (?<password>.+?)(?!.*?--password)){2}$";
    public static String createUser = "^user create(?=.* --username)(?=.* --password)(?=.* --nickname)" +
                                     "(?: --username (?<username>.+?)(?!.*?--username)|" +
                                     " --nickname (?<nickname>.+?)(?!.*?--nickname)|" +
                                     " --password (?<password>.+?)(?!.*?--password)){3}$";
    public static String menuNavigation = "^menu enter (?<menu_name>.+?)$";
    public static String exitMenu = "^menu exit$";
    public static String showCurrentMenu = "^menu show-current$";
    public static String logout = "^user logout$";
    public static String showScoreboard = "^scoreboard show$";
    public static String changeNickname = "^profile change --nickname (?<nickname>.+?)$";
    public static String changePassword = "^profile change(?=.* --current)(?=.* --password)(?=.* --new)" +
                                          "(?: --password(?!.*?--password)|" +
                                          " --current (?<current_pass>.+?)(?!.*?--current)|" +
                                          " --new (?<new_pass>.+?)(?!.*?--new)){3}$";
    public static String showCards = "^deck show --cards$";
    public static String createDeck = "^deck create (?<deck_name>.+?)$";
    public static String deleteDeck = "^deck delete (?<deck_name>.+?)$";
    public static String activateDeck = "^deck set-activate (?<deck_name>.+?)$";
    public static String addCardToDeck = "^deck add-card(?=.* --card)(?=.* --deck)" +
                                         "(?: --card (?<card_name>.+?)(?!.*?--card)|" +
                                         " --deck (?<deck_name>.+?)(?!.*?--deck)|" +
                                         "(?<is_side> --side)?(?!.*?--side)){3}$";
    public static String removeCardFromDeck = "^deck rm-card(?=.* --card)(?=.* --deck)" +
                                              "(?: --card (?<card_name>.+?)(?!.*?--card)|" +
                                              " --deck (?<deck_name>.+?)(?!.*?--deck)|" +
                                              "(?<is_side> --side)?(?!.*?--side)){3}$";
    public static String showAllDeck = "^deck show --all$";
    public static String showOneDeck = "^deck show(?=.* --deck-name)" +
                                       "(?: --deck-name (?<deck_name>.+?)(?!.*?--deck-name)|" +
                                       "(?<is_side> --side)?(?!.*?--side)){2}$";
    public static String shopBuy = "^shop buy (?<card_name>.+?)$";
    public static String shopShowAll = "^shop show --all$";
    public static String startPlayerDuel = "^duel(?=.* --second-player)(?=.* --new)(?=.* --rounds)" +
                                           "(?: --second-player (?<username>.+?)(?!.*?--second-player)|" +
                                           " --new(?!.*?--new)|" +
                                           " --rounds (?<rounds>.+?)(?!.*?--rounds)){3}$";
    public static String startAIDuel = "^duel(?=.* --ai)(?=.* --new)(?=.* --rounds)" +
                                       "(?: --ai(?!.*?--ai)|" +
                                       " --new(?!.*?--new)|" +
                                       " --rounds (?<rounds>.+?)(?!.*?--rounds)){3}$";
    public static String selectCard = "^select(?<oppo1>.*? --opponent(?!.* --opponent))?(?:" +
                                      " --monster (?<monster_id>.+?)|" +
                                      " --spell (?<spell_id>.+?)(?! --spell)|" +
                                      " --field)" +
                                      "(?<oppo2> --opponent)?$";
    public static String deselectCard = "^select -d$";
    public static String summon = "^summon$";
    public static String set = "^set$";
    public static String setPosition = "^set --position (attack|defense)$";
    public static String flipSummon = "^flip-summon$";
    public static String attack = "^attack (?<monster_id>.+?)$";
    public static String directAttack = "^attack direct$";
    public static String activateEffect = "^activate effect$";
    public static String showGraveyard = "^show graveyard$";
    public static String showSelectedCard = "^card show --selected$";
    public static String surrender = "^surrender$";
    public static String increaseMoneyCheatCode = "^increase --money (?<amount>.+?)$";
    public static String forceAddCardCheatCode = "^select(?: --hand (?<card_name>.+?)| --force){2}$";
    public static String increaseLifePointsCheatCode = "^increase --LP (?<amount>.+?)$";
    public static String setWinnerCheatCode = "^duel set-winner (?<nickname>.+?)$";
    public static String importCard = "^import card (?<card_name>.+?)$";
    public static String exportCard = "^export card (?<card_name>.+?)$";
    public static String back = "^back$";

    public static Matcher getCommandMatcher(String command, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(command);
    }
}
