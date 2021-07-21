package GUI;

import Controller.ChatBoxController;
import Database.Message;
import Database.User;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
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
    public Label onlineCount;
    public Circle greenCircle;

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
        String onlineCount = ChatBoxController.getInstance().requestOnlineCount();
        pinnedMessageLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(5), null)));
        pinnedMessageLabel.setFont(new Font(20));
        greenCircle.setFill(Color.LAWNGREEN);
        chatScrollPane.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY,CornerRadii.EMPTY,null)));
        refreshPinLabel(pinnedMessage);
        refreshChatVBox(messageList);
        refreshOnlineCount(onlineCount);
    }

    private void refreshOnlineCount(String onlineCount) {
        this.onlineCount.setText(onlineCount);
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
                    updateEverything();
                } catch (Exception e) {
                    showAlert(e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
        pin.setOnAction(actionEvent -> {
            try {
                ChatBoxController.getInstance().pinMessage(message);
                updateEverything();
            } catch (Exception e) {
                showAlert(e.getMessage(), Alert.AlertType.ERROR);
            }
        });
        delete.setOnAction(actionEvent -> {
            try {
                ChatBoxController.getInstance().deleteMessage(message);
                updateEverything();
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

    @FXML
    private void updateEverything() {
        updateChatBox();
        updatePinLabel();
        updateOnlineCount();
    }

    private void updateOnlineCount() {
        String onlineCount = ChatBoxController.getInstance().requestOnlineCount();
        refreshOnlineCount(onlineCount);
    }

    public void sendMessage() {
        if (messageToSend.getText().equals("")) return;
        try {
            ChatBoxController.getInstance().sendMessage(messageToSend.getText());
            updateEverything();
            messageToSend.setText("");
        } catch (Exception e) {
            showAlert(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void updateChatBox() {
        List<Message> messageList = ChatBoxController.getInstance().requestMessages();
        chatVBox = new VBox(5);
        refreshChatVBox(messageList);
        chatScrollPane.setContent(chatVBox);
    }

    private void updatePinLabel() {
        Message pinnedMessage = ChatBoxController.getInstance().requestPinnedMessage();
        refreshPinLabel(pinnedMessage);
    }

    private void refreshChatVBox(List<Message> messageList) {
        messageList.sort(Comparator.comparing(Message::getDate));
        User loggedUser = MainMenu.currentUser;
        loggedUser.setRandomColorToShowUser(messageList);
        for (Message message :
                messageList) {
            HBox hBox = getMessageHBox(message);
            chatVBox.getChildren().add(hBox);
        }
    }

    private HBox getMessageHBox(Message message) {
        User loggedUser = MainMenu.currentUser;
        Label label = new Label(message.getContent());
        Label userLabel = new Label(message.getSenderUserName());
        userLabel.setFont(new Font(10));
        userLabel.setAlignment(Pos.CENTER);
        Color color = loggedUser.getRandomColorToShowUser().get(message.getSenderUserName());
        Circle avatar = new Circle(30);
        avatar.setFill(new ImagePattern(ChatBoxController.getInstance().getProfilePicture(message.getSenderUserName())));
        avatar.setOnMouseClicked(mouseEvent -> {
            ContextMenu contextMenu = new ContextMenu();
            String userInfo = ChatBoxController.getInstance().requestUserInfo(message.getSenderUserName());
            showUserInfo(userInfo);
            contextMenu.show(avatar, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        });
        VBox vBox = new VBox(avatar, userLabel);
        vBox.setAlignment(Pos.CENTER);
        label.setFont(new Font(16));
        label.setPadding(new Insets(2, 5, 2, 5));
        label.setMaxWidth(200);
        label.setWrapText(true);
        label.setBackground(new Background(new BackgroundFill(color, new CornerRadii(10), null)));
        label.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() != MouseButton.SECONDARY) return;
            ContextMenu contextMenu = getContextMenu(message);
            contextMenu.show(label, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        });
        HBox hBox = new HBox();
        if (message.getSenderUserName().equals(loggedUser.getUsername())) {
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.getChildren().addAll(vBox, label);
        } else {
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.getChildren().addAll(label, vBox);
        }
        hBox.setPrefWidth(500);
        return hBox;
    }

    @Override
    public void showAlert(String text, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Alert");
        alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        alert.setContentText(text);
        alert.show();
    }

    private void showUserInfo(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User Info");
        alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        alert.setHeaderText(text);
        alert.show();
    }
}
