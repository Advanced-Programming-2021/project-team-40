import Controller.DatabaseController.DatabaseController;
import Controller.ServerController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        DatabaseController.getInstance();
        initializeNetwork();
    }

    private static void initializeNetwork() {
        try {
            ServerSocket serverSocket = new ServerSocket(7776);
            while (true) {
                Socket socket = serverSocket.accept();
                assignClientToThread(serverSocket, socket);
            }
        } catch (Exception ignored) {

        }
    }

    private static void assignClientToThread(ServerSocket serverSocket, Socket socket) {
        new Thread(() -> {
            DataInputStream dataInputStream = null;
            try {

                dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                ServerController.getInstance().newClient(socket, dataInputStream, dataOutputStream);

            } catch (IOException e) {
                try {
                    assert dataInputStream != null;
                    dataInputStream.close();
                    ServerController.getLoggedInUsers().remove(ServerController.getLoggedInSockets().get(socket));
                    ServerController.getLoggedInSockets().remove(socket);
                    socket.close();
                    System.out.println("a client disconnected!");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }).start();
    }
}


