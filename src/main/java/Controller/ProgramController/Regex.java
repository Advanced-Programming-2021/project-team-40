package Controller.ProgramController;

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
    public static String strongPassword = "^(?=(.*[a-z])+)(?=(.*[A-Z])+)(?=(.*[0-9])+).{8,}$";
    public static String menuNavigation = "^menu enter (?<menuName>.+?)$";
    public static String exitMenu = "^menu exit$";
    public static String showCurrentMenu = "^menu show-current$";
    public static String logout = "^user logout$";
    public static String showScoreboard = "^scoreboardMenu show$";
    public static String changeNickname = "^profile change --nickname (?<nickname>.+?)$";
    public static String changePassword = "^profile change(?=.* --current)(?=.* --password)(?=.* --new)" +
            "(?: --password(?!.*?--password)|" +
            " --current (?<currentPass>.+?)(?!.*?--current)|" +
            " --new (?<newPass>.+?)(?!.*?--new)){3}$";
    public static String showCards = "^deck show --cards$";
    public static String createDeck = "^deck create (?<deckName>.+?)$";
    public static String deleteDeck = "^deck delete (?<deckName>.+?)$";
    public static String activateDeck = "^deck set-activate (?<deckName>.+?)$";
    public static String addCardToDeck = "^deck add-card(?=.* --card)(?=.* --deck)" +
            "(?: --card (?<cardName>.+?)(?!.*?--card)|" +
            " --deck (?<deckName>.+?)(?!.*?--deck)|" +
            "(?<isSide> --side)?(?!.*?--side)){3}$";
    public static String removeCardFromDeck = "^deck rm-card(?=.* --card)(?=.* --deck)" +
            "(?: --card (?<cardName>.+?)(?!.*?--card)|" +
            " --deck (?<deckName>.+?)(?!.*?--deck)|" +
            "(?<isSide> --side)?(?!.*?--side)){3}$";
    public static String showAllDeck = "^deck show --all$";
    public static String showOneDeck = "^deck show(?=.* --deck-name)" +
            "(?: --deck-name (?<deckName>.+?)(?!.*?--deck-name)|" +
            "(?<isSide> --side)?(?!.*?--side)){2}$";
    public static String shopBuy = "^shop buy (?<cardName>.+?)$";
    public static String shopShowAll = "^shop show --all$";
    public static String startPlayerDuel = "^duel(?=.* --second-player)(?=.* --new)(?=.* --rounds)" +
            "(?: --second-player (?<username>.+?)(?!.*?--second-player)|" +
            " --new(?!.*?--new)|" +
            " --rounds (?<rounds>.+?)(?!.*?--rounds)){3}$";
    public static String startAIDuel = "^duel(?=.* --ai)(?=.* --new)(?=.* --rounds)" +
                                       "(?: --ai(?!.*?--ai)|" +
                                       " --new(?!.*?--new)|" +
                                       " --rounds (?<rounds>.+?)(?!.*?--rounds)){3}$";
    public static String nextPhase = "^next phase$";
    public static String endPhase = "^end phase$";
    public static String selectMonsterCard = "^select(?=.* --monster)" +
                                      "(?: (?<type>--monster) (?<id>.+?)(?!.*?--monster)|" +
                                      "(?<isOpponent> --opponent)?(?!.*?--opponent)){2}$";
    public static String selectSpellCard = "^select(?=.* --spell)" +
                                           "(?: (?<type>--spell) (?<id>.+?)(?!.*?--spell)|" +
                                           "(?<isOpponent> --opponent)?(?!.*?--opponent)){2}$";
    public static String selectHandCard = "^select(?=.* --hand)" +
                                          "(?: (?<type>--hand) (?<id>.+?)(?!.*?--hand)|" +
                                          "(?<isOpponent> --opponent)?(?!.*?--opponent)){2}$";
    public static String selectFieldZoneCard = "^select(?=.* --field)" +
                                               "(?: (?<type>--field) (?!.*?--field)|" +
                                               "(?<isOpponent> --opponent)?(?!.*?--opponent)){2}$";
    public static String deselectCard = "^select -d$";
    public static String summon = "^summon$";
    public static String set = "^set$";
    public static String setPosition = "^set --position (attack|defense)$";
    public static String flipSummon = "^flip-summon$";
    public static String attack = "^attack (?<monsterId>.+?)$";
    public static String directAttack = "^attack direct$";
    public static String activateEffect = "^activate effect$";
    public static String showGraveyard = "^show graveyard$";
    public static String showSelectedCard = "^card show --selected$";
    public static String showCardByName = "^card show (?<cardName>.+?)$";
    public static String surrender = "^surrender$";
    public static String increaseMoneyCheatCode = "^increase --money (?<amount>.+?)$";
    public static String forceAddCardCheatCode = "^select(?: --hand (?<cardName>.+?)| --force){2}$";
    public static String increaseLifePointsCheatCode = "^increase --LP (?<amount>.+?)$";
    public static String setWinnerCheatCode = "^duel set-winner (?<nickname>.+?)$";
    public static String importCard = "^import card (?<cardName>.+?)$";
    public static String exportCard = "^export card (?<cardName>.+?)$";
    public static String back = "^back$";
    public static String help = "^help$";
    public static String cancelAction = "^cancel$";

    public static Matcher getCommandMatcher(String command, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(command);
    }
}
