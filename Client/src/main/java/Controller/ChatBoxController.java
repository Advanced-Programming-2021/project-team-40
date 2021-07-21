package Controller;

import Database.EfficientUser;
import Database.Message;
import GUI.MainMenu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.scene.image.Image;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChatBoxController {
    private static ChatBoxController chatBoxController;

    public static ChatBoxController getInstance() {
        if (chatBoxController == null) chatBoxController = new ChatBoxController();
        return chatBoxController;
    }

    public ArrayList<EfficientUser> loadUsers() {
        String serverMessage = ClientController.sendMessage(MainMenu.userToken + " request efficient users");
        Type efficientUserList = new TypeToken<ArrayList<EfficientUser>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(serverMessage, efficientUserList);
    }

    public void pinMessage(Message toPin) throws Exception {
        String serverMessage = ClientController.sendMessage(MainMenu.userToken + " pin message -id " + toPin.getId());
        if (serverMessage.startsWith("FAILED")) throw new Exception("WTF just happened?");
        if (serverMessage.startsWith("ERROR")) throw new Exception(serverMessage.substring(6));
    }

    public void sendMessage(String message) throws Exception {
        String serverMessage = ClientController.sendMessage(MainMenu.userToken + " send message -m " + message);
        if (serverMessage.startsWith("FAILED")) throw new Exception("WTF just happened?");
        if (serverMessage.startsWith("ERROR")) throw new Exception(serverMessage.substring(6));
    }

    public void editMessage(String toReplace, Message message) throws Exception {
        String serverMessage = ClientController.sendMessage(MainMenu.userToken + " edit message -r " + toReplace + " -id " + message.getId());
        if (serverMessage.startsWith("FAILED")) throw new Exception("WTF just happened?");
        if (serverMessage.startsWith("ERROR")) throw new Exception(serverMessage.substring(6));
    }

    public void deleteMessage(Message toDelete) throws Exception {
        String serverMessage = ClientController.sendMessage(MainMenu.userToken + " delete message -id " + toDelete.getId());
        if (serverMessage.startsWith("FAILED")) throw new Exception("WTF just happened?");
        if (serverMessage.startsWith("ERROR")) throw new Exception(serverMessage.substring(6));
    }

    public List<Message> requestMessages() {
        String serverMessage = ClientController.sendMessage(MainMenu.userToken + " request messages");
        Type messageList = new TypeToken<List<Message>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(serverMessage, messageList);
    }

    public Message requestPinnedMessage() {
        String serverMessage = ClientController.sendMessage(MainMenu.userToken + " request pinned message");
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(serverMessage, Message.class);
    }

    public String requestUserInfo(String username) {
        List<EfficientUser> users = loadUsers();
        EfficientUser requestedUser = null;
        for (EfficientUser user :
                users) {
            if (user.getUsername().equals(username)) {
                requestedUser = user;
                break;
            }
        }
        if (requestedUser == null) return "ERROR";
        return "Username: " + requestedUser.getUsername() + "\nNickname: " + requestedUser.getNickname() + "\n" + "Score: " + requestedUser.getScore();
    }

    public String requestOnlineCount() {
        return ClientController.sendMessage(MainMenu.userToken + " request online count");
    }

    public Image getProfilePicture(String username) {
        List<EfficientUser> users = loadUsers();
        EfficientUser requestedUser = null;
        for (EfficientUser user :
                users) {
            if (user.getUsername().equals(username)) {
                requestedUser = user;
                break;
            }
        }
        if (requestedUser == null) return null;
        return new Image(getClass().getResource("/Avatars/Chara001.dds" + requestedUser.getAvatarID() + ".png").toExternalForm());
    }
}
