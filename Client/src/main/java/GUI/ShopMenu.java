package GUI;

import Controller.Main;
import Controller.MenuController.ShopController;
import Database.Cards.Card;
import Database.Cards.Monster;
import Database.User;
import View.Exceptions.InvalidCardNameException;
import View.Exceptions.NotEnoughMoneyException;
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

import java.util.ArrayList;
import java.util.HashMap;

public class ShopMenu extends Application implements AlertFunction, SoundEffect{

    @FXML
    ScrollPane scrollPane;
    @FXML
    Text cardDescription;
    @FXML
    Rectangle cardLarge;
    @FXML
    Button buyButton;

    private HashMap<String, Integer> cardsHashMap;
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
        cardsHashMap = MainMenu.currentUser.getCardHashMap();
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setMinHeight(1200);
        int i = 0;
        for (Card card : Card.getAllCards()) {
            pane.getChildren().add(getCardVBox(card, i++));
        }
        scrollPane.setContent(pane);
        scrollPane.pannableProperty().set(true);
        scrollPane.vbarPolicyProperty().set(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    public VBox getCardVBox(Card card, int i) {
        VBox vBox = new VBox();
        Rectangle cardView = new Rectangle(70, 100, card.getFill());
        Label name = new Label(card.getName());
        name.setMaxWidth(70);
        cardView.getStyleClass().add("cardItems");
        Label count = new Label(cardsHashMap.get(card.getName()).toString());
        vBox.getChildren().addAll(cardView, name, count);
        vBox.setTranslateX((i % 8) * 80 + 20);
        vBox.setTranslateY((i / 8) * 150 + 20);
        vBox.setMinSize(80, 150);
        vBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selectCard(card);
            }
        });
        return vBox;
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
        if (MainMenu.currentUser.getBalance() > selectedCard.getCardPrice()) {
            buyButton.disableProperty().set(false);
        } else {
            buyButton.disableProperty().set(true);
        }
    }

    public void buy(MouseEvent mouseEvent) {
        try {
            ShopController.getInstance().buy(selectedCard.getName(), MainMenu.currentUser);
            addCards();
            updateCardDetails();
            playSoundEffect("buySoundEffect.mp3");
            showAlert("card bought successfully", Alert.AlertType.INFORMATION);
        } catch (InvalidCardNameException | NotEnoughMoneyException e) {
            showAlert(e.getMessage(), Alert.AlertType.ERROR);
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
