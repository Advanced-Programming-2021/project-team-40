package GUI;

import Controller.DatabaseController.DatabaseController;
import Controller.DuelController.GameplayController;
import Controller.ProgramController.Regex;
import Database.Cards.Card;
import Database.Cards.Monster;
import Database.Cards.SpellAndTrap;
import Database.User;
import Gameplay.*;
import View.Exceptions.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;

public class GameplayView extends Application implements SoundEffect {
    final private static int GAMEPLAY_WIDTH = 700;
    final private static int GAMEPLAY_HEIGHT = 600;
    private static AnchorPane pane = new AnchorPane();
    private static MenuItem effectItem = new MenuItem("Activate effect");
    private static MenuItem summonItem = new MenuItem("Summon");
    private static MenuItem setItem = new MenuItem("Set");
    private static MenuItem directAttackItem = new MenuItem("Direct attack");
    private static MenuItem attackItem = new MenuItem("Attack");
    private static MenuItem flipItem = new MenuItem("Flip summon");
    private static MenuItem changePositionItem = new MenuItem("Change position");
    private static Button nextPhaseButton = new Button("Next phase");
    private static Button settingsButton = new Button("Settings");
    private static Alert addedCardsAlert = new Alert(Alert.AlertType.INFORMATION);
    private static Alert newPhaseAlert = new Alert(Alert.AlertType.INFORMATION);
    public static ArrayList<MenuItem> monsterItems = new ArrayList<>();
    public static ArrayList<MenuItem> spellItems = new ArrayList<>();
    public static ArrayList<MenuItem> handItems = new ArrayList<>();
    public static VBox cardDisplay = new VBox(10);
    public static VBox lowerInfo = new VBox();
    public static VBox upperInfo = new VBox();
    private static Dialog<ButtonType> settingsDialog = new Dialog<>();
    private static GameplayView gameplayView;


    private static Stage thisStage;

    public static GameplayView getInstance() {
        if (gameplayView == null) gameplayView = new GameplayView();
        return gameplayView;
    }

    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid action");
        alert.setHeaderText(message);
        alert.show();
    }

    public static void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(message);
        alert.show();
    }

    public static void checkItems() {
        Phase currentPhase = GameplayController.getInstance().gameplay.getCurrentPhase();
        FieldArea selectedField = GameplayController.getInstance().gameplay.getSelectedField();
        summonItem.setDisable(true);
        setItem.setDisable(true);
        flipItem.setDisable(true);
        effectItem.setDisable(true);
        directAttackItem.setDisable(true);
        directAttackItem.setDisable(true);
        attackItem.setDisable(true);
        changePositionItem.setDisable(true);
        switch (currentPhase) {
            case BATTLE_PHASE:
                if (GameplayController.getGameState() == GameState.CHAIN_MODE)
                    effectItem.setDisable(false);
                if (selectedField instanceof MonsterFieldArea &&
                        !((MonsterFieldArea) selectedField).hasAttacked() &&
                        ((MonsterFieldArea) selectedField).isAttack())
                    attackItem.setDisable(false);
                if (GameplayController.getInstance().isOpponentFieldEmpty())
                    directAttackItem.setDisable(false);
                break;
            case STANDBY_PHASE:
                if (GameplayController.getGameState() == GameState.CHAIN_MODE)
                    effectItem.setDisable(false);
                break;
            case MAIN_PHASE_ONE:
            case MAIN_PHASE_TW0:
                if (selectedField.getCard() instanceof SpellAndTrap) {
                    if (selectedField instanceof SpellAndTrapFieldArea && !selectedField.visibility())
                        effectItem.setDisable(false);
                    else if (selectedField instanceof HandFieldArea) effectItem.setDisable(false);
                    if (selectedField.canBePutOnBoard()) setItem.setDisable(false);
                }
                if ((selectedField.getCard() instanceof Monster) &&
                        !GameplayController.getInstance().gameplay.hasPlacedMonster()) {
                    summonItem.setDisable(false);
                    setItem.setDisable(false);
                }
                if (selectedField instanceof MonsterFieldArea &&
                        !((MonsterFieldArea) selectedField).hasSwitchedMode()) {
                    changePositionItem.setDisable(false);
                    flipItem.setDisable(false);
                }
                break;
        }
    }

    private static void createCardDisplayPanel() {
        Rectangle cardView = new Rectangle(175, 250);
        cardView.setFill(new ImagePattern(Card.UNKNOWN_CARD));
        Label description = new Label();
        description.setWrapText(true);
        cardDisplay.setPrefSize(175, GAMEPLAY_HEIGHT);
        cardDisplay.setAlignment(Pos.CENTER_RIGHT);
        cardDisplay.getChildren().add(cardView);
        cardDisplay.getChildren().add(description);
        cardDisplay.setPrefSize(200, 300);
    }

    private static void createLPs() {
        lowerInfo.setPrefSize(200, 50);
        upperInfo.setPrefSize(200, 50);
        String lowerNickname = GameplayController.getInstance().getGameplay().getCurrentPlayer().getUser().getNickname();
        String upperNickname = GameplayController.getInstance().getGameplay().getOpponentPlayer().getUser().getNickname();
        lowerInfo.getChildren().add(new Label(lowerNickname));
        upperInfo.getChildren().add(new Label(upperNickname));
        Player lowerPlayer, upperPlayer;
        if (GameplayController.getInstance().getGameplay().getPlayerOne().getUser().getNickname().equals(lowerNickname)) {
            lowerPlayer = GameplayController.getInstance().getGameplay().getPlayerOne();
            upperPlayer = GameplayController.getInstance().getGameplay().getPlayerTwo();
        } else {
            lowerPlayer = GameplayController.getInstance().getGameplay().getPlayerTwo();
            upperPlayer = GameplayController.getInstance().getGameplay().getPlayerOne();
        }
        Rectangle avatar = new Rectangle(62, 73);
        avatar.setFill(new ImagePattern(lowerPlayer.getUser().getProfilePicture()));
        lowerInfo.getChildren().add(avatar);
        avatar = new Rectangle(62, 73);
        avatar.setFill(new ImagePattern(upperPlayer.getUser().getProfilePicture()));
        upperInfo.getChildren().add(avatar);
        lowerInfo.setLayoutX(500);
        lowerInfo.setLayoutY(500);
        upperInfo.setLayoutX(500);
        upperInfo.setLayoutY(50);

        Rectangle LPForeground = new Rectangle(150 * ((double) lowerPlayer.getLifePoints()) / 8000, 30, Color.RED);
        AnchorPane healthBar = new AnchorPane();
        healthBar.setMaxSize(150, 30);
        healthBar.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        healthBar.getChildren().add(LPForeground);
        lowerInfo.getChildren().add(healthBar);

        LPForeground = new Rectangle(150 * ((double) upperPlayer.getLifePoints()) / 8000, 30, Color.RED);
        healthBar = new AnchorPane();
        healthBar.setMaxSize(150, 30);
        healthBar.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        healthBar.getChildren().add(LPForeground);
        upperInfo.getChildren().add(healthBar);


        pane.getChildren().addAll(upperInfo, lowerInfo);
    }

    public static void updateCardDisplayPanel(FieldArea fieldArea) {
        if (fieldArea.getCard() == null) return;
        boolean ownsCard = false;
        if (!fieldArea.visibility()) {
            if (fieldArea instanceof MonsterFieldArea) {
                for (MonsterFieldArea monsterField :
                        GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getMonstersField()) {
                    if (monsterField.equals(fieldArea)) {
                        ownsCard = true;
                        break;
                    }
                }
                if (!ownsCard) return;
            } else if (fieldArea instanceof SpellAndTrapFieldArea) {
                for (SpellAndTrapFieldArea spellField :
                        GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getSpellAndTrapField()) {
                    if (spellField.equals(fieldArea)) {
                        ownsCard = true;
                        break;
                    }
                }
                if (fieldArea instanceof FieldZoneArea) {
                    if (GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getFieldZone().equals(fieldArea))
                        ownsCard = true;
                }
                if (!ownsCard) return;
            } else if (fieldArea instanceof HandFieldArea) {
                for (HandFieldArea handField :
                        GameplayController.getInstance().gameplay.getCurrentPlayer().getPlayingHand()) {
                    if (handField.equals(fieldArea)) {
                        ownsCard = true;
                        break;
                    }
                }
                if (!ownsCard) return;
            }
        }
        Rectangle cardView = (Rectangle) cardDisplay.getChildren().get(0);
        Label description = (Label) cardDisplay.getChildren().get(1);
        cardView.setFill(fieldArea.getCard().getFill());
        description.setText(fieldArea.getCard().getDescription());
    }

    public static void ritualTribute(Player player, FieldArea ritualSpell, boolean isSummon) {
        Stage deckShowStage = new Stage();
        deckShowStage.initModality(Modality.APPLICATION_MODAL);
        ArrayList<Card> deck = player.getPlayingDeck().getMainCards();
        ArrayList<String> ids = new ArrayList<>();
        HBox hBox = new HBox();
        Button button = new Button("Ritual Tribute");
        button.setDisable(true);
        for (int i = 0; i < deck.size(); i++) {
            Rectangle cardView = new Rectangle(52.5, 75);
            cardView.setFill(deck.get(i).getFill());
            String finalI = String.valueOf(i + 1);
            cardView.setOnMouseClicked(mouseEvent -> {
                if (!ids.contains(finalI)) ids.add(finalI);
                else ids.remove(finalI);
                if (GameplayController.getInstance().isRitualInputsValid(ids.toArray(new String[0])))
                    button.setDisable(false);
            });
            hBox.getChildren().add(cardView);
        }
        button.setOnAction(actionEvent -> {
            GameplayController.getInstance().ritualTribute(ids.toArray(new String[0]), ritualSpell, isSummon);
            deckShowStage.hide();

        });
        hBox.setAlignment(Pos.CENTER);
        ScrollPane scrollPane = new ScrollPane();
        VBox contents = new VBox(10);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setContent(hBox);
        contents.getChildren().add(scrollPane);
        contents.getChildren().add(button);
        Scene scene = new Scene(contents, 400, 200);
        deckShowStage.setTitle(player.getUser().getUsername() + "'s Deck");
        deckShowStage.setScene(scene);
        deckShowStage.show();
    }

    public static void chainPrompt() {
        Alert chainPrompt = new Alert(Alert.AlertType.CONFIRMATION);
        chainPrompt.setTitle("Chain Prompt");
        chainPrompt.setHeaderText("Do you want to activate your Trap?");
        Optional<ButtonType> response = chainPrompt.showAndWait();
        ButtonType buttonPressed;
        if (response.isEmpty()) buttonPressed = null;
        else buttonPressed = response.get();
        if (buttonPressed == null || buttonPressed == ButtonType.CANCEL) {
            GameplayController.getInstance().temporarySwitchTurn();
            switch (GameplayController.getChainType()) {
                case ON_ATTACKED:
                    try {
                        StringBuilder message = GameplayController.getInstance().attack();
                        showInfo(String.valueOf(message));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    GameplayController.getInstance().resetChainSituation();
                    break;
                case ON_SUMMON:
                case ON_STANDBY:
                    GameplayController.getInstance().resetChainSituation();
                    break;
                case ON_SPELL:
                    try {
                        GameplayController.getInstance().activateSpellEffect();
                    } catch (Exception e) {
                        showAlert(e.getMessage());
                    }
                    GameplayController.getInstance().resetChainSituation();
                    break;
            }
        }
        if (buttonPressed == ButtonType.OK) {
            GameplayController.setGameState(GameState.CHAIN_MODE);
        }

    }

    @Override
    public void start(Stage stage) throws Exception {
        thisStage = stage;
        createBoard();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        setCheatConsole(stage);
        stage.show();
        addedCardsAlert.setTitle("New Card!");
        newPhaseAlert.setTitle("New Phase!");
        addedCardsAlert.setHeaderText(GameplayController.getInstance().doPhaseAction());
        addedCardsAlert.show();
    }

    public void createBoard() {
        pane = new AnchorPane();
        pane.setBackground(new Background(new BackgroundFill(Color.web("#825030", 1.0), CornerRadii.EMPTY, Insets.EMPTY)));
        effectItem = new MenuItem("Activate effect");
        summonItem = new MenuItem("Summon");
        setItem = new MenuItem("Set");
        directAttackItem = new MenuItem("Direct attack");
        attackItem = new MenuItem("Attack");
        flipItem = new MenuItem("Flip summon");
        changePositionItem = new MenuItem("Change position");
        nextPhaseButton = new Button("Next phase");
        settingsButton = new Button("Settings");
        addedCardsAlert = new Alert(Alert.AlertType.INFORMATION);
        newPhaseAlert = new Alert(Alert.AlertType.INFORMATION);
        monsterItems = new ArrayList<>();
        spellItems = new ArrayList<>();
        handItems = new ArrayList<>();
        cardDisplay = new VBox(10);
        lowerInfo = new VBox();
        upperInfo = new VBox();
        settingsDialog = new Dialog<>();
        GameplayController.getInstance().setStartingPlayer();
        GameplayController.getInstance().dealCardsAtBeginning();
        Gameplay gameplay = GameplayController.getInstance().getGameplay();
        gameplay.getOpponentPlayer().getField().setRotate(180);
        createCardDisplayPanel();
        createLPs();
        changePosition();
        flipSummon();
        activateEffect();
        attack();
        directAttack();
        createHandItems();
        nextPhase();
        createSettings();
        gameplay.getCurrentPlayer().getField().setAlignment(Pos.CENTER);
        gameplay.getOpponentPlayer().getField().setAlignment(Pos.CENTER);
        gameplay.getCurrentPlayer().getField().setLayoutX(0);
        gameplay.getCurrentPlayer().getField().setLayoutY(FieldArea.getFieldAreaHeight() * 5);
        gameplay.getOpponentPlayer().getField().setLayoutX(0);
        gameplay.getOpponentPlayer().getField().setLayoutY(0);
        cardDisplay.setLayoutX(500);
        cardDisplay.setLayoutY(200);
        pane.getChildren().add(gameplay.getCurrentPlayer().getField());
        pane.getChildren().add(gameplay.getOpponentPlayer().getField());
        pane.getChildren().add(cardDisplay);
        updateLPs();
        System.out.println(gameplay);
        hideOpponentHands(gameplay);
    }

    private void createSettings() {
        ButtonType surrender = new ButtonType("SURRENDER");
        ButtonType ok = new ButtonType("CANCEL");
        settingsDialog.getDialogPane().getButtonTypes().addAll(surrender, ok);
        settingsDialog.setTitle("Settings");
        settingsDialog.setHeight(100);
        settingsDialog.setWidth(200);
        settingsDialog.getDialogPane().setContent(null);
        settingsButton.setOnAction(actionEvent -> {
            Optional<ButtonType> result = settingsDialog.showAndWait();
            if (result.isPresent()) {
                if (result.get() == surrender) {
                    //TODO add a menu here
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String message = GameplayController.getInstance().surrender();
                    showInfo(message);
                    try {
                        DatabaseController.getInstance().saveAllUsers();
                        new MainMenu().start(WelcomeMenu.stage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (result.get() == ok) {
                    settingsDialog.close();
                }
            }
        });
        cardDisplay.getChildren().add(settingsButton);
    }

    public void updateLPs() {
        lowerInfo.getChildren().remove(lowerInfo.getChildren().size() - 1);
        upperInfo.getChildren().remove(upperInfo.getChildren().size() - 1);
        String lowerNickname = ((Label) lowerInfo.getChildren().get(0)).getText();
        Player lowerPlayer, upperPlayer;
        if (GameplayController.getInstance().getGameplay().getPlayerOne().getUser().getNickname().equals(lowerNickname)) {
            lowerPlayer = GameplayController.getInstance().getGameplay().getPlayerOne();
            upperPlayer = GameplayController.getInstance().getGameplay().getPlayerTwo();
        } else {
            lowerPlayer = GameplayController.getInstance().getGameplay().getPlayerTwo();
            upperPlayer = GameplayController.getInstance().getGameplay().getPlayerOne();
        }
        Rectangle LPForeground = new Rectangle(150 * ((double) lowerPlayer.getLifePoints()) / 8000, 30, Color.RED);
        AnchorPane healthBar = new AnchorPane();
        healthBar.setMaxSize(150, 30);
        healthBar.setMinSize(150, 30);
        Label healthLabel = new Label(String.valueOf(lowerPlayer.getLifePoints()) + "/8000");
        healthBar.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        healthBar.getChildren().addAll(LPForeground, healthLabel);
        lowerInfo.getChildren().add(healthBar);

        LPForeground = new Rectangle(150 * ((double) upperPlayer.getLifePoints()) / 8000, 30, Color.RED);
        healthBar = new AnchorPane();
        healthBar.setMaxSize(150, 30);
        healthBar.setMinSize(150, 30);
        healthLabel = new Label(String.valueOf(upperPlayer.getLifePoints()) + "/8000");
        healthBar.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        healthBar.getChildren().addAll(LPForeground, healthLabel);
        upperInfo.getChildren().add(healthBar);
        if (lowerPlayer.getLifePoints() <= 0)
            endGame(GameplayController.getInstance().endARound(upperPlayer, lowerPlayer));
        else if (upperPlayer.getLifePoints() <= 0)
            endGame(GameplayController.getInstance().endARound(upperPlayer, lowerPlayer));
    }

    private void setCheatConsole(Stage stage) {
        stage.getScene().getAccelerators().put(
                KeyCombination.keyCombination("CTRL+SHIFT+C"),
                new Runnable() {
                    @Override
                    public void run() {
                        TextInputDialog cheatBox = new TextInputDialog();
                        cheatBox.setTitle("CHEAT");
                        cheatBox.setHeaderText("");
                        cheatBox.setContentText("");
                        Optional<String> cheatCode = cheatBox.showAndWait();
                        if (cheatCode.isPresent()) {
                            processCheatCode(cheatCode.get());
                        }
                    }
                }
        );

        stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.ESCAPE) {
                    if (GameplayController.getGameState() == GameState.NORMAL_MODE){
                        ke.consume();
                        return;
                    }
                    if (GameplayController.getGameState() != GameState.CHAIN_MODE && GameplayController.getGameState() != GameState.ATTACK_MODE) {
                        if (GameplayController.getGameState() == GameState.RITUAL_SET_MODE || GameplayController.getGameState() == GameState.RITUAL_SUMMON_MODE || GameplayController.getGameState() == GameState.RITUAL_SPELL_ACTIVATED_MODE){
                            GameplayController.getInstance().effectSpellAndTraps.clear();
                        }
                            GameplayController.setGameState(GameState.NORMAL_MODE);
                    }
                    ke.consume();
                }
            }
        });
    }

    private void processCheatCode(String cheat) {
        Matcher matcher;
        if ((matcher = Regex.getCommandMatcher(cheat, Regex.addCardToHandCheatCode)).matches())
            forceAddCardCheat(matcher);
        else if ((matcher = Regex.getCommandMatcher(cheat, Regex.increaseLifePointsCheatCode)).matches())
            increaseLifePointsCheat(matcher);
        else if ((matcher = Regex.getCommandMatcher(cheat, Regex.forceAddCardCheatCode)).matches())
            forceAddCardCheat(matcher);
        else if ((matcher = Regex.getCommandMatcher(cheat, Regex.setWinnerCheatCode)).matches())
            setWinnerCheat(matcher);
            //TODO delete print
        else System.out.println("invalid command");
    }

    private void nextPhase() {
        nextPhaseButton.setOnAction(actionEvent -> {
            if (GameplayController.getGameState() == GameState.CHAIN_MODE) return;
            if (GameplayController.getGameState() == GameState.RITUAL_SET_MODE) return;
            if (GameplayController.getGameState() == GameState.RITUAL_SUMMON_MODE) return;
            if (GameplayController.getGameState() == GameState.RITUAL_SPELL_ACTIVATED_MODE) return;
            String newPhaseMessage = GameplayController.getInstance().goToNextPhase();
            String addedCards = null;
            try {
                addedCards = GameplayController.getInstance().doPhaseAction();
                if (addedCards != null) {
                    addedCardsAlert.setHeaderText(newPhaseMessage + "\n" + addedCards);
                    addedCardsAlert.show();
                } else {
                    newPhaseAlert.setHeaderText(newPhaseMessage);
                    newPhaseAlert.show();
                }
            } catch (DeckEmptiedException e) {
                showAlert(e.getMessage());
            }

            GameplayController.setGameState(GameState.NORMAL_MODE);
            GameplayController.getInstance().onStandbyTraps();
        });
        cardDisplay.getChildren().add(nextPhaseButton);
    }

    private void attack() {
        attackItem.setOnAction(actionEvent -> GameplayController.setGameState(GameState.ATTACK_MODE));
        monsterItems.add(attackItem);
    }

    private void activateEffect() {
        effectItem.setOnAction(actionEvent -> {
            if (GameplayController.getGameState() == GameState.NORMAL_MODE) try {
                GameplayController.getInstance().activateEffect(SpellAndTrapActivationType.NORMAL);
            } catch (Exception e) {
                showAlert(e.getMessage());
            }
            if (GameplayController.getGameState() == GameState.CHAIN_MODE) {
                try {
                    try {
                        GameplayController.getInstance().activateEffect(GameplayController.getChainType());
                        GameplayController.getInstance().temporarySwitchTurn();
                        GameplayController.getInstance().resetOpponentTrapsAndSpells(GameplayController.getChainType());
                        GameplayController.setChainType(SpellAndTrapActivationType.NORMAL);
                        GameplayController.setGameState(GameState.NORMAL_MODE);
                    } catch (InvalidActivateException | RitualSummonNotPossibleException | AlreadyActivatedException | SpecialSummonNotPossibleException | CommandCancellationException | MonsterZoneFullException | WrongPhaseForSpellException | SpellZoneFullException | PreparationNotReadyException | NoCardIsSelectedException e) {
                        showAlert(e.getMessage());
                    }
                } catch (ActionNotPossibleException | AttackNotPossibleException e) {
                    GameplayController.getInstance().temporarySwitchTurn();
                    GameplayController.getInstance().resetOpponentTrapsAndSpells(GameplayController.getChainType());
                    showAlert(e.getMessage());
                    GameplayController.setChainType(SpellAndTrapActivationType.NORMAL);
                    GameplayController.setGameState(GameState.NORMAL_MODE);
                }
            }
            GameplayController.getInstance().calculateFieldZoneEffects();
        });
        spellItems.add(effectItem);
    }

    private void changePosition() {
        changePositionItem.setOnAction(actionEvent -> {
            FieldArea fieldArea;
            if ((fieldArea = GameplayController.getInstance().gameplay.getSelectedField()) == null) return;
            if (!(fieldArea instanceof MonsterFieldArea)) return;
            MonsterFieldArea selectedMonsterField = (MonsterFieldArea) fieldArea;
            try {
                GameplayController.getInstance().changePosition(!selectedMonsterField.isAttack());
            } catch (Exception e) {
                showAlert(e.getMessage());
            }
            GameplayController.getInstance().calculateFieldZoneEffects();
        });
        monsterItems.add(changePositionItem);
    }

    private void directAttack() {
        directAttackItem.setOnAction(actionEvent -> {
            try {
                String message = GameplayController.getInstance().directAttack();
                playSoundEffect("swordSwoosh.wav");
                showInfo(message);
            } catch (Exception e) {
                showAlert(e.getMessage());
            }
            GameplayController.getInstance().calculateFieldZoneEffects();
            updateLPs();
        });
        monsterItems.add(directAttackItem);
    }

    private void flipSummon() {
        flipItem.setOnAction(actionEvent -> {
            FieldArea fieldArea;
            if ((fieldArea = GameplayController.getInstance().gameplay.getSelectedField()) == null) return;
            if (!(fieldArea instanceof MonsterFieldArea)) return;
            try {
                GameplayController.getInstance().flipSummon();
            } catch (Exception e) {
                showAlert(e.getMessage());
            }
            GameplayController.getInstance().calculateFieldZoneEffects();
        });
        monsterItems.add(flipItem);
    }

    private void createHandItems() {
        if (!handItems.isEmpty()) return;
        setItem.setOnAction(actionEvent -> {
            try {
                GameplayController.getInstance().set();
                playSoundEffect("cardFlip.mp3");
            } catch (Exception e) {
                showAlert(e.getMessage());
            }
            GameplayController.getInstance().calculateFieldZoneEffects();
        });
        summonItem.setOnAction(actionEvent -> {
            try {
                GameplayController.getInstance().summon();
                playSoundEffect("monsterGrowl.mp3");
            } catch (Exception e) {
                showAlert(e.getMessage());
            }
            GameplayController.getInstance().calculateFieldZoneEffects();
        });
        handItems.add(setItem);
        handItems.add(summonItem);
        handItems.add(effectItem);
    }

    public void hideOpponentHands(Gameplay gameplay) {
        for (Node node :
                gameplay.getCurrentPlayer().getField().getHandFieldArea().getChildren()) {
            HandFieldArea hand = (HandFieldArea) node;
            hand.getCardView().setFill(hand.getCard().getFill());
        }
        for (Node node :
                gameplay.getOpponentPlayer().getField().getHandFieldArea().getChildren()) {
            HandFieldArea hand = (HandFieldArea) node;
            hand.getCardView().setFill(new ImagePattern(Card.UNKNOWN_CARD));
        }
        for (MonsterFieldArea monsterFieldArea :
                gameplay.getOpponentPlayer().getField().getMonstersField()) {
            if (!monsterFieldArea.visibility()) monsterFieldArea.getStats().setVisible(false);
        }
        for (MonsterFieldArea monsterFieldArea :
                gameplay.getCurrentPlayer().getField().getMonstersField()) {
            if (!monsterFieldArea.visibility()) monsterFieldArea.getStats().setVisible(true);
        }
        try {
            GameplayController.getInstance().deselectCard();
        } catch (NoCardIsSelectedException ignored) {
        }
        gameplay.getCurrentPlayer().getField().getHandScrollPane().setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        gameplay.getOpponentPlayer().getField().getHandScrollPane().setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    public void showGraveyard(Player player) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        ArrayList<Card> graveyardCards = player.getField().getGraveyard();
        HBox hBox = new HBox();
        for (Card card :
                graveyardCards) {
            Rectangle rectangle = new Rectangle(70, 100, card.getFill());
            hBox.getChildren().add(rectangle);
        }
        hBox.setAlignment(Pos.CENTER);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setContent(hBox);
        Scene scene = new Scene(scrollPane, 400, 200);
        stage.setTitle(player.getUser().getUsername() + "'s graveyard");
        stage.setScene(scene);
        stage.show();
    }


    private void forceAddCardCheat(Matcher matcher) {
        String cardName = matcher.group("cardName");
        try {
            GameplayController.getInstance().forceAddCard(cardName);
        } catch (InvalidCardNameException e) {
            System.out.println(e.getMessage());
        }
    }

    private void increaseLifePointsCheat(Matcher matcher) {
        int amount = Integer.parseInt(matcher.group("amount"));
        GameplayController.getInstance().increaseLifePointsCheat(amount);
        updateLPs();
    }

    private void setWinnerCheat(Matcher matcher) {
        String nickname = matcher.group("nickname");
        String message = GameplayController.getInstance().setWinnerCheat(nickname);
        if (message == null) return;
        endGame(message);
        try {
            GameplayController.getInstance().doPhaseAction();
        } catch (DeckEmptiedException e) {
            //TODO fix
            e.printStackTrace();
            System.out.println("NOW WHAT?");
        }
    }

    public void endGame(String message) {
        showInfo(message);
        if (message.contains("won the whole match")) {
            try {
                DatabaseController.getInstance().saveAllUsers();
                new MainMenu().start(WelcomeMenu.stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (message.contains("won the game")) {
            createBoard();
        }
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