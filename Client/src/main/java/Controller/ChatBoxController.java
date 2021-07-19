package Controller;

import Database.Message;
import Database.User;
import GUI.MainMenu;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChatBoxController {
    private static ChatBoxController chatBoxController;

    public static ChatBoxController getInstance() {
        if (chatBoxController == null) chatBoxController = new ChatBoxController();
        return chatBoxController;
    }

    public void pinMessage(Message toPin) {
        String serverMessage = ClientController.sendMessage(MainMenu.userToken + " pin message -id " + toPin.getId());
    }

    public void sendMessage(String message) {
        String serverMessage = ClientController.sendMessage(MainMenu.userToken + " send message -m " + message);

    }
    public void editMessage(String toReplace,Message message) throws Exception {
        String serverMessage = ClientController.sendMessage(MainMenu.userToken + " edit message -r " + toReplace + " -id " + message.getId());
    }

    public void deleteMessage(Message toDelete) throws Exception {
        String serverMessage = ClientController.sendMessage(MainMenu.userToken + " delete message -id " + toDelete.getId());
    }

    public List<Message> requestMessages() {
        String serverMessage = ClientController.sendMessage(MainMenu.userToken + " request messages");
        Type messageList = new TypeToken<List<Message>>() {}.getType();
        Gson gson = new Gson();
        return gson.fromJson(serverMessage,messageList);
    }

    public Message requestPinnedMessage() {
        String serverMessage = ClientController.sendMessage(MainMenu.userToken + " request pinned message");
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(serverMessage, Message.class);
    }
}
