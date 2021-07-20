package GUI;

import Controller.ClientController;
import Database.Cards.Card;
import Database.Cards.Monster;
import Database.EfficientUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class ShopAdminMenu extends Application implements AlertFunction {

    @FXML
    ScrollPane scrollPane;
    @FXML
    Text cardDescription;
    @FXML
    Rectangle cardLarge;
    @FXML
    Button increase;
    @FXML
    Button decrease;
    @FXML
    Button toggleLock;

    private HashMap<String, Integer> cardsHashMap;
    private HashMap<String, Integer> cardStock;
    private EfficientUser efficientUser;
    private Card selectedCard;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/shopAdminMenu.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        addCards();
    }

    public void addCards() {
        cardsHashMap = getCardHashMap();
        updateCardStock();
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
        String cardAvailability;
        if (getUnavailableCards().contains(card.getName())) {
            cardAvailability = "UNAVAILABLE";
        } else cardAvailability = "AVAILABLE";
        Label count = new Label(cardAvailability + "\nStock: " + cardStock.get(card.getName()));
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

    private void updateCardStock() {
        String serverMessage = ClientController.sendMessage(MainMenu.userToken + " request card stock");
        Type cardStockMap = new TypeToken<HashMap<String, Integer>>() {
        }.getType();
        Gson gson = new Gson();
        cardStock = gson.fromJson(serverMessage, cardStockMap);
    }

    private ArrayList<String> getUnavailableCards() {
        String serverMessage = ClientController.sendMessage(MainMenu.userToken + " request unavailable cards");
        Type cardArrayList = new TypeToken<ArrayList<String>>() {
        }.getType();
        Gson gson = new Gson();
        ArrayList<String> actualList = gson.fromJson(serverMessage, cardArrayList);
        return actualList;
    }

    private void selectCard(Card card) {
        selectedCard = card;
        updateCardDetails();
    }

    private void updateCardDetails() {
        updateCardStock();
        cardLarge.setFill(selectedCard.getFill());
        cardDescription.setText("PRICE: " + selectedCard.getCardPrice() + "\n" + selectedCard.getDescription());
        if (selectedCard instanceof Monster)
            cardDescription.setText(cardDescription.getText() + "\nATK: " + ((Monster) selectedCard).getAttackPoints() + "\tDEF: " + ((Monster) selectedCard).getDefensePoints());
        increase.disableProperty().set(false);
        decrease.disableProperty().set(false);
        toggleLock.disableProperty().set(false);
        if (getUnavailableCards().contains(selectedCard.getName())) {
            toggleLock.setText("Unlock");
        } else {
            toggleLock.setText("Lock");
        }
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        new ShopMenu().start(WelcomeMenu.stage);
    }

    @Override
    public void showAlert(String text, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Alert");
        alert.getDialogPane().setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        alert.setContentText(text);
        alert.show();
    }

    public void increase(MouseEvent mouseEvent) {
        ClientController.sendMessage(MainMenu.userToken + " shop increase " + selectedCard.getName());
        addCards();
        updateCardDetails();
    }

    public void decrease(MouseEvent mouseEvent) {
        ClientController.sendMessage(MainMenu.userToken + " shop decrease " + selectedCard.getName());
        addCards();
        updateCardDetails();
    }

    public void toggleLock(MouseEvent mouseEvent) {
        ClientController.sendMessage(MainMenu.userToken + " shop toggle " + selectedCard.getName());
        addCards();
        updateCardDetails();
    }
}
