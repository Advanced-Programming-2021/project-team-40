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
