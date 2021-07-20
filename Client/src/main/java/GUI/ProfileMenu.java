package GUI;

import Controller.ClientController;
import Controller.DatabaseController.DatabaseController;
import Controller.MenuController.DeckMenuController;
import Controller.MenuController.ProfileMenuController;
import Database.EfficientUser;
import Database.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Optional;

public class ProfileMenu extends Application implements AlertFunction {

    public User currentUser;

    @FXML
    Label username;
    @FXML
    Label nickname;
    @FXML
    Label score;
    @FXML
    Rectangle avatar;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/profileMenu.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        updateUser();
    }

    private void updateUser() {
        User.clearUsers();
        String userString = ClientController.sendMessage(MainMenu.userToken + " get user");
        Gson gson = new GsonBuilder().create();
        EfficientUser efficientUser = gson.fromJson(userString, EfficientUser.class);
        DatabaseController.getInstance().createUserFromEffUser(efficientUser);
        User user = User.getUserByName(efficientUser.getUsername());
        this.currentUser = user;
        avatar.setFill(new ImagePattern(user.getProfilePicture()));
        username.setText(user.getUsername());
        nickname.setText(user.getNickname());
        score.setText(Integer.toString(user.getScore()));
        DatabaseController.getInstance().saveUser(currentUser);
    }

    public void changeNickname(MouseEvent mouseEvent) {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Change Nickname");
        inputDialog.setHeaderText("Change Nickname");
        inputDialog.setContentText("Input your new nickname:");
        Optional<String> newNickName = inputDialog.showAndWait();
        if (newNickName.isPresent()) {
            try {
                if (newNickName.get().isEmpty()) throw new Exception("this is not a valid nickname");
                String serverMessage = ClientController.sendMessage(MainMenu.userToken + " profile change -n " + newNickName.get());
                if (serverMessage.startsWith("ERROR")) throw new Exception(serverMessage.substring(6));
                updateUser();
                showAlert("nickname changed successfully!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert(e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    public void changePassword(MouseEvent mouseEvent) {

        Dialog<String> passwordDialog = new Dialog<>();
        passwordDialog.setTitle("Change Password");
        passwordDialog.setHeaderText("Old Password");
        passwordDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        PasswordField passwordField = new PasswordField();
        Label passwordLabel = new Label("Enter your old password:");
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(passwordLabel, passwordField);
        passwordDialog.getDialogPane().setContent(content);
        passwordDialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return passwordField.getText();
            }
            return null;
        });

        Optional<String> oldPassword = passwordDialog.showAndWait();
        if (!oldPassword.isPresent()) return;
        passwordDialog.setHeaderText("New Password");
        passwordLabel.setText("Enter your new password:");
        passwordField.setText("");
        Optional<String> newPassword = passwordDialog.showAndWait();
        if (newPassword.isPresent()) {
            try {
                String serverMessage = ClientController.sendMessage(MainMenu.userToken + " profile change -p -c " + oldPassword.get() + " -n " + newPassword.get());
                if (serverMessage.startsWith("ERROR")) throw new Exception(serverMessage.substring(6));
                updateUser();
                showAlert("password changed successfully!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert(e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        new MainMenu().start(WelcomeMenu.stage);
    }

    @Override
    public void showAlert(String text, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        alert.setContentText(text);
        alert.show();
    }

    public void previousAvatar(MouseEvent mouseEvent) {
        String serverMessage = ClientController.sendMessage(MainMenu.userToken + " prev avatar");
        updateUser();
    }

    public void nextAvatar(MouseEvent mouseEvent) {
        String serverMessage = ClientController.sendMessage(MainMenu.userToken + " next avatar");
        updateUser();
    }
}
