package Database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Message {
    //TODO load from Json in server and save on Exit
    public static Message pinnedMessage;
    public static List<Message> messageList = new ArrayList<>();//TODO in server
    private static int idCounter = 0;
    private int id;
    private String content;
    private String senderUserName;
    private Date date;

    public Message(String sender,String message) {
        this.senderUserName = sender;
        this.content = message;
        id = idCounter;
        idCounter++;
        date = new Date(System.currentTimeMillis());
        messageList.add(this);
    }

    public static Message getMessageById(int id) {
        for (Message message :
                messageList) {
            if (message.getId() == id) return message;
        }
        return null;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderUserName() {
        return senderUserName;
    }


    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public int getId() {
        return id;
    }
}
