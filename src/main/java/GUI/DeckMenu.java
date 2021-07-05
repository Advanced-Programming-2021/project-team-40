package GUI;

import Controller.DatabaseController.DatabaseController;
import Controller.MenuController.DeckMenuController;
import Database.Cards.Card;
import Database.Deck;
import View.Exceptions.CardNotInDeckException;
import View.Exceptions.DeckIsFullException;
import View.Exceptions.InvalidCardNameException;
import View.Exceptions.InvalidDeckNameException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class DeckMenu extends Application {
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
    Button addCardButton;
    @FXML
    Button removeCardButton;
    @FXML
    ToggleButton mainSwitch;
    @FXML
    ToggleButton sideSwitch;
    @FXML
    Button activateDeckButton;

    private static ImagePattern unknownCard = new ImagePattern(new Image(DeckMenu.class.getResourceAsStream("/Database/Cards/Monsters/Unknown.jpg")));
    private static ToggleGroup toggleGroup = new ToggleGroup();


    private Deck selectedDeck;
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
        mainSwitch.setToggleGroup(toggleGroup);
        sideSwitch.setToggleGroup(toggleGroup);
        updateDecks();
        updateInactiveCards();
    }

    public void updateEverything() {
        DatabaseController.getInstance().saveUser(MainMenu.currentUser);
        updateDecks();
        updateMainDeck();
        updateSideDeck();
        updateButtons();
        updateInactiveCards();
    }

    public void updateButtons() {
        deleteDeckButton.disableProperty().set(selectedDeck == null);
        addCardButton.disableProperty().set(selectedCard == null || selectedDeck == null || cardIsInDeck);
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
        decks.setContent(pane);
    }

    public VBox getCreateDeckButton(int i) {
        VBox vBox = new VBox();
        StackPane deckView = new StackPane();
        deckView.setMinSize(70, 100);
        Rectangle cardPicture = new Rectangle(70, 100, unknownCard);
        cardPicture.setEffect(new Glow(2));
        deckView.getChildren().add(new Label("+"));
        deckView.getChildren().add(cardPicture);
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

    //TODO complete this!!!
    private void createDeck() {
    }

    public void updateInactiveCards() {
        VBox outer = new VBox();
        outer.setSpacing(10);
        outer.alignmentProperty().set(Pos.CENTER);
        for (Card card : MainMenu.currentUser.getInactiveCards()) {
            outer.getChildren().add(getInactiveCardVBox(card));
        }
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
        if (deck.isActive()) vBox.getStyleClass().add("activeDeck");
        vBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                selectDeck(deck);
            }
        });

        return vBox;
    }

    private void selectDeck(Deck deck) {
        selectedDeck = deck;
        updateButtons();
        updateMainDeck();
        updateSideDeck();
    }

    public void updateMainDeck() {
        GridPane pane = new GridPane();
        pane.setVgap(10);
        pane.setHgap(10);
        pane.setMaxWidth(580);
        int i = 0;
        if (selectedDeck != null)
            for (Card card : selectedDeck.getMainCards()) {
                pane.getChildren().add(getCardVBox(card, i++, CardPosition.MAIN));
            }
        //TODO fix minHeight
        pane.setMinHeight(7 * 150);
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
        if (selectedDeck != null)
            for (Card card : selectedDeck.getSideCards()) {
                pane.getChildren().add(getCardVBox(card, i++, cardPosition.SIDE));
            }
        //TODO fix minHeight
        pane.setMinHeight(7 * 150);
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
        //TODO card details
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
        selectedDeck = null;
        selectedCard = null;
        updateEverything();
    }

    public void addCard(MouseEvent mouseEvent) {
        if (toggleGroup.getSelectedToggle() == null) try {
            throw new Exception("please select main deck or side deck to add card to");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        if (toggleGroup.getSelectedToggle().toString().endsWith("'Main'")) isSide = false;
        else isSide = true;
        try {
            DeckMenuController.getInstance().addCard(selectedDeck.getName(), selectedCard.getName(), isSide, MainMenu.currentUser);
            selectedCard = null;
            updateEverything();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeCard(MouseEvent mouseEvent) {
        try {
            if (cardPosition == CardPosition.NONE) throw new CardNotInDeckException(selectedCard.getName(), selectedDeck.getName());
            DeckMenuController.getInstance().removeCard(selectedDeck.getName(), selectedCard.getName(), (cardPosition == CardPosition.SIDE), MainMenu.currentUser);
            updateEverything();
        } catch (DeckIsFullException | InvalidCardNameException | InvalidDeckNameException | CardNotInDeckException e) {
            System.out.println(e.getMessage());
        }
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        new MainMenu().start(WelcomeMenu.stage);
    }
}

enum CardPosition {
    SIDE,MAIN,NONE
}
