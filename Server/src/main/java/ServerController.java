import Controller.*;
import Database.EfficientUser;
import Database.Message;
import Database.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;

public class ServerController {

    private static ServerController instance = null;
    private static HashMap<String, User> loggedInUsers = new HashMap<>();

    private ServerController() {

    }

    public static ServerController getInstance() {
        if (instance == null) instance = new ServerController();
        return instance;
    }

    public static HashMap<String, User> getLoggedInUsers() {
        return loggedInUsers;
    }

    public void newClient(DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
        while (true) {
            String clientMessage = dataInputStream.readUTF();
            System.out.println("CLIENT " + new SimpleDateFormat("HH:mm:ss").format(new Date()) + ":" + clientMessage);
            String serverMessage = process(clientMessage);
            System.out.println("SERVER " + new SimpleDateFormat("HH:mm:ss").format(new Date()) + ":" + serverMessage);
            if (serverMessage.equals("")) return;
            dataOutputStream.writeUTF(serverMessage);
            dataOutputStream.flush();
        }
    }

    public String process(String message) {
        Matcher matcher;
        String token;
        if ((matcher = Regex.getCommandMatcher(message, Regex.createUser)).matches()) return createUser(matcher);
        else if ((matcher = Regex.getCommandMatcher(message, Regex.login)).matches()) return loginUser(matcher);
        if (!tokenIsValid(message)) return "invalid token";
        else if ((matcher = Regex.getCommandMatcher(message, Regex.sendMessage)).matches()) return sendMessage(matcher);
        else if ((matcher = Regex.getCommandMatcher(message, Regex.requestMessages)).matches()) return getMessages();
        else if ((matcher = Regex.getCommandMatcher(message, Regex.getUser)).matches()) return requestUser(matcher);
        return "";
    }

    private String getMessages() {
        new Message("A","Kir mikhori?");
        new Message("B","Aghaye dildo");
        Gson gson = new GsonBuilder().create();
        String allMessages = gson.toJson(Message.messageList);
        return allMessages;
    }

    private String sendMessage(Matcher matcher) {
        String message = matcher.group("message");
        User currentUser = getUserByToken(matcher.group("token"));
        if (currentUser == null) return "ERROR";
        new Message(currentUser.getUsername(),message);
        return "SUCCESS";
    }

    private boolean tokenIsValid(String message) {
        String[] messageSplit = message.split(" ");
        for (String token : loggedInUsers.keySet()) {
            if (messageSplit[0].matches(token)) return true;
        }
        return false;
    }

    private User getUserByToken(String requestToken) {
        for (String token : loggedInUsers.keySet()) {
            if (requestToken.equals(token)){
                return loggedInUsers.get(token);
            }
        }
        return null;
    }

    private String requestUser(Matcher matcher){
        String requestToken = matcher.group("token");
        User user;
        user = getUserByToken(requestToken);
        if (user == null) return "user not found";
        EfficientUser efficientUser = new EfficientUser(user);
        Gson gson = new GsonBuilder().create();
        String userString = gson.toJson(efficientUser);
        return userString;
    }

    private String loginUser(Matcher matcher) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        try {
            String newUserToken = LoginController.loginUser(username, password);
            loggedInUsers.put(newUserToken, User.getUserByName(username));
            return newUserToken;
        } catch (Exception e) {
            return ("ERROR " + e.getMessage());
        }
    }

    private String createUser(Matcher matcher) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        String nickname = matcher.group("nickname");
        try {
            String newUserToken = LoginController.registerUser(username, password, nickname);
            loggedInUsers.put(newUserToken, User.getUserByName(username));
            return newUserToken;
        } catch (Exception e) {
            return ("ERROR " + e.getMessage());
        }
    }

}
