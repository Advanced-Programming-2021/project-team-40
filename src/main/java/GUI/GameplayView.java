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
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;

public class GameplayView extends Application {
    final private static int GAMEPLAY_WIDTH = 700;
    final private static int GAMEPLAY_HEIGHT = 600;
    private static final AnchorPane pane = new AnchorPane();
    private static final MenuItem effectItem = new MenuItem("Activate effect");
    private static final MenuItem summonItem = new MenuItem("Summon");
    private static final MenuItem setItem = new MenuItem("Set");
    private static final MenuItem directAttackItem = new MenuItem("Direct attack");
    private static final MenuItem attackItem = new MenuItem("Attack");
    private static final MenuItem flipItem = new MenuItem("Flip summon");
    private static final MenuItem changePositionItem = new MenuItem("Change position");
    private static final Button nextPhaseButton = new Button("Next phase");
    private static final Button settingsButton = new Button("Settings");
    private static final Alert addedCardsAlert = new Alert(Alert.AlertType.INFORMATION);
    private static final Alert newPhaseAlert = new Alert(Alert.AlertType.INFORMATION);
    public static ArrayList<MenuItem> monsterItems = new ArrayList<>();
    public static ArrayList<MenuItem> spellItems = new ArrayList<>();
    public static ArrayList<MenuItem> handItems = new ArrayList<>();
    public static VBox cardDisplay = new VBox(10);
    private static GameplayView gameplayView;

    private static HBox lowerInfo = new HBox();

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
        createLPs();
    }

    private static void createLPs(){
        //TODO fix this
        lowerInfo.setPrefSize(200, 200);
        lowerInfo.setLayoutX(640);
        lowerInfo.setLayoutY(30);
        lowerInfo.getChildren().add(new Label(GameplayController.getInstance().getGameplay().getCurrentPlayer().getUser().getNickname()));
        String lowerNickname = GameplayController.getInstance().getGameplay().getCurrentPlayer().getUser().getNickname();
        Player lowerPlayer, upperPlayer;
        if (GameplayController.getInstance().getGameplay().getPlayerOne().getUser().getNickname().equals(lowerNickname)) {
            lowerPlayer = GameplayController.getInstance().getGameplay().getPlayerOne();
            upperPlayer = GameplayController.getInstance().getGameplay().getPlayerTwo();
        } else {
            lowerPlayer = GameplayController.getInstance().getGameplay().getPlayerTwo();
            upperPlayer = GameplayController.getInstance().getGameplay().getPlayerOne();
        }
        lowerInfo.setLayoutX(600);
        lowerInfo.setLayoutY(500);
        Rectangle LPForeground = new Rectangle(150 * ((double) lowerPlayer.getLifePoints()) / 8000, 30, Color.RED);
        AnchorPane healthBar = new AnchorPane();
        healthBar.setMaxSize(150, 30);
        healthBar.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        healthBar.getChildren().add(LPForeground);
        lowerInfo.getChildren().add(healthBar);
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
            System.out.println(GameplayController.getGameState());
        }

    }

    @Override
    public void start(Stage stage) throws Exception {
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
        DatabaseController.getInstance();//TODO: remove deez
        Gameplay gameplay = new Gameplay(new Player(User.getUserByName("DanDan")), new Player(User.getUserByName("KiaKia")), 1);
        GameplayController.getInstance().setGameplay(gameplay);
        GameplayController.getInstance().setStartingPlayer();
        GameplayController.getInstance().dealCardsAtBeginning();
        try {
            GameplayController.getInstance().forceAddCard("Mirror Force");
            GameplayController.getInstance().forceAddCard("Black Pendant");
        } catch (InvalidCardNameException ignored) {
        }
        gameplay.getOpponentPlayer().getField().setRotate(180);
        createCardDisplayPanel();
        changePosition();
        flipSummon();
        activateEffect();
        attack();
        directAttack();
        createHandItems();
        nextPhase();
        gameplay.getCurrentPlayer().getField().setAlignment(Pos.CENTER);
        gameplay.getOpponentPlayer().getField().setAlignment(Pos.CENTER);
        gameplay.getCurrentPlayer().getField().setLayoutX(0);
        gameplay.getCurrentPlayer().getField().setLayoutY(FieldArea.getFieldAreaHeight() * 5);
        gameplay.getOpponentPlayer().getField().setLayoutX(0);
        gameplay.getOpponentPlayer().getField().setLayoutY(0);
        nextPhaseButton.setAlignment(Pos.BOTTOM_RIGHT);
        cardDisplay.setLayoutX(500);
        cardDisplay.setLayoutY(200);
        pane.getChildren().add(gameplay.getCurrentPlayer().getField());
        pane.getChildren().add(gameplay.getOpponentPlayer().getField());
        pane.getChildren().add(cardDisplay);
        //TODO: add this
        updateLPs();
        pane.getChildren().addAll(lowerInfo);
        hideOpponentHands();
    }

    public void updateLPs() {

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
        else System.out.println("invalid command");
    }

    private void nextPhase() {
        nextPhaseButton.setOnAction(actionEvent -> {
            if (GameplayController.getGameState() == GameState.CHAIN_MODE) return;
            if (GameplayController.getGameState() == GameState.RITUAL_SET_MODE) return;
            if (GameplayController.getGameState() == GameState.RITUAL_SUMMON_MODE) return;
            if (GameplayController.getGameState() == GameState.RITUAL_SPELL_ACTIVATED_MODE) return;
            String newPhaseMessage = GameplayController.getInstance().goToNextPhase();
            String addedCards = GameplayController.getInstance().doPhaseAction();
            if (addedCards != null) {
                addedCardsAlert.setHeaderText(newPhaseMessage + "\n" + addedCards);
                addedCardsAlert.show();
            } else {
                newPhaseAlert.setHeaderText(newPhaseMessage);
                newPhaseAlert.show();
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
                System.out.println(message);
            } catch (Exception e) {
                showAlert(e.getMessage());
            }
            GameplayController.getInstance().calculateFieldZoneEffects();
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
            } catch (Exception e) {
                showAlert(e.getMessage());
            }
            GameplayController.getInstance().calculateFieldZoneEffects();
        });
        summonItem.setOnAction(actionEvent -> {
            try {
                GameplayController.getInstance().summon();
            } catch (Exception e) {
                showAlert(e.getMessage());
            }
            GameplayController.getInstance().calculateFieldZoneEffects();
        });
        handItems.add(setItem);
        handItems.add(summonItem);
        handItems.add(effectItem);
    }

    public void hideOpponentHands() {
        Gameplay gameplay = GameplayController.getInstance().gameplay;
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
    }

    private void setWinnerCheat(Matcher matcher) {
        String nickname = matcher.group("nickname");
        String message = GameplayController.getInstance().setWinnerCheat(nickname);
        if (message == null) return;
        System.out.println(message);
        GameplayController.getInstance().doPhaseAction();
    }


}