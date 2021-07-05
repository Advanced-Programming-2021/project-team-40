package GUI;

import Controller.DatabaseController.DatabaseController;
import Controller.MenuController.DeckMenuController;
import Controller.MenuController.ProfileMenuController;
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
import javafx.stage.Stage;

import java.util.Optional;

public class ProfileMenu extends Application implements AlertFunction {

    @FXML
    Label username;
    @FXML
    Label nickname;
    @FXML
    Label score;

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
        username.setText(MainMenu.currentUser.getUsername());
        nickname.setText(MainMenu.currentUser.getNickname());
        score.setText(Integer.toString(MainMenu.currentUser.getScore()));
        DatabaseController.getInstance().saveUser(MainMenu.currentUser);
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
                ProfileMenuController.getInstance().changeNickname(newNickName.get(), MainMenu.currentUser);
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
                ProfileMenuController.getInstance().changePassword(oldPassword.get(), newPassword.get(), MainMenu.currentUser);
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
}

class PasswordDialog extends TextInputDialog {
    public PasswordDialog() {
        DialogPane dialogPane = new DialogPane();
        PasswordField passwordField = new PasswordField();
        HBox hBox = new HBox();
        hBox.getChildren().add(passwordField);
        hBox.setPadding(new Insets(20));
        getDialogPane().setContent(hBox);
    }
}
