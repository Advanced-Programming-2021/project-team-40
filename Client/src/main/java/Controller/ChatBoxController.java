package Controller;

import Database.Message;
import GUI.MainMenu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ChatBoxController {
    private static ChatBoxController chatBoxController;

    public static ChatBoxController getInstance() {
        if (chatBoxController == null) chatBoxController = new ChatBoxController();
        return chatBoxController;
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
}
