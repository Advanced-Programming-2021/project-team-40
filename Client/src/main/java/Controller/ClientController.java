package Controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientController {

    private static String token;
    private static Socket socket;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;

    public static void initializeNetwork() {
        try {
            socket = new Socket("localhost", 7776);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String sendMessage(String clientMessage){
        try {
            dataOutputStream.writeUTF(clientMessage);
            System.out.println("SENDING MESSAGE");
            dataOutputStream.flush();
            String response = dataInputStream.readUTF();
            System.out.println("RESPONSE: " + response);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
