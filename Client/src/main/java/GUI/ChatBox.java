package GUI;

import Controller.ChatBoxController;
import Database.Message;
import Database.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ChatBox extends Application implements AlertFunction {
    public Label pinnedMessageLabel;
    public ScrollPane chatScrollPane;
    public TextField messageToSend;
    public VBox chatVBox;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/chatBox.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        WelcomeMenu.stage = stage;
    }

    public void initialize() {
        Message pinnedMessage = ChatBoxController.getInstance().requestPinnedMessage();
        List<Message> messageList = ChatBoxController.getInstance().requestMessages();
        User loggedUser = MainMenu.currentUser;
        pinnedMessageLabel.setAlignment(Pos.CENTER);
        pinnedMessageLabel.setPrefWidth(500);
        pinnedMessageLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(5), null)));
        refreshPinLabel(pinnedMessage);
        refreshChatVBox(messageList, loggedUser);
        chatScrollPane.setPrefHeight(600);
        chatScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        chatScrollPane.setPannable(true);
        chatScrollPane.setContent(chatVBox);
    }

    private void refreshPinLabel(Message pinnedMessage) {
        if (pinnedMessage != null) pinnedMessageLabel.setText(pinnedMessage.getContent());
    }

    private ContextMenu getContextMenu(Message message) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem edit = new MenuItem("Edit");
        MenuItem pin = new MenuItem("Pin");
        MenuItem delete = new MenuItem("Delete");
        edit.setOnAction(actionEvent -> {
            TextInputDialog dialog = new TextInputDialog(message.getContent());
            dialog.setTitle("Edit Your Message");
            dialog.setContentText("type your new Message:");
            Optional<String> result = dialog.showAndWait();
            String editedMessage;
            if (result.isPresent()) {
                editedMessage = result.get();
                try {
                    ChatBoxController.getInstance().editMessage(editedMessage, message);
                    updateChatBox();
                } catch (Exception e) {
                    showAlert(e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
        pin.setOnAction(actionEvent -> {
            try {
                ChatBoxController.getInstance().pinMessage(message);
                updatePinLabel();
            } catch (Exception e) {
                showAlert(e.getMessage(), Alert.AlertType.ERROR);
            }
        });
        delete.setOnAction(actionEvent -> {
            try {
                ChatBoxController.getInstance().deleteMessage(message);
                updateChatBox();
            } catch (Exception e) {
                showAlert(e.getMessage(), Alert.AlertType.ERROR);
            }
        });
        contextMenu.getItems().addAll(pin, edit, delete);
        return contextMenu;
    }

    public void back() throws Exception {
        new MainMenu().start(WelcomeMenu.stage);
    }

    public void sendMessage() {
        if (messageToSend.getText().equals("")) return;
        try {
            ChatBoxController.getInstance().sendMessage(messageToSend.getText());
            updateChatBox();
            messageToSend.setText("");
        } catch (Exception e) {
            showAlert(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void updateChatBox() {
        List<Message> messageList = ChatBoxController.getInstance().requestMessages();
        chatVBox = new VBox(5);
        refreshChatVBox(messageList, MainMenu.currentUser);
        chatScrollPane.setContent(chatVBox);
    }

    private void updatePinLabel() {
        Message pinnedMessage = ChatBoxController.getInstance().requestPinnedMessage();
        refreshPinLabel(pinnedMessage);
    }
    private void refreshChatVBox(List<Message> messageList, User loggedUser) {
        messageList.sort(Comparator.comparing(Message::getDate));
        loggedUser.setRandomColorToShowUser(messageList);
        for (Message message :
                messageList) {
            Label label = new Label(message.getContent());
            Label userLabel = new Label(message.getSenderUserName());
            userLabel.setFont(new Font(10));
            userLabel.setAlignment(Pos.CENTER);
            Color color = loggedUser.getRandomColorToShowUser().get(message.getSenderUserName());
            Circle avatar = new Circle(20);
//            avatar.setFill(new ImagePattern(MainMenu.currentUser.getProfilePicture()));
            VBox vBox = new VBox(avatar, userLabel);
            vBox.setAlignment(Pos.CENTER);
            label.setPadding(new Insets(2, 5, 2, 5));
            label.setMaxWidth(150);
            label.setWrapText(true);
            label.setBackground(new Background(new BackgroundFill(color, new CornerRadii(10), null)));
            label.setOnContextMenuRequested(contextMenuEvent -> {
                ContextMenu contextMenu = getContextMenu(message);
                contextMenu.show(label, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
            });
            HBox hBox = new HBox();
            hBox.setPrefWidth(500);
            if (message.getSenderUserName().equals(loggedUser.getUsername())) {
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.getChildren().addAll(vBox, label);
            } else {
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.getChildren().addAll(label, vBox);
            }
            chatVBox.getChildren().add(hBox);
        }
    }

    @Override
    public void showAlert(String text, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Alert");
        alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        alert.setContentText(text);
        alert.show();
    }
}
