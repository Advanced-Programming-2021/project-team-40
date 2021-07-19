package Controller;

import Database.Message;
import Database.User;
import GUI.MainMenu;
import com.google.gson.Gson;
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
        Message.pinnedMessage = toPin;
    }

    public void sendMessage(String message,User sender) {
        String serverMessage = ClientController.sendMessage("send message -m " + message + " -u " + sender.getUsername());
    }
    public void editMessage(String toReplace,Message message) throws Exception {
        String serverMessage = ClientController.sendMessage("edit message -r " + toReplace + " -id " + message.getId());
    }

    public void deleteMessage(User deleteRequester,Message toDelete) throws Exception {
        String serverMessage = ClientController.sendMessage("delete message -id " + toDelete.getId());
        if (!toDelete.getSenderUserName().equals(deleteRequester)) throw new Exception("You Can't Delete This Message");
        Message.messageList.remove(toDelete);
    }

    public List<Message> requestMessages() {
        String serverMessage = ClientController.sendMessage(MainMenu.userToken + " request messages");
        Type messageList = new TypeToken<List<Message>>() {}.getType();
        Gson gson = new Gson();
        List<Message> output = gson.fromJson(serverMessage,messageList);
        System.out.println(output);
        return output;
    }
}
