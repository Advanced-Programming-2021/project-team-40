package Controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    public static String login = "^user login(?=.* --username|.* -u)(?=.* --password|.* -p)" +
                                 "(?: (?:--username|-u) (?<username>.+?)(?!.*?--username|.*?-u)| " +
                                 "(?:--password|-p) (?<password>.+?)(?!.*?--password|.*?-p)){2}$";
    public static String createUser = "^user create(?=.* --username|.* -u)(?=.* --password|.* -p)(?=.* --nickname|.* -n)" +
                                      "(?: (?:--username|-u) (?<username>.+?)(?!.*?--username|.*?-u)| " +
                                      "(?:--nickname|-n) (?<nickname>.+?)(?!.*?--nickname|.*?-n)| " +
                                      "(?:--password|-p) (?<password>.+?)(?!.*?--password|.*?-p)){3}$";
    public static String strongPassword = "^(?=(.*[a-z])+)(?=(.*[A-Z])+)(?=(.*[0-9])+).{8,}$";
    public static String menuNavigation = "^menu enter (?<menuName>.+?)$";
    public static String exitMenu = "^menu exit$";
    public static String showCurrentMenu = "^menu show-current$";
    public static String logout = "^(?<token>[A-Za-z0-9-]+) user logout$";
    public static String showScoreboard = "^scoreboard show$";
    public static String changeNickname = "^(?<token>[A-Za-z0-9-]+) profile change (?:--nickname|-n) (?<nickname>.+?)$";
    public static String changePassword = "^(?<token>[A-Za-z0-9-]+) profile change(?=.* --current|.* -c)(?=.* --password|.* -p)(?=.* --new|.* -n)" +
            "(?: (?:--password|-p)(?!.*?--password)|" +
            " (?:--current|-c) (?<currentPass>.+?)(?!.*?--current|.*?-c)|" +
            " (?:--new|-n) (?<newPass>.+?)(?!.*?--new|.*?-n)){3}$";
    public static String showCards = "^deck show (?:--cards|-c)$";
    public static String createDeck = "^deck create (?<deckName>.+?)$";
    public static String deleteDeck = "^deck delete (?<deckName>.+?)$";
    public static String activateDeck = "^deck set-activate (?<deckName>.+?)$";
    public static String addCardToDeck = "^deck add-card(?=.* --card|.* -c)(?=.* --deck|.* -d)" +
            "(?: (?:--card|-c) (?<cardName>.+?)(?!.*?--card|.*?-c)|" +
            " (?:--deck|-d) (?<deckName>.+?)(?!.*?--deck|.*?-d)|" +
            "(?<isSide> (?:--side|-s))?(?!.*?--side|.*?-s)){3}$";
    public static String removeCardFromDeck = "^deck rm-card(?=.* --card|.* -c)(?=.* --deck|.* -d)" +
            "(?: (?:--card|-c) (?<cardName>.+?)(?!.*?--card|.*?-c)|" +
            " (?:--deck|-d) (?<deckName>.+?)(?!.*?--deck|.*?-d)|" +
            "(?<isSide> (?:--side|-s))?(?!.*?--side|.*?-s)){3}$";
    public static String showAllDeck = "^deck show (?:--all|-a)$";
    public static String showOneDeck = "^deck show(?=.* --deck-name|.* -d)" +
            "(?: (?:--deck-name|-d) (?<deckName>.+?)(?!.*?--deck-name|.*?-d)|" +
            "(?<isSide> (?:--side|-s))?(?!.*?--side|.*?-s)){2}$";
    public static String shopBuy = "^shop buy (?<cardName>.+?)$";
    public static String shopShowAll = "^shop show (?:--all|-a)$";
    public static String startPlayerDuel = "^duel(?=.* --second-player|.* -s)(?=.* --new|.* -n)(?=.* --rounds|.* -r)" +
            "(?: (?:--second-player|-s) (?<username>.+?)(?!.*?--second-player|.*-s)|" +
            " (?:--new|-n)(?!.*?--new|.*?-n)|" +
            " (?:--rounds|-r) (?<rounds>.+?)(?!.*?--rounds|.*?-r)){3}$";
    public static String startAIDuel = "^duel(?=.* --ai|.* -a)(?=.* --new|.* -n)(?=.* --rounds|.* -r)" +
            "(?: (?:--ai|-a)(?!.*?--ai|.*?-a)|" +
            " (?:--new|-n)(?!.*?--new|.*?-n)|" +
            " (?:--rounds|-r) (?<rounds>.+?)(?!.*?--rounds|.*?-r)){3}$";
    public static String nextPhase = "^next phase$";
    public static String endPhase = "^end phase$";
    public static String selectMonsterCard = "^select(?=.* --monster|.* -m)" +
            "(?: (?<type>--monster|-m) (?<id>.+?)(?!.*?--monster|.*?-m)|" +
            "(?<isOpponent> (?:--opponent|-o))?(?!.*?--opponent|.*?-o)){2}$";
    public static String selectSpellCard = "^select(?=.* --spell|.* -s)" +
            "(?: (?<type>--spell|-s) (?<id>.+?)(?!.*?--spell|.*?-s)|" +
            "(?<isOpponent> (?:--opponent|-o))?(?!.*?--opponent|.*?-o)){2}$";
    public static String selectHandCard = "^select(?=.* --hand|.* -h)" +
            "(?: (?<type>--hand|-h) (?<id>.+?)(?!.*?--hand|.*?-h)|" +
            "(?<isOpponent> (?:--opponent|-o))?(?!.*?--opponent|.*?-o)){2}$";
    public static String selectFieldZoneCard = "^select(?=.* --field|.* -f)" +
            "(?: (?<type>--field|-f)(?!.*?--field|.*?-f)|" +
            "(?<isOpponent> (?:--opponent|-o))?(?!.*?--opponent|.*?-o)){2}$";
    public static String deselectCard = "^select -d$";
    public static String summon = "^summon$";
    public static String set = "^set$";
    public static String setPosition = "^set (?:--position|-p) (attack|defense)$";
    public static String flipSummon = "^flip-summon$";
    public static String attack = "^attack (?<monsterId>.+?)$";
    public static String directAttack = "^attack direct$";
    public static String activateEffect = "^activate effect$";
    public static String showGraveyard = "^show graveyard(?<isOpponent> (?:--opponent|-o))?$";
    public static String showSelectedCard = "^card show (?:--selected|-s)$";
    public static String showCardByName = "^card show (?<cardName>.+?)$";
    public static String surrender = "^surrender$";
    public static String switchCard = "^card switch(?=.* --main|.* -m)(?=.* --side|.* -s)" +
                                      "(?: (?:--main|-m) (?<main>.+?)(?!.*?--main|.*?-m)| " +
                                      "(?:--side|-s) (?<side>.+?)(?!.*?--side|.*?-s)){2}$";
    public static String increaseMoneyCheatCode = "^increase (?:--money|-m) (?<amount>.+?)$";
    public static String forceAddCardCheatCode = "^select(?: (?:--hand|-h) (?<cardName>.+?)| (?:--force|-f)){2}$";
    public static String increaseLifePointsCheatCode = "^increase --LP (?<amount>.+?)$";
    public static String setWinnerCheatCode = "^duel set-winner (?<nickname>.+?)$";
    public static String addCardToHandCheatCode = "^add card (?<cardName>.+?)$";
    public static String importCard = "^import card (?<cardName>.+?)$";
    public static String exportCard = "^export card (?<cardName>.+?)$";
    public static String back = "^back$";
    public static String help = "^help$";
    public static String cancelAction = "^cancel$";
    public static String sendMessage = "^(?<token>[A-Za-z0-9-]+) send message -m (?<message>.+?)$";
    public static String requestMessages = "^(?<token>[A-Za-z0-9-]+) request messages$";
    public static String requestPinnedMessage = "^(?<token>[A-Za-z0-9-]+) request pinned message$";
    public static String requestEfficientUsers = "^(?<token>[A-Za-z0-9-]+) request efficient users$";
    public static String getUser = "^(?<token>[A-Za-z0-9-]+) get user$";
    public static String prevAvatar = "^(?<token>[A-Za-z0-9-]+) prev avatar$";
    public static String nextAvatar = "^(?<token>[A-Za-z0-9-]+) next avatar$";
    public static String pinMessage = "^(?<token>[A-Za-z0-9-]+) pin message -id (?<id>.+?)$";
    public static String editMessage = "^(?<token>[A-Za-z0-9-]+) edit message -r (?<replace>.+?) -id (?<id>.+?)$";
    public static String deleteMessage = "^(?<token>[A-Za-z0-9-]+) delete message -id (?<id>.+?)$";

    public static Matcher getCommandMatcher(String command, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(command);
    }
}