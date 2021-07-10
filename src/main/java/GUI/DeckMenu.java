package GUI;

import Controller.DatabaseController.DatabaseController;
import Controller.MenuController.DeckMenuController;
import Database.Cards.Card;
import Database.Cards.Monster;
import Database.Deck;
import View.Exceptions.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Optional;


public class DeckMenu extends Application implements AlertFunction {
    @FXML
    ScrollPane mainDeck;
    @FXML
    ScrollPane sideDeck;
    @FXML
    ScrollPane inactiveCards;
    @FXML
    ScrollPane decks;

    @FXML
    Button deleteDeckButton;
    @FXML
    Button removeCardButton;
    @FXML
    Button activateDeckButton;
    @FXML
    AnchorPane selectedCardPane;

    private static ImagePattern unknownCard = new ImagePattern(new Image(DeckMenu.class.getResourceAsStream("/Database/Cards/Monsters/Unknown.jpg")));


    private Deck selectedDeck;
    private Deck tempDeck;
    private VBox selectedVBox;
    private Card selectedCard;
    private Boolean cardIsInDeck;
    private Boolean isSide;
    private CardPosition cardPosition;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/deckMenu.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        initializeDragAndDrop();
        updateDecks();
        updateInactiveCards();
    }

    private void initializeDragAndDrop() {
        initializeDeckPanes(mainDeck);
        initializeDeckPanes(sideDeck);
    }

    private void initializeDeckPanes(ScrollPane deckPane) {
        deckPane.setOnDragOver(dragEvent -> {
            if (dragEvent.getGestureSource() != deckPane &&
                    dragEvent.getDragboard().hasString()) {
                if (deckPane.equals(mainDeck))
                    dragEvent.acceptTransferModes(TransferMode.MOVE);
                else if (deckPane.equals(sideDeck))
                    dragEvent.acceptTransferModes(TransferMode.COPY);
            }
            dragEvent.consume();
        });

        deckPane.setOnDragEntered(dragEvent -> {
            isSide = (deckPane.equals(sideDeck));
            if (isSide) dragEvent.acceptTransferModes(TransferMode.COPY);
            else dragEvent.acceptTransferModes(TransferMode.MOVE);
        });

        deckPane.setOnDragExited(dragEvent -> {
            isSide = null;
            dragEvent.acceptTransferModes(TransferMode.NONE);
        });

        deckPane.setOnDragDropped(dragEvent -> {
            Dragboard db = dragEvent.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                selectedCard = Card.getCardByName(db.getString());
                success = true;
            }
            dragEvent.setDropCompleted(success);

            dragEvent.consume();
        });
    }

    public void updateEverything() {
        DatabaseController.getInstance().saveUser(MainMenu.currentUser);
        updateDecks();
        updateMainDeck();
        updateSideDeck();
        updateButtons();
        updateInactiveCards();
        updateSelectedCard();
    }

    public void updateButtons() {
        deleteDeckButton.disableProperty().set(selectedDeck == null);
        removeCardButton.disableProperty().set(selectedCard == null || !cardIsInDeck);
        activateDeckButton.disableProperty().set(selectedDeck == null || selectedDeck.isActive());
    }

    public void updateDecks() {
        GridPane pane = new GridPane();
        pane.setVgap(10);
        pane.setHgap(10);
        pane.setMinWidth((MainMenu.currentUser.getDecks().size() + 1) * 80);
        int i = 0;
        for (Deck deck : MainMenu.currentUser.getDecks()) {
            pane.getChildren().add(getDeckVBox(deck, i++));
        }
        pane.getChildren().add(getCreateDeckButton(i));
        decks.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        decks.setPannable(true);
        pane.getStyleClass().add("lightBackground");
        decks.setContent(pane);
    }

    public VBox getCreateDeckButton(int i) {
        VBox vBox = new VBox();
        StackPane deckView = new StackPane();
        deckView.setMinSize(70, 100);
        Rectangle cardPicture = new Rectangle(70, 100, unknownCard);
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.7);
        cardPicture.setEffect(colorAdjust);
        deckView.getChildren().add(cardPicture);
        deckView.getChildren().add(new Label("+"));
        deckView.getStyleClass().add("createDeckButton");
        Label deckName = new Label("Create Deck");
        deckName.setMaxWidth(70);
        deckView.getStyleClass().add("cardItems");
        vBox.getChildren().addAll(deckView, deckName);
        vBox.setMinSize(80, 130);
        vBox.setTranslateX(i * 80 + 10);
        vBox.setTranslateY(10);
        vBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                createDeck();
            }
        });

        return vBox;
    }

    private void createDeck() {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Create Deck");
        inputDialog.setHeaderText("Create Deck");
        inputDialog.setContentText("Input name of new deck:");
        Optional<String> deckName = inputDialog.showAndWait();
        if (deckName.isPresent()) {
            try {
                if (deckName.get().isEmpty()) throw new Exception("this is not a valid deck name");
                DeckMenuController.getInstance().createDeck(deckName.get(), MainMenu.currentUser);
                selectedDeck = MainMenu.currentUser.getDeckByName(deckName.get());
                updateEverything();
                showAlert("deck created successfully!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert(e.getMessage(), Alert.AlertType.ERROR);
            }
        }

    }

    public void updateInactiveCards() {
        VBox outer = new VBox();
        outer.setSpacing(10);
        outer.alignmentProperty().set(Pos.CENTER);
        for (Card card : MainMenu.currentUser.getInactiveCards()) {
            outer.getChildren().add(getInactiveCardVBox(card));
        }
        outer.getStyleClass().add("lightBackground");
        inactiveCards.setContent(outer);
    }

    public VBox getInactiveCardVBox(Card card) {
        VBox vBox = new VBox();
        Rectangle cardView = new Rectangle(56, 80, card.getFill());
        Label name = new Label(card.getName());
        name.setMaxWidth(70);
        cardView.getStyleClass().add("cardItems");
        vBox.getChildren().addAll(cardView, name);
        vBox.setMinSize(70, 100);
        vBox.setTranslateX(10);
        vBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selectCard(card, false);
                cardPosition = CardPosition.NONE;
            }
        });

        vBox.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Dragboard db = vBox.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(card.getName());
                db.setContent(content);

                selectedCard = card;
                mouseEvent.consume();
            }
        });

        vBox.setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                if (dragEvent.getTransferMode() != null) {
                    if (dragEvent.getTransferMode().equals(TransferMode.MOVE)) {
                        isSide = false;
                        addCard();
                    }
                    if (dragEvent.getTransferMode().equals(TransferMode.COPY)) {
                        isSide = true;
                        addCard();
                    }
                }
                selectedCard = null;
            }
        });
        return vBox;
    }

    public VBox getDeckVBox(Deck deck, int i) {
        VBox vBox = new VBox();
        Rectangle deckView = new Rectangle(70, 100, unknownCard);
        Label deckName = new Label(deck.getName());
        Label deckInfo = new Label(deck.getMainCards().size() + "/" + deck.getSideCards().size());
        deckName.setMaxWidth(70);
        deckInfo.setMaxWidth(70);
        deckView.getStyleClass().add("cardItems");
        vBox.getChildren().addAll(deckView, deckName, deckInfo);
        vBox.setMinSize(80, 130);
        vBox.setTranslateX(i * 80 + 10);
        vBox.setTranslateY(10);
        if (deck.isActive()) {
            deckName.getStyleClass().add("activeDeck");
            deckInfo.getStyleClass().add("activeDeck");
        }
        if (selectedDeck != null && selectedDeck.equals(deck)) vBox.getStyleClass().add("selectedDeck");
        vBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selectDeck(deck);
            }
        });

        vBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                tempDeck = deck;
                updateMainDeck();
                updateSideDeck();
            }
        });

        vBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                tempDeck = null;
                updateMainDeck();
                updateSideDeck();
            }
        });

        return vBox;
    }

    private void selectDeck(Deck deck) {
        selectedDeck = deck;
        updateButtons();
        updateMainDeck();
        updateSideDeck();
        updateDecks();
    }

    public void updateMainDeck() {
        GridPane pane = new GridPane();
        pane.setVgap(10);
        pane.setHgap(10);
        pane.setMaxWidth(580);
        int i = 0;
        Deck curDeck = selectedDeck;
        if (tempDeck != null) curDeck = tempDeck;
        if (curDeck != null) {
            for (Card card : curDeck.getMainCards()) {
                pane.getChildren().add(getCardVBox(card, i++, CardPosition.MAIN));
            }
        }
        pane.setMinHeight(7 * 150);
        pane.getStyleClass().add("lightBackground");
        mainDeck.setContent(pane);
        mainDeck.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        mainDeck.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    public void updateSideDeck() {
        GridPane pane = new GridPane();
        pane.setVgap(10);
        pane.setHgap(10);
        pane.setMaxWidth(580);
        int i = 0;
        Deck curDeck = selectedDeck;
        if (tempDeck != null) curDeck = tempDeck;
        if (curDeck != null) {
            for (Card card : curDeck.getSideCards()) {
                pane.getChildren().add(getCardVBox(card, i++, cardPosition.SIDE));
            }
        }
        pane.setMinHeight(7 * 150);
        pane.getStyleClass().add("lightBackground");
        sideDeck.setContent(pane);
        sideDeck.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sideDeck.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    public VBox getCardVBox(Card card, int i, CardPosition position) {
        VBox vBox = new VBox();
        Rectangle cardView = new Rectangle(70, 100, card.getFill());
        Label name = new Label(card.getName());
        name.setMaxWidth(70);
        cardView.getStyleClass().add("cardItems");
        vBox.getChildren().addAll(cardView, name);
        vBox.setTranslateX((i % 7) * 80 + 10);
        vBox.setTranslateY((i / 7) * 130 + 10);
        vBox.setMinSize(80, 130);

        vBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selectCard(card, true);
                cardPosition = position;
            }
        });

        return vBox;
    }

    private void selectCard(Card card, Boolean cardIsInDeck) {
        selectedCard = card;
        this.cardIsInDeck = cardIsInDeck;
        updateButtons();
        updateSelectedCard();
    }

    private void updateSelectedCard() {
        selectedCardPane.getChildren().clear();
        if (selectedCard == null) return;
        HBox hbox = new HBox();
        Rectangle cardImage = new Rectangle(102, 160, selectedCard.getFill());
        Text text = new Text(selectedCard.getName() + "\n" + selectedCard.getDescription());
        if (selectedCard instanceof Monster)
            text.setText(text.getText() + "\nATK: " + ((Monster) selectedCard).getAttackPoints() + "\tDEF: " + ((Monster) selectedCard).getDefensePoints());
        text.setWrappingWidth(160);
        hbox.getChildren().addAll(cardImage, text);
        selectedCardPane.getChildren().add(hbox);
    }

    public void deleteDeck(MouseEvent mouseEvent) {
        try {
            DeckMenuController.getInstance().deleteDeck(selectedDeck.getName(), MainMenu.currentUser);
            DatabaseController.getInstance().saveUser(MainMenu.currentUser);
            selectedDeck = null;
            selectedCard = null;
            updateEverything();
        } catch (InvalidDeckNameException e) {
            e.printStackTrace();
        }
    }

    public void activateDeck(MouseEvent mouseEvent) {
        try {
            DeckMenuController.getInstance().activateDeck(selectedDeck.getName(), MainMenu.currentUser);
        } catch (InvalidDeckNameException e) {
            e.printStackTrace();
        }
        DatabaseController.getInstance().saveUser(MainMenu.currentUser);
        selectedCard = null;
        updateEverything();
    }

    public void addCard() {

        if (isSide == null) try {
            throw new Exception("please select main deck or side deck to add card to");
        } catch (Exception e) {
            showAlert(e.getMessage(), Alert.AlertType.INFORMATION);
            return;
        }

        try {
            if (selectedDeck == null) throw new Exception("Please select a deck to add card to first");
            DeckMenuController.getInstance().addCard(selectedDeck.getName(), selectedCard.getName(), isSide, MainMenu.currentUser);
            selectedCard = null;
            updateEverything();
        } catch (Exception e) {
            showAlert(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void removeCard(MouseEvent mouseEvent) {
        try {
            if (cardPosition == CardPosition.NONE)
                throw new CardNotInDeckException(selectedCard.getName(), selectedDeck.getName());
            DeckMenuController.getInstance().removeCard(selectedDeck.getName(), selectedCard.getName(), (cardPosition == CardPosition.SIDE), MainMenu.currentUser);
            selectedCard = null;
            updateEverything();
        } catch (DeckIsFullException | InvalidCardNameException | InvalidDeckNameException | CardNotInDeckException e) {
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
}

enum CardPosition {
    SIDE, MAIN, NONE
}
