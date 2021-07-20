package Database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Message {
    public static Message pinnedMessage;
    public static List<Message> messageList = new ArrayList<>();
    private int id;
    private String content;
    private String senderUserName;
    private Date date;
    public Message(String sender, String message) {
        this.senderUserName = sender;
        this.content = message;
        id = getGreatestId();
        date = new Date(System.currentTimeMillis());
        messageList.add(this);
    }

    private static int getGreatestId() {
        int i = -1;
        for (Message message :
                messageList) {
            if (message.getId() > i) i = message.getId();
        }
        i++;
        return i;
    }

    public static Message getMessageById(int id) {
        for (Message message :
                messageList) {
            if (message.getId() == id) return message;
        }
        return null;
    }

    public String getSenderUserName() {
        return senderUserName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public int getId() {
        return id;
    }
}
