package GUI;

import Controller.ClientController;
import Controller.Main;
import Controller.MenuController.ShopController;
import Database.Cards.Card;
import Database.Cards.Monster;
import Database.EfficientUser;
import Database.User;
import View.Exceptions.InvalidCardNameException;
import View.Exceptions.NotEnoughMoneyException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ShopMenu extends Application implements AlertFunction, SoundEffect {

    @FXML
    ScrollPane scrollPane;
    @FXML
    Text cardDescription;
    @FXML
    Rectangle cardLarge;
    @FXML
    Button buyButton;
    @FXML
    Button sellButton;

    private HashMap<String, Integer> cardsHashMap;
    private EfficientUser efficientUser;
    private Card selectedCard;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/shopMenu.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        addCards();
    }

    public void addCards() {
        cardsHashMap = getCardHashMap();
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setMinHeight(1500);
        int i = 0;
        for (Card card : Card.getAllCards()) {
            pane.getChildren().add(getCardVBox(card, i++));
        }
        scrollPane.setContent(pane);
        scrollPane.pannableProperty().set(true);
        scrollPane.vbarPolicyProperty().set(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    private HashMap<String, Integer> getCardHashMap() {
        String userString = ClientController.sendMessage(MainMenu.userToken + " get user");
        Gson gson = new GsonBuilder().create();
        EfficientUser efficientUser = gson.fromJson(userString, EfficientUser.class);
        this.efficientUser = efficientUser;
        return efficientUser.getCardHashMap();
    }

    public VBox getCardVBox(Card card, int i) {
        VBox vBox = new VBox();
        Rectangle cardView = new Rectangle(70, 100, card.getFill());
        Label name = new Label(card.getName());
        name.setMaxWidth(70);
        cardView.getStyleClass().add("cardItems");
        Label count = new Label(cardsHashMap.get(card.getName()).toString() + "\nStock: " + getCardStock().get(card.getName()));
        vBox.getChildren().addAll(cardView, name, count);
        vBox.setTranslateX((i % 8) * 80 + 20);
        vBox.setTranslateY((i / 8) * 180 + 20);
        vBox.setMinSize(80, 150);
        vBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selectCard(card);
            }
        });
        return vBox;
    }

    private HashMap<String, Integer> getCardStock() {
        String serverMessage = ClientController.sendMessage(MainMenu.userToken + " request card stock");
        Type cardStockMap = new TypeToken<HashMap<String, Integer>>() {
        }.getType();
        Gson gson = new Gson();
        HashMap<String, Integer> actualMap = gson.fromJson(serverMessage, cardStockMap);
        return actualMap;
    }

    private void selectCard(Card card) {
        selectedCard = card;
        updateCardDetails();
    }

    private void updateCardDetails() {
        cardLarge.setFill(selectedCard.getFill());
        cardDescription.setText("PRICE: " + selectedCard.getCardPrice() + "\n" + selectedCard.getDescription());
        if (selectedCard instanceof Monster)
            cardDescription.setText(cardDescription.getText() + "\nATK: " + ((Monster) selectedCard).getAttackPoints() + "\tDEF: " + ((Monster) selectedCard).getDefensePoints());
        if (efficientUser.getBalance() > selectedCard.getCardPrice()) {
            buyButton.disableProperty().set(false);
        } else {
            buyButton.disableProperty().set(true);
        }
        if (efficientUser.ownsCard(selectedCard.getName())) {
            sellButton.disableProperty().set(false);
        } else {
            sellButton.disableProperty().set(true);
        }
    }

    public void buy(MouseEvent mouseEvent) {
        try {
            String serverMessage = ClientController.sendMessage(MainMenu.userToken + " shop buy " + selectedCard.getName());
            if (serverMessage.startsWith("ERROR")) throw new Exception(serverMessage.substring(6));
            else if (serverMessage.startsWith("SUCCESS")) {

                addCards();
                updateCardDetails();
                playSoundEffect("buySoundEffect.mp3");
                showAlert("card bought successfully", Alert.AlertType.INFORMATION);
            } else throw new Exception("UNKNOWN SHOP ERROR");
        } catch (Exception e) {
            showAlert(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void sell(MouseEvent mouseEvent) {
        try {
            String serverMessage = ClientController.sendMessage(MainMenu.userToken + " shop sell " + selectedCard.getName());
            if (serverMessage.startsWith("ERROR")) throw new Exception(serverMessage.substring(6));
            else if (serverMessage.startsWith("SUCCESS")) {

                addCards();
                updateCardDetails();
                playSoundEffect("buySoundEffect.mp3");
                showAlert("card sold successfully", Alert.AlertType.INFORMATION);
            } else throw new Exception("UNKNOWN SHOP ERROR");
        } catch (Exception e) {
            showAlert(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void admin(MouseEvent mouseEvent) {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Shop Admin");
        inputDialog.setHeaderText("Shop Admin");
        inputDialog.setContentText("Input administrative keycode:");
        Optional<String> adminKey = inputDialog.showAndWait();
        if (adminKey.isPresent()) {
            try {
                String serverMessage = ClientController.sendMessage(MainMenu.userToken + " shop admin " + adminKey.get());
                if (serverMessage.startsWith("ERROR")) throw new Exception(serverMessage.substring(6));
                new ShopAdminMenu().start(WelcomeMenu.stage);
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
        alert.setTitle("Alert");
        alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        alert.setContentText(text);
        alert.show();
    }

    @Override
    public void playSoundEffect(String effectName) {
        String mediaAddress = WelcomeMenu.class.getResource("/Audio/" + effectName).toExternalForm();
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(mediaAddress));
        mediaPlayer.play();
        soundEffects.add(mediaPlayer);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                soundEffects.remove(mediaPlayer);
            }
        });
    }
}
