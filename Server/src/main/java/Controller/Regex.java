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

    public static String logout = "^(?<token>[A-Za-z0-9-]+) user logout$";
    public static String changeNickname = "^(?<token>[A-Za-z0-9-]+) profile change (?:--nickname|-n) (?<nickname>.+?)$";
    public static String changePassword = "^(?<token>[A-Za-z0-9-]+) profile change(?=.* --current|.* -c)(?=.* --password|.* -p)(?=.* --new|.* -n)" +
            "(?: (?:--password|-p)(?!.*?--password)|" +
            " (?:--current|-c) (?<currentPass>.+?)(?!.*?--current|.*?-c)|" +
            " (?:--new|-n) (?<newPass>.+?)(?!.*?--new|.*?-n)){3}$";
    public static String shopBuy = "^(?<token>[A-Za-z0-9-]+) shop buy (?<cardName>.+?)$";
    public static String shopSell = "^(?<token>[A-Za-z0-9-]+) shop sell (?<cardName>.+?)$";
    public static String shopAdmin = "^(?<token>[A-Za-z0-9-]+) shop admin (?<keycode>.+)";
    public static String shopIncrease = "^(?<token>[A-Za-z0-9-]+) shop increase (?<cardName>.+?)$";
    public static String shopDecrease = "^(?<token>[A-Za-z0-9-]+) shop decrease (?<cardName>.+?)$";
    public static String shopToggle = "^(?<token>[A-Za-z0-9-]+) shop toggle (?<cardName>.+?)$";

    public static String sendMessage = "^(?<token>[A-Za-z0-9-]+) send message -m (?<message>.+?)$";
    public static String requestMessages = "^(?<token>[A-Za-z0-9-]+) request messages$";
    public static String requestPinnedMessage = "^(?<token>[A-Za-z0-9-]+) request pinned message$";
    public static String requestUserSummary = "^(?<token>[A-Za-z0-9-]+) request user summary -u (?<username>.+?)$";
    public static String requestEfficientUsers = "^(?<token>[A-Za-z0-9-]+) request efficient users$";
    public static String requestOnlineCount = "^(?<token>[A-Za-z0-9-]+) request online count$";

    public static String requestCardStock = "^(?<token>[A-Za-z0-9-]+) request card stock";
    public static String requestUnavailableCards = "^(?<token>[A-Za-z0-9-]+) request unavailable cards";

    public static String getUser = "^(?<token>[A-Za-z0-9-]+) get user$";
    public static String prevAvatar = "^(?<token>[A-Za-z0-9-]+) prev avatar$";
    public static String nextAvatar = "^(?<token>[A-Za-z0-9-]+) next avatar$";
    public static String pinMessage = "^(?<token>[A-Za-z0-9-]+) pin message -id (?<id>.+?)$";
    public static String editMessage = "^(?<token>[A-Za-z0-9-]+) edit message -r (?<replace>.+?) -id (?<id>.+?)$";
    public static String deleteMessage = "^(?<token>[A-Za-z0-9-]+) delete message -id (?<id>.+?)$";
    public static String saveUser = "^(?<token>[A-Za-z0-9-]+) save user (?<userJson>.+)$";

    public static Matcher getCommandMatcher(String command, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(command);
    }
}