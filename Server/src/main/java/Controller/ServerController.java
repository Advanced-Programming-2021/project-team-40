package Controller;

import Controller.DatabaseController.DatabaseController;
import Controller.Exceptions.*;
import Controller.Exceptions.InvalidPasswordException;
import Controller.Exceptions.RepetitiveNicknameException;
import Controller.Exceptions.RepetitivePasswordException;
import Controller.Exceptions.WeakPasswordException;
import Controller.LoginController;
import Controller.ProfileController;
import Controller.Regex;
import Database.EfficientUser;
import Database.Message;
import Database.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;

public class ServerController {

    private static ServerController instance = null;
    private static HashMap<String, User> loggedInUsers = new HashMap<>();
    private static HashMap<Socket, String> loggedInSockets = new HashMap<>();

    private ServerController() {

    }

    public static ServerController getInstance() {
        if (instance == null) instance = new ServerController();
        return instance;
    }

    public static HashMap<String, User> getLoggedInUsers() {
        return loggedInUsers;
    }

    public static HashMap<Socket, String> getLoggedInSockets() {
        return loggedInSockets;
    }

    public void newClient(Socket thisClientsSocket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
        while (true) {
            String clientMessage = dataInputStream.readUTF();
            System.out.println("CLIENT " + new SimpleDateFormat("HH:mm:ss").format(new Date()) + ":" + clientMessage);
            String serverMessage = process(clientMessage, thisClientsSocket);
            System.out.println("SERVER " + new SimpleDateFormat("HH:mm:ss").format(new Date()) + ":" + serverMessage);
            if (serverMessage.equals("")) return;
            dataOutputStream.writeUTF(serverMessage);
            dataOutputStream.flush();
        }
    }

    public String process(String message, Socket thisClientsSocket) {
        System.out.println(loggedInSockets.toString());
        System.out.println(loggedInUsers.toString());
        Matcher matcher;
        String token;
        if ((matcher = Regex.getCommandMatcher(message, Regex.createUser)).matches())
            return createUser(matcher, thisClientsSocket);
        else if ((matcher = Regex.getCommandMatcher(message, Regex.login)).matches())
            return loginUser(matcher, thisClientsSocket);
        if (!tokenIsValid(message)) return "invalid token";
        else if ((matcher = Regex.getCommandMatcher(message, Regex.saveUser)).matches()) return saveUser(matcher);
        else if ((matcher = Regex.getCommandMatcher(message, Regex.sendMessage)).matches()) return sendMessage(matcher);
        else if ((matcher = Regex.getCommandMatcher(message, Regex.requestMessages)).matches()) return getMessages();
        else if ((matcher = Regex.getCommandMatcher(message, Regex.requestPinnedMessage)).matches())
            return getPinnedMessage();
        else if ((matcher = Regex.getCommandMatcher(message, Regex.requestEfficientUsers)).matches())
            return requestEfficientUsers(matcher);
        else if ((matcher = Regex.getCommandMatcher(message, Regex.requestOnlineCount)).matches())
            return requestOnlineCount();
        else if ((matcher = Regex.getCommandMatcher(message, Regex.requestCardStock)).matches())
            return requestCardStock();
        else if ((matcher = Regex.getCommandMatcher(message, Regex.requestUnavailableCards)).matches())
            return requestUnavailableCards();
        else if ((matcher = Regex.getCommandMatcher(message, Regex.getUser)).matches()) return requestUser(matcher);
        else if ((matcher = Regex.getCommandMatcher(message, Regex.pinMessage)).matches()) return pinMessage(matcher);
        else if ((matcher = Regex.getCommandMatcher(message, Regex.editMessage)).matches()) return editMessage(matcher);
        else if ((matcher = Regex.getCommandMatcher(message, Regex.deleteMessage)).matches())
            return deleteMessage(matcher);
        else if ((matcher = Regex.getCommandMatcher(message, Regex.prevAvatar)).matches())
            return changeAvatar(matcher, -1);
        else if ((matcher = Regex.getCommandMatcher(message, Regex.nextAvatar)).matches())
            return changeAvatar(matcher, +1);
        else if ((matcher = Regex.getCommandMatcher(message, Regex.changePassword)).matches())
            return changePassword(matcher);
        else if ((matcher = Regex.getCommandMatcher(message, Regex.changeNickname)).matches())
            return changeNickname(matcher);
        else if ((matcher = Regex.getCommandMatcher(message, Regex.shopBuy)).matches())
            return buyCard(matcher);
        else if ((matcher = Regex.getCommandMatcher(message, Regex.shopSell)).matches())
            return sellCard(matcher);
        else if ((matcher = Regex.getCommandMatcher(message, Regex.shopAdmin)).matches())
            return shopAdmin(matcher);
        else if ((matcher = Regex.getCommandMatcher(message, Regex.shopIncrease)).matches())
            return shopRestock(matcher, +1);
        else if ((matcher = Regex.getCommandMatcher(message, Regex.shopDecrease)).matches())
            return shopRestock(matcher, -1);
        else if ((matcher = Regex.getCommandMatcher(message, Regex.shopToggle)).matches())
            return shopToggle(matcher);
        else if ((matcher = Regex.getCommandMatcher(message, Regex.logout)).matches())
            return logout(matcher);
        return "ERROR unknown command";
    }

    private String saveUser(Matcher matcher) {
        String userJson = matcher.group("userJson");
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        EfficientUser tempUser = gson.fromJson(userJson, EfficientUser.class);
        DatabaseController.getInstance().saveEfficientUser(tempUser);
        return "SUCCESS";
    }

    private String shopToggle(Matcher matcher) {
        if (!matcher.group("token").matches(ShopController.getAdminToken())) return "ERROR invalid token";
        String cardName = matcher.group("cardName");
        try {
            ShopController.toggle(cardName);
            return "SUCCESS";
        } catch (Exception e) {
            return ("ERROR" + e.getMessage());
        }
    }

    private String shopRestock(Matcher matcher, int i) {
        if (!matcher.group("token").matches(ShopController.getAdminToken())) return "ERROR invalid token";
        String cardName = matcher.group("cardName");
        try {
            ShopController.restock(cardName, i);
            return "SUCCESS";
        } catch (Exception e) {
            return ("ERROR" + e.getMessage());
        }
    }

    private String shopAdmin(Matcher matcher) {
        if (matcher.group("keycode").matches("AmirrezaGoBRRRRRR")) {
            ShopController.setAdminToken(matcher.group("token"));
            return "SUCCESS";
        }
        return "ERROR invalid keycode";
    }

    private String requestUnavailableCards() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(ShopController.getUnavailableCards());
    }

    private String requestCardStock() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(ShopController.getCardStock());
    }

    private String buyCard(Matcher matcher) {
        String cardName = matcher.group("cardName");
        User currentUser = ServerController.getInstance().getUserByToken(matcher.group("token"));
        try {
            ShopController.buyCard(cardName, currentUser);
            return "SUCCESS";
        } catch (Exception e) {
            return ("ERROR " + e.getMessage());
        }
    }

    private String sellCard(Matcher matcher) {
        String cardName = matcher.group("cardName");
        User currentUser = ServerController.getInstance().getUserByToken(matcher.group("token"));
        try {
            ShopController.sellCard(cardName, currentUser);
            return "SUCCESS";
        } catch (Exception e) {
            return ("ERROR " + e.getMessage());
        }
    }

    private String logout(Matcher matcher) {
        String token = matcher.group("token");
        loggedInUsers.remove(token);
        return "SUCCESS";
    }

    private String changeNickname(Matcher matcher) {
        User requestedUser = getUserByToken(matcher.group("token"));
        if (requestedUser == null) return "ERROR user not found";
        try {
            return ProfileController.changeNickname(matcher.group("nickname"), requestedUser);
        } catch (RepetitiveNicknameException e) {
            return ("ERROR " + e.getMessage());
        }
    }

    private String changePassword(Matcher matcher) {
        User requestedUser = getUserByToken(matcher.group("token"));
        if (requestedUser == null) return "ERROR user not found";
        try {
            return ProfileController.changePassword(matcher.group("currentPass"), matcher.group("newPass"), requestedUser);
        } catch (RepetitivePasswordException | InvalidPasswordException | WeakPasswordException e) {
            return ("ERROR " + e.getMessage());
        }
    }

    private String changeAvatar(Matcher matcher, int change) {
        User requestedUser = getUserByToken(matcher.group("token"));
        if (requestedUser == null) return "ERROR user not found";
        requestedUser.setAvatarID(String.valueOf(Integer.parseInt(requestedUser.getAvatarID()) + change));
        return "SUCCESS";
    }

    private String getPinnedMessage() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(Message.pinnedMessage);
    }

    private synchronized String deleteMessage(Matcher matcher) {
        int id;
        try {
            id = Integer.parseInt(matcher.group("id"));
        } catch (Exception e) {
            return "FAILED";
        }
        Message toDelete = Message.getMessageById(id);
        User currentUser = getUserByToken(matcher.group("token"));
        if (toDelete == null) return "ERROR invalid id for some reason";
        if (currentUser == null) return "ERROR token invalid for some reason";
        if (!currentUser.getUsername().equals(toDelete.getSenderUserName()))
            return "ERROR You Can't Delete This Message!";
        if (Message.pinnedMessage == toDelete) Message.pinnedMessage = null;
        Message.messageList.remove(toDelete);
        DatabaseController.getInstance().saveChat();
        return "SUCCESS";
    }

    private String editMessage(Matcher matcher) {
        String replace = matcher.group("replace");
        int id;
        try {
            id = Integer.parseInt(matcher.group("id"));
        } catch (Exception e) {
            return "FAILED";
        }
        Message toEdit = Message.getMessageById(id);
        User currentUser = getUserByToken(matcher.group("token"));
        if (toEdit == null) return "ERROR invalid id for some reason";
        if (currentUser == null) return "ERROR token invalid for some reason";
        if (!currentUser.getUsername().equals(toEdit.getSenderUserName())) return "ERROR You Can't Edit This Message!";
        toEdit.setContent(replace);
        DatabaseController.getInstance().saveChat();
        return "SUCCESS";
    }

    private synchronized String pinMessage(Matcher matcher) {
        int id;
        try {
            id = Integer.parseInt(matcher.group("id"));
        } catch (Exception e) {
            return "FAILED";
        }
        Message toPin = Message.getMessageById(id);
        if (toPin == null) return "ERROR invalid id";
        Message.pinnedMessage = toPin;
        return "SUCCESS";
    }

    private String getMessages() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(Message.messageList);
    }

    private String requestOnlineCount() {
        ArrayList<User > countedUsers = new ArrayList<>();
        for (String token : loggedInUsers.keySet()) {
            if (!countedUsers.contains(loggedInUsers.get(token))){
                countedUsers.add(loggedInUsers.get(token));
            }
        }
        return String.valueOf(countedUsers.size());
    }

    private synchronized String sendMessage(Matcher matcher) {
        String message = matcher.group("message");
        User currentUser = getUserByToken(matcher.group("token"));
        if (currentUser == null) return "ERROR";
        new Message(currentUser.getUsername(), message);
        DatabaseController.getInstance().saveChat();
        return "SUCCESS";
    }

    private String requestEfficientUsers(Matcher matcher) {
        Gson gson = new GsonBuilder().create();
        ArrayList<EfficientUser> allEfficientUsers = new ArrayList<>();
        for (User user : User.getUsers()) {
            allEfficientUsers.add(new EfficientUser(user));
        }
        return gson.toJson(allEfficientUsers);
    }

    private boolean tokenIsValid(String message) {
        String[] messageSplit = message.split(" ");
        for (String token : loggedInUsers.keySet()) {
            if (messageSplit[0].matches(token)) return true;
        }
        return false;
    }

    public User getUserByToken(String requestToken) {
        for (String token : loggedInUsers.keySet()) {
            if (requestToken.equals(token)) {
                return loggedInUsers.get(token);
            }
        }
        return null;
    }

    private String requestUser(Matcher matcher) {
        String requestToken = matcher.group("token");
        User user;
        user = getUserByToken(requestToken);
        if (user == null) return "user not found";
        EfficientUser efficientUser = new EfficientUser(user);
        Gson gson = new GsonBuilder().create();
        return gson.toJson(efficientUser);
    }

    private String loginUser(Matcher matcher, Socket socket) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        try {
            String newUserToken = LoginController.loginUser(username, password);
            loggedInUsers.put(newUserToken, User.getUserByName(username));
            loggedInSockets.put(socket, newUserToken);
            return newUserToken;
        } catch (Exception e) {
            return ("ERROR " + e.getMessage());
        }
    }

    private String createUser(Matcher matcher, Socket socket) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        String nickname = matcher.group("nickname");
        try {
            String newUserToken = LoginController.registerUser(username, password, nickname);
            loggedInUsers.put(newUserToken, User.getUserByName(username));
            loggedInSockets.put(socket, newUserToken);
            return newUserToken;
        } catch (Exception e) {
            return ("ERROR " + e.getMessage());
        }
    }

}
