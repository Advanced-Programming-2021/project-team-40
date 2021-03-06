package Controller.DatabaseController;

import Controller.ClientController;
import Controller.DuelController.GameplayController;
import Controller.ProgramController.ProgramController;
import Controller.ProgramController.Regex;
import Database.Cards.*;
import Database.Deck;
import Database.EfficientDeck;
import Database.EfficientUser;
import Database.User;
import GUI.MainMenu;
import Gameplay.*;
import View.Exceptions.*;
import View.GameplayView;
import View.GraveyardView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;

public class DatabaseController {

    private static DatabaseController databaseController;

    private DatabaseController() {
        initialize();
    }

    public static DatabaseController getInstance() {
        if (databaseController == null)
            databaseController = new DatabaseController();
        return databaseController;
    }

    private void initialize() {
        initializeMonsterCards();
        initializeSpellAndTrapCards();
    }

    public void saveUser(User user) {
        EfficientUser efficientUser = new EfficientUser(user);
        Gson gson = new GsonBuilder().create();
        ClientController.sendMessage(MainMenu.userToken + " save user " + gson.toJson(efficientUser));
    }

    public void initializeSpellAndTrapCards() {
        try {
            File spellAndTrapCards = new File("./Client/src/main/resources/Cards/SpellTrap.csv");
            FileReader fileReader = new FileReader(spellAndTrapCards);
            CSVReader csvReader = new CSVReader(fileReader);
            csvReader.readNext();
            String[] cardDetails;
            while ((cardDetails = csvReader.readNext()) != null) {
                switch (cardDetails[1]) {
                    case "Trap":
                        new Trap(cardDetails[0], getIcon(cardDetails[2]), cardDetails[3], isLimited(cardDetails[4]), Integer.parseInt(cardDetails[5]));
                        break;
                    case "Spell":
                        new Spell(cardDetails[0], getIcon(cardDetails[2]), cardDetails[3], isLimited(cardDetails[4]), Integer.parseInt(cardDetails[5]));
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        initializeTrapEffects();
        initializeSpellEffects();
    }

    private void initializeMonsterCards() {
        try {
            File monsterCards = new File("./Client/src/main/resources/Cards/Monster.csv");
            FileReader fileReader = new FileReader(monsterCards);
            CSVReader csvReader = new CSVReader(fileReader);
            csvReader.readNext();
            String[] cardDetails;
            while ((cardDetails = csvReader.readNext()) != null) {
                new Monster(cardDetails[0], Integer.parseInt(cardDetails[1]), getAttribute(cardDetails[2]), getMonsterType(cardDetails[3]),
                        getCardType(cardDetails[4]), Integer.parseInt(cardDetails[5]), Integer.parseInt(cardDetails[6]), cardDetails[7], Integer.parseInt(cardDetails[8]));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        initializeMonsterCardEffects();
    }

    private void initializeTrapEffects() {
        for (Card trap :
                Trap.getTraps()) {
            switch (trap.getName()) {
                case "Mirror Force":
                    trap.onBeingAttacked = objects -> {
                        for (MonsterFieldArea monster :
                                Effect.gameplay.getOpponentPlayer().getField().getMonstersField()) {
                            if (monster.getCard() != null)
                                if (monster.isAttack())
                                    GameplayController.getInstance().destroyMonsterCard(Effect.gameplay.getOpponentPlayer(), monster);
                        }
                        throw new ActionNotPossibleException("Trap destroyed all your monster cards!");
                    };
                    break;
                case "Negate Attack":
                    trap.onBeingAttacked = objects -> {
                        GameplayController.getInstance().goToNextPhase();
                        throw new ActionNotPossibleException("Your attack was negated!");
                    };
                    break;
                case "Magic Cylinder":
                    trap.onBeingAttacked = objects -> {
                        MonsterFieldArea attackingMonster = (MonsterFieldArea) Effect.gameplay.getAttacker();
                        Effect.gameplay.getOpponentPlayer().setLifePoints(Effect.gameplay.getOpponentPlayer().getLifePoints() - attackingMonster.getAttackPoint());
                        throw new ActionNotPossibleException("Your attack was negated!");
                    };
                    break;
                case "Torrential Tribute":
                    trap.onSummon = objects -> {
                        for (MonsterFieldArea monster :
                                Effect.gameplay.getOpponentPlayer().getField().getMonstersField()) {
                            if (monster.getCard() != null)
                                GameplayController.getInstance().destroyMonsterCard(Effect.gameplay.getOpponentPlayer(), monster);
                        }
                        throw new ActionNotPossibleException("Trap destroyed all your Monster Cards");
                    };
                    break;
                case "Trap Hole":
                    trap.onSummon = objects -> {
                        MonsterFieldArea summonedMonster = Effect.gameplay.getRecentlySummonedMonster();
                        if (summonedMonster.getAttackPoint() >= 1000)
                            GameplayController.getInstance().destroyMonsterCard(Effect.gameplay.getOpponentPlayer(), summonedMonster);
                    };
                    break;
                case "Magic Jammer":
                    trap.onSpellActivation = objects -> {
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setTitle("Magic Jammer");
                        HBox handView = new HBox(5);
                        for (HandFieldArea handField :
                                Effect.gameplay.getCurrentPlayer().getPlayingHand()) {
                            Rectangle cardView = new Rectangle(70, 100, handField.getCard().getFill());
                            cardView.setOnMouseClicked(mouseEvent -> {
                                Effect.gameplay.setSelectedField(handField);
                                Effect.gameplay.setOwnsSelectedCard(true);
                            });
                            handView.getChildren().add(cardView);
                        }
                        ButtonType destroy = new ButtonType("DISCARD!", ButtonBar.ButtonData.OK_DONE);
                        dialog.getDialogPane().getButtonTypes().add(destroy);
                        dialog.getDialogPane().setContent(handView);
                        Optional<ButtonType> result = dialog.showAndWait();
                        if (result.isPresent() && result.get() == destroy) {
                            try {
                                GameplayController.getInstance().discardSelectedCard();
                                if (GameplayController.getInstance().toActivateSpell instanceof HandFieldArea)
                                    GameplayController.getInstance().destroyHandFieldCard(Effect.gameplay.getOpponentPlayer(), (HandFieldArea) GameplayController.getInstance().toActivateSpell);
                                else if (GameplayController.getInstance().toActivateSpell instanceof SpellAndTrapFieldArea)
                                    GameplayController.getInstance().destroySpellAndTrapCard(Effect.gameplay.getOpponentPlayer(), (SpellAndTrapFieldArea) GameplayController.getInstance().toActivateSpell);
                                dialog.hide();
                                throw new ActionNotPossibleException("Opponent's magic jammer destroyed your card!");
                            } catch (InvalidCardSelectionException ignored) {
                            }
                        }
                    };
                    break;
                case "Mind Crush":
                    trap.inStandbyPhase = objects -> {
                        TextInputDialog dialog = new TextInputDialog("Card Name");
                        dialog.setTitle("Mind Crush");
                        dialog.setContentText("Declare A Card Name:");
                        Optional<String> result = dialog.showAndWait();
                        String input;
                        if (result.isPresent()) {
                            input = result.get();
                            boolean isRight = false;
                            for (HandFieldArea hand :
                                    Effect.gameplay.getOpponentPlayer().getPlayingHand()) {
                                if (hand.getCard().getName().equalsIgnoreCase(input)) {
                                    isRight = true;
                                    break;
                                }
                            }
                            if (isRight) {
                                Effect.gameplay.getOpponentPlayer().getPlayingHand().removeIf(hand -> hand.getCard().getName().equalsIgnoreCase(input));
                                Effect.gameplay.getOpponentPlayer().getField().getHandFieldArea().getChildren().removeIf(hand -> ((HandFieldArea) hand).getCard().getName().equalsIgnoreCase(input));
                            } else {
                                int random = new Random().nextInt(Effect.gameplay.getCurrentPlayer().getPlayingHand().size());
                                try {
                                    GameplayController.getInstance().selectCard(Integer.toString(random), "-h", false);
                                    GameplayController.getInstance().discardSelectedCard();
                                } catch (Exception ignored) {
                                }
                            }
                        }
                    };
                    break;
                case "Call of The Haunted":
                    trap.inStandbyPhase = objects -> {
                        if (Effect.gameplay.getCurrentPlayer().getField().getGraveyard().size() == 0 && Effect.gameplay.getOpponentPlayer().getField().getGraveyard().size() == 0)
                            throw new ActionNotPossibleException("you can't use this card effects");
                        if (Effect.gameplay.getCurrentPlayer().getField().getFreeMonsterFieldArea() == null)
                            throw new MonsterZoneFullException();
                        System.out.println("Please select a card from you or your opponent's graveyard:");
                        String input;
                        Card cardToSpecialSummon;
                        while (true) {
                            ArrayList<Card> graveyard = new ArrayList<>();
                            for (Card card : Effect.gameplay.getCurrentPlayer().getField().getGraveyard()) {
                                if (card instanceof Monster) graveyard.add(card);
                            }
                            for (Card card : Effect.gameplay.getOpponentPlayer().getField().getGraveyard()) {
                                if (card instanceof Monster) graveyard.add(card);
                            }
                            GraveyardView.showGraveyard(graveyard);
                            input = ProgramController.getInstance().getScanner().nextLine();
                            if (input.matches("\\d+")) {
                                if (Integer.parseInt(input) > graveyard.size())
                                    System.out.println("location invalid in this graveyard");
                                else {
                                    cardToSpecialSummon = graveyard.get(Integer.parseInt(input) - 1);
                                    graveyard.remove(Integer.parseInt(input) - 1);
                                    if (Effect.gameplay.getCurrentPlayer().getField().getGraveyard().contains(cardToSpecialSummon))
                                        Effect.gameplay.getCurrentPlayer().getField().getGraveyard().remove(cardToSpecialSummon);
                                    else
                                        Effect.gameplay.getOpponentPlayer().getField().getGraveyard().remove(cardToSpecialSummon);
                                    GameplayController.getInstance().specialSummon(cardToSpecialSummon);
                                    break;
                                }

                            }
                        }
                    };
                    break;
            }
        }
    }

    private void initializeMonsterCardEffects() {
        for (Card card :
                Card.getAllCards()) {
            switch (card.getName()) {
                case "Man-Eater Bug":
                    card.setHasEffect(true);
                    card.onFlipSummon = objects -> {
                        boolean monsterExists = false;
                        for (MonsterFieldArea monster :
                                GameplayController.getInstance().gameplay.getOpponentPlayer().getField().getMonstersField()) {
                            if (monster.getCard() != null) {
                                monsterExists = true;
                                break;
                            }
                        }
                        if (!monsterExists) return;
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setTitle("Man-Eater Bug");
                        HBox availableMonster = new HBox(5);
                        final MonsterFieldArea[] toDestroy = new MonsterFieldArea[1];
                        for (MonsterFieldArea monster :
                                GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getMonstersField()) {
                            Rectangle cardView = new Rectangle(70, 100);
                            if (monster.getCard() == null) {
                                cardView.setFill(Color.TRANSPARENT);
                            } else {
                                cardView.setFill(monster.getCard().getFill());
                                cardView.setOnMouseClicked(mouseEvent -> toDestroy[0] = monster);
                                if (monster.visibility()) {
                                    if (!monster.isAttack()) cardView.setRotate(-90);
                                } else {
                                    cardView.setFill(new ImagePattern(Card.UNKNOWN_CARD));
                                    cardView.setRotate(-90);
                                }
                            }
                            availableMonster.getChildren().add(cardView);
                        }
                        ButtonType destroy = new ButtonType("DESTROY!", ButtonBar.ButtonData.OK_DONE);
                        dialog.getDialogPane().getButtonTypes().add(destroy);
                        dialog.getDialogPane().setContent(availableMonster);
                        Optional<ButtonType> result = dialog.showAndWait();
                        if (result.isPresent() && result.get() == destroy) {
                            if (toDestroy[0] == null) return;
                            GameplayController.getInstance().destroyMonsterCard(Effect.gameplay.getOpponentPlayer(), toDestroy[0]);
                            dialog.hide();
                        }
                    };
                    break;
                case "Yomi Ship":
                    card.setHasEffect(true);
                    card.onDestruction = objects -> {
                        Player cardOwner = (Player) objects[0];
                        if (Effect.gameplay.getAttacker() == null) return;
                        if (cardOwner.equals(Effect.gameplay.getOpponentPlayer()))
                            GameplayController.getInstance().destroyMonsterCard(Effect.gameplay.getCurrentPlayer(), (MonsterFieldArea) Effect.gameplay.getAttacker());
                    };
                    break;
                case "Exploder Dragon":
                    card.setHasEffect(true);
                    card.onDestruction = objects -> {
                        Player cardOwner = (Player) objects[0];
                        if (cardOwner.equals(Effect.gameplay.getOpponentPlayer()))
                            GameplayController.getInstance().destroyMonsterCard(Effect.gameplay.getCurrentPlayer(), (MonsterFieldArea) Effect.gameplay.getAttacker());
                    };
                    card.onDamageCalculation = objects -> {
                        throw new ActionNotPossibleException("");
                    };
                    break;
                case "Marshmallon":
                    card.setHasEffect(true);
                    card.onDestruction = objects -> {
                        if (Effect.gameplay.getAttacker() != null)
                            throw new ActionNotPossibleException("this card cannot be destroyed");
                    };
                    card.afterDamageCalculation = objects -> {
                        boolean visibility = (boolean) objects[0];
                        if (!visibility)
                            Effect.gameplay.getCurrentPlayer().setLifePoints(Effect.gameplay.getCurrentPlayer().getLifePoints() - 1000);
                    };
                    break;
                case "Terratiger, The Empowered Warrior":
                    card.setHasEffect(true);
                    card.afterSummon = objects -> {
                        boolean hasMonster = false;
                        for (HandFieldArea hand :
                                Effect.gameplay.getCurrentPlayer().getPlayingHand()) {
                            if (hand.getCard() instanceof Monster && ((Monster) hand.getCard()).getLevel() <= 4) {
                                hasMonster = true;
                                break;
                            }
                        }
                        if (!hasMonster) throw new SpecialSummonNotPossibleException();
                        MonsterFieldArea monster = Effect.gameplay.getCurrentPlayer().getField().getFreeMonsterFieldArea();
                        if (monster == null) throw new MonsterZoneFullException();
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setTitle("Terratiger, the Empowered Warrior");
                        HBox handView = new HBox(5);
                        final HandFieldArea[] selectedHand = new HandFieldArea[1];
                        for (HandFieldArea handField :
                                Effect.gameplay.getCurrentPlayer().getPlayingHand()) {
                            if (!(handField.getCard() instanceof Monster) || ((Monster) handField.getCard()).getLevel() > 4)
                                continue;
                            Rectangle cardView = new Rectangle(70, 100, handField.getCard().getFill());
                            cardView.setOnMouseClicked(mouseEvent -> {
                                selectedHand[0] = handField;
                            });
                            handView.getChildren().add(cardView);
                        }
                        ButtonType destroy = new ButtonType("SPECIAL SUMMON!", ButtonBar.ButtonData.OK_DONE);
                        dialog.getDialogPane().getButtonTypes().add(destroy);
                        dialog.getDialogPane().setContent(handView);
                        Optional<ButtonType> result = dialog.showAndWait();
                        if (result.isPresent() && result.get() == destroy) {
                            if (selectedHand[0] == null) throw new SpecialSummonNotPossibleException();
                            Card toSummon = selectedHand[0].getCard();
                            monster.putCard(toSummon, false);
                        }
                    };
                    break;
                case "The Tricky":
                    card.setHasEffect(true);
                    card.uniqueSummon = () -> {
                        if (UniqueSummon.gameplay.getCurrentPlayer().getPlayingHand().size() == 1)
                            throw new SpecialSummonNotPossibleException();
                        FieldArea selected = UniqueSummon.gameplay.getSelectedField();
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setTitle("The Tricky Unique Summon");
                        HBox handView = new HBox(5);
                        for (HandFieldArea handField :
                                UniqueSummon.gameplay.getCurrentPlayer().getPlayingHand()) {
                            if (handField.equals(selected)) continue;
                            Rectangle cardView = new Rectangle(70, 100, handField.getCard().getFill());
                            cardView.setOnMouseClicked(mouseEvent -> {
                                UniqueSummon.gameplay.setSelectedField(handField);
                                UniqueSummon.gameplay.setOwnsSelectedCard(true);
                            });
                            handView.getChildren().add(cardView);
                        }
                        ButtonType destroy = new ButtonType("DISCARD!", ButtonBar.ButtonData.OK_DONE);
                        dialog.getDialogPane().getButtonTypes().add(destroy);
                        dialog.getDialogPane().setContent(handView);
                        Optional<ButtonType> result = dialog.showAndWait();
                        if (result.isPresent() && result.get() == destroy) {
                            try {
                                GameplayController.getInstance().discardSelectedCard();
                                UniqueSummon.gameplay.setSelectedField(selected);
                                UniqueSummon.gameplay.setOwnsSelectedCard(true);
                                dialog.hide();
                            } catch (InvalidCardSelectionException ignored) {
                            }
                        }
                    };
                    break;
                case "Gate Guardian":
                    card.setHasEffect(true);
                    card.uniqueSummon = () -> {
                        GameplayController.setGameState(GameState.TRIBUTE_SUMMON_MODE);
                        GameplayController.getInstance().tributeCount = 3;
                    };
                    break;
            }
        }
    }

    private void initializeSpellEffects() {
        for (Spell spell : Spell.getSpells()) {
            switch (spell.getName()) {
                case "United We Stand":
                    spell.equipEffect = new EquipEffect() {
                        int initialSum;

                        @Override
                        public void activate(MonsterFieldArea toEquipMonster) {
                            int count = 0;
                            for (MonsterFieldArea monster :
                                    GameplayController.getInstance().gameplay.getCurrentPlayer().getField().getMonstersField()) {
                                if (monster.isVisible()) count++;
                            }
                            initialSum = count * 800;
                            toEquipMonster.setAttackPoint(toEquipMonster.getAttackPoint() + initialSum);
                            toEquipMonster.setDefensePoint(toEquipMonster.getDefensePoint() + initialSum);
                        }

                        @Override
                        public void deactivate(MonsterFieldArea toDeequipMonster) {
                            toDeequipMonster.setAttackPoint(toDeequipMonster.getAttackPoint() - initialSum);
                            toDeequipMonster.setDefensePoint(toDeequipMonster.getDefensePoint() - initialSum);
                            if (toDeequipMonster.getAttackPoint() < 0) toDeequipMonster.setAttackPoint(0);
                            if (toDeequipMonster.getDefensePoint() < 0) toDeequipMonster.setDefensePoint(0);
                        }

                        @Override
                        public boolean isMonsterCorrect(MonsterFieldArea chosenMonster) {
                            return true;
                        }
                    };
                    break;
                case "Magnum Shield":
                    spell.equipEffect = new EquipEffect() {
                        boolean initialAttack = false;

                        @Override
                        public void activate(MonsterFieldArea toEquipMonster) {
                            if (toEquipMonster.isAttack()) {
                                toEquipMonster.setDefensePoint(toEquipMonster.getDefensePoint() + toEquipMonster.getAttackPoint());
                                initialAttack = true;
                            } else
                                toEquipMonster.setAttackPoint(toEquipMonster.getDefensePoint() + toEquipMonster.getAttackPoint());
                        }

                        @Override
                        public void deactivate(MonsterFieldArea toDeequipMonster) {
                            if (initialAttack)
                                toDeequipMonster.setDefensePoint(toDeequipMonster.getDefensePoint() - toDeequipMonster.getAttackPoint());
                            else
                                toDeequipMonster.setAttackPoint(toDeequipMonster.getAttackPoint() - toDeequipMonster.getDefensePoint());
                            if (toDeequipMonster.getAttackPoint() < 0) toDeequipMonster.setAttackPoint(0);
                            if (toDeequipMonster.getDefensePoint() < 0) toDeequipMonster.setDefensePoint(0);
                        }

                        @Override
                        public boolean isMonsterCorrect(MonsterFieldArea chosenMonster) {
                            return ((Monster) chosenMonster.getCard()).getMonsterType().equals(MonsterType.WARRIOR);
                        }
                    };
                    break;
                case "Black Pendant":
                    spell.equipEffect = new EquipEffect() {
                        @Override
                        public void activate(MonsterFieldArea toEquipMonster) {
                            toEquipMonster.setAttackPoint(toEquipMonster.getAttackPoint() + 500);
                        }

                        @Override
                        public void deactivate(MonsterFieldArea toDeequipMonster) {
                            toDeequipMonster.setAttackPoint(toDeequipMonster.getAttackPoint() - 500);
                            if (toDeequipMonster.getAttackPoint() < 0) toDeequipMonster.setAttackPoint(0);
                        }

                        @Override
                        public boolean isMonsterCorrect(MonsterFieldArea chosenMonster) {
                            return true;
                        }
                    };
                    break;
                case "Sword of dark destruction":
                    spell.equipEffect = new EquipEffect() {
                        @Override
                        public void activate(MonsterFieldArea toEquipMonster) {
                            toEquipMonster.setAttackPoint(toEquipMonster.getAttackPoint() + 400);
                            toEquipMonster.setDefensePoint(toEquipMonster.getDefensePoint() - 200);
                        }

                        @Override
                        public void deactivate(MonsterFieldArea toDeequipMonster) {
                            toDeequipMonster.setAttackPoint(toDeequipMonster.getAttackPoint() - 400);
                            toDeequipMonster.setDefensePoint(toDeequipMonster.getDefensePoint() + 200);
                            if (toDeequipMonster.getAttackPoint() < 0) toDeequipMonster.setAttackPoint(0);
                        }

                        @Override
                        public boolean isMonsterCorrect(MonsterFieldArea chosenMonster) {
                            if (chosenMonster.getAttackPoint() < 200) return false;
                            return ((Monster) chosenMonster.getCard()).getMonsterType().equals(MonsterType.FIEND) || ((Monster) chosenMonster.getCard()).getMonsterType().equals(MonsterType.SPELLCASTER);
                        }
                    };
                    break;
                case "Monster Reborn":
                    spell.spellEffect = objects -> {
                        if (Effect.gameplay.getCurrentPlayer().getField().getGraveyard().size() == 0 && Effect.gameplay.getOpponentPlayer().getField().getGraveyard().size() == 0)
                            throw new ActionNotPossibleException("you can't use this card effects");
                        if (Effect.gameplay.getCurrentPlayer().getField().getFreeMonsterFieldArea() == null)
                            throw new MonsterZoneFullException();
                        System.out.println("Please select a card from you or your opponent's graveyard:");
                        String input;
                        Card cardToSpecialSummon;
                        while (true) {
                            ArrayList<Card> graveyard = new ArrayList<>();
                            for (Card card : Effect.gameplay.getCurrentPlayer().getField().getGraveyard()) {
                                if (card instanceof Monster) graveyard.add(card);
                            }
                            for (Card card : Effect.gameplay.getOpponentPlayer().getField().getGraveyard()) {
                                if (card instanceof Monster) graveyard.add(card);
                            }
                            GraveyardView.showGraveyard(graveyard);
                            input = ProgramController.getInstance().getScanner().nextLine();
                            if (input.matches("\\d+")) {
                                if (Integer.parseInt(input) > graveyard.size())
                                    System.out.println("location invalid in this graveyard");
                                else {
                                    cardToSpecialSummon = graveyard.get(Integer.parseInt(input) - 1);
                                    graveyard.remove(Integer.parseInt(input) - 1);
                                    if (Effect.gameplay.getCurrentPlayer().getField().getGraveyard().contains(cardToSpecialSummon))
                                        Effect.gameplay.getCurrentPlayer().getField().getGraveyard().remove(cardToSpecialSummon);
                                    else
                                        Effect.gameplay.getOpponentPlayer().getField().getGraveyard().remove(cardToSpecialSummon);
                                    GameplayController.getInstance().specialSummon(cardToSpecialSummon);
                                    break;
                                }

                            }
                        }

                    };
                    break;

                case "Terraforming":
                    spell.spellEffect = objects -> {
                        boolean foundCard = false;
                        System.out.println("looking for field spell card to add to hand...");
                        for (Card card : Effect.gameplay.getCurrentPlayer().getPlayingDeck().getMainCards()) {
                            if (!(card instanceof SpellAndTrap)) continue;
                            if (((SpellAndTrap) card).getIcon().equals(Icon.FIELD)) {
                                HandFieldArea handFieldArea = new HandFieldArea(card);
                                Effect.gameplay.getCurrentPlayer().getPlayingHand().add(handFieldArea);
                                Effect.gameplay.getCurrentPlayer().getPlayingDeck().getMainCards().remove(card);
                                System.out.println("new card added to the hand : " + card.getName());
                                foundCard = true;
                                break;
                            }
                        }
                        if (!foundCard) System.out.println("couldn't find any field spell cards in deck");

                    };
                    break;

                case "Pot of Greed":
                    spell.spellEffect = objects -> {
                        Card card;
                        card = GameplayController.getInstance().drawCard();
                        System.out.println("new card added to the hand : " + card.getName());
                        card = GameplayController.getInstance().drawCard();
                        System.out.println("new card added to the hand : " + card.getName());
                    };
                    break;

                case "Raigeki":
                    spell.spellEffect = objects -> {
                        MonsterFieldArea[] opponentMonsters = Effect.gameplay.getOpponentPlayer().getField().getMonstersField();
                        for (int i = 0; i < 5; i++) {
                            if (opponentMonsters[i].getCard() != null)
                                GameplayController.getInstance().destroyMonsterCard(Effect.gameplay.getOpponentPlayer(), opponentMonsters[i]);
                        }
                    };
                    break;

                case "Harpie's Feather Duster":
                    spell.spellEffect = objects -> {
                        SpellAndTrapFieldArea[] opponentSpellZone = Effect.gameplay.getOpponentPlayer().getField().getSpellAndTrapField();
                        for (int i = 0; i < 5; i++) {
                            if (opponentSpellZone[i].getCard() != null)
                                GameplayController.getInstance().destroySpellAndTrapCard(Effect.gameplay.getOpponentPlayer(), opponentSpellZone[i]);
                        }
                    };
                    break;

                case "Dark Hole":
                    spell.spellEffect = objects -> {
                        MonsterFieldArea[] monsters = Effect.gameplay.getOpponentPlayer().getField().getMonstersField();
                        for (int i = 0; i < 5; i++) {
                            if (monsters[i].getCard() != null)
                                GameplayController.getInstance().destroyMonsterCard(Effect.gameplay.getOpponentPlayer(), monsters[i]);
                        }
                        monsters = Effect.gameplay.getCurrentPlayer().getField().getMonstersField();
                        for (int i = 0; i < 5; i++) {
                            if (monsters[i].getCard() != null)
                                GameplayController.getInstance().destroyMonsterCard(Effect.gameplay.getCurrentPlayer(), monsters[i]);
                        }
                    };
                    break;

                case "Twin Twisters":
                    spell.spellEffect = objects -> {
                        Matcher matcher;
                        SpellAndTrapFieldArea thisCardSpellAndTrap = null;
                        HandFieldArea thisCardHand = null;
                        if (GameplayController.getInstance().getGameplay().getSelectedField() instanceof SpellAndTrapFieldArea)
                            thisCardSpellAndTrap = (SpellAndTrapFieldArea) GameplayController.getInstance().getGameplay().getSelectedField();
                        else
                            thisCardHand = (HandFieldArea) GameplayController.getInstance().getGameplay().getSelectedField();
                        System.out.println("Discard one card from your own hand");
                        while (true) {
                            try {
                                GameplayView.getInstance().discardACard(thisCardHand);
                                break;
                            } catch (CardCantDiscardItselfException ignored) {
                            }
                        }
                        int counter = 0;
                        GameplayController.getInstance().deselectCard();
                        System.out.println("Select at most 2 spell and trap cards to destroy, or type done");
                        while (counter < 2) {
                            try {
                                String input = ProgramController.getInstance().getScanner().nextLine();
                                if ((matcher = Regex.getCommandMatcher(input, Regex.selectSpellCard)).matches()) {
                                    boolean isOpponent = (matcher.group("isOpponent") != null);
                                    GameplayController.getInstance().selectCard(matcher.group("id"), "--spell", isOpponent);
                                    if (GameplayController.getInstance().getGameplay().getSelectedField().getCard() == null)
                                        continue;
                                    if (GameplayController.getInstance().getGameplay().getSelectedField() == thisCardSpellAndTrap) {
                                        System.out.println("You can't discard this card");
                                        continue;
                                    }
                                    counter++;
                                    Player player = GameplayController.getInstance().getGameplay().getCurrentPlayer();
                                    if (isOpponent)
                                        player = GameplayController.getInstance().gameplay.getOpponentPlayer();
                                    GameplayController.getInstance().destroySpellAndTrapCard(player, (SpellAndTrapFieldArea) GameplayController.getInstance().getGameplay().getSelectedField());
                                } else if (input.matches("done")) break;
                                else if (input.matches(Regex.help))
                                    System.out.println("Select at most " + (2 - counter) + " spell and trap cards to destroy, or type done");
                                GameplayView.getInstance().showBoard();
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }

                    };
                    break;

                case "Mystical space typhoon":
                    spell.spellEffect = objects -> {
                        Matcher matcher;
                        SpellAndTrapFieldArea thisCard = null;
                        if (GameplayController.getInstance().getGameplay().getSelectedField() instanceof SpellAndTrapFieldArea)
                            thisCard = (SpellAndTrapFieldArea) GameplayController.getInstance().getGameplay().getSelectedField();
                        GameplayController.getInstance().deselectCard();
                        while (true) {
                            try {
                                String input = ProgramController.getInstance().getScanner().nextLine();
                                if ((matcher = Regex.getCommandMatcher(input, Regex.selectSpellCard)).matches()) {
                                    boolean isOpponent = (matcher.group("isOpponent") != null);
                                    GameplayController.getInstance().selectCard(matcher.group("id"), "--spell", isOpponent);
                                    Player player = GameplayController.getInstance().getGameplay().getCurrentPlayer();
                                    if (isOpponent)
                                        player = GameplayController.getInstance().gameplay.getOpponentPlayer();
                                    if (GameplayController.getInstance().getGameplay().getSelectedField().getCard() == null)
                                        continue;
                                    if (GameplayController.getInstance().getGameplay().getSelectedField() == thisCard) {
                                        System.out.println("You can't discard this card");
                                        continue;
                                    }
                                    GameplayController.getInstance().destroySpellAndTrapCard(player, (SpellAndTrapFieldArea) GameplayController.getInstance().getGameplay().getSelectedField());
                                    break;
                                } else if (input.matches(Regex.help))
                                    System.out.println("Select one spell and trap cards to destroy, or type done");
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    };
                    break;

                case "Yami":
                    spell.fieldZoneEffect = new ContinuousEffect() {
                        @Override
                        public void activate() {
                            for (MonsterFieldArea myField :
                                    GameplayController.getInstance().getGameplay().getCurrentPlayer().getField().getMonstersField()) {
                                if (myField.getCard() != null && (((Monster) myField.getCard()).getMonsterType().equals(MonsterType.FIEND)
                                                                  || ((Monster) myField.getCard()).getMonsterType().equals(MonsterType.SPELLCASTER))) {
                                    myField.setDefensePoint(myField.getDefensePoint() + 200);
                                    myField.setAttackPoint(myField.getAttackPoint() + 200);
                                } else if (myField.getCard() != null && ((Monster) myField.getCard()).getMonsterType().equals(MonsterType.FAIRY)) {
                                    myField.setDefensePoint(myField.getDefensePoint() - 200);
                                    myField.setAttackPoint(myField.getAttackPoint() - 200);
                                }
                            }
                            for (MonsterFieldArea opponentField :
                                    GameplayController.getInstance().getGameplay().getOpponentPlayer().getField().getMonstersField()) {
                                if (opponentField.getCard() != null && (((Monster) opponentField.getCard()).getMonsterType().equals(MonsterType.FIEND)
                                                                        || ((Monster) opponentField.getCard()).getMonsterType().equals(MonsterType.SPELLCASTER))) {
                                    opponentField.setDefensePoint(opponentField.getDefensePoint() + 200);
                                    opponentField.setAttackPoint(opponentField.getAttackPoint() + 200);
                                } else if (opponentField.getCard() != null && ((Monster) opponentField.getCard()).getMonsterType().equals(MonsterType.FAIRY)) {
                                    opponentField.setDefensePoint(opponentField.getDefensePoint() - 200);
                                    opponentField.setAttackPoint(opponentField.getAttackPoint() - 200);
                                }
                            }
                        }
                    };
                    break;

                case "Forest":
                    spell.fieldZoneEffect = new ContinuousEffect() {
                        @Override
                        public void activate() {
                            for (MonsterFieldArea myField :
                                    GameplayController.getInstance().getGameplay().getCurrentPlayer().getField().getMonstersField()) {
                                if (myField.getCard() != null && (((Monster) myField.getCard()).getMonsterType().equals(MonsterType.INSECT)
                                                                  || ((Monster) myField.getCard()).getMonsterType().equals(MonsterType.BEAST)
                                                                  || ((Monster) myField.getCard()).getMonsterType().equals(MonsterType.BEAST_WARRIOR))) {
                                    myField.setDefensePoint(myField.getDefensePoint() + 200);
                                    myField.setAttackPoint(myField.getAttackPoint() + 200);
                                }
                            }
                            for (MonsterFieldArea opponentField :
                                    GameplayController.getInstance().getGameplay().getOpponentPlayer().getField().getMonstersField()) {
                                if (opponentField.getCard() != null && (((Monster) opponentField.getCard()).getMonsterType().equals(MonsterType.INSECT)
                                                                        || ((Monster) opponentField.getCard()).getMonsterType().equals(MonsterType.BEAST)
                                                                        || ((Monster) opponentField.getCard()).getMonsterType().equals(MonsterType.BEAST_WARRIOR))) {
                                    opponentField.setDefensePoint(opponentField.getDefensePoint() + 200);
                                    opponentField.setAttackPoint(opponentField.getAttackPoint() + 200);
                                }
                            }
                        }
                    };
                    break;

                case "Closed Forest":
                    spell.fieldZoneEffect = new ContinuousEffect() {
                        @Override
                        public void activate() {
                            int counter = 0;
                            for (Card card : GameplayController.getInstance().getGameplay().getCurrentPlayer().getField().getGraveyard()) {
                                if (card instanceof Monster) counter++;
                            }
                            for (MonsterFieldArea myField :
                                    GameplayController.getInstance().getGameplay().getCurrentPlayer().getField().getMonstersField()) {
                                if (myField.getCard() != null && (((Monster) myField.getCard()).getMonsterType().equals(MonsterType.BEAST)
                                                                  || ((Monster) myField.getCard()).getMonsterType().equals(MonsterType.BEAST_WARRIOR))) {
                                    myField.setDefensePoint(myField.getDefensePoint() + 100 * counter);
                                    myField.setAttackPoint(myField.getAttackPoint() + 100 * counter);
                                }
                            }
                        }
                    };
                    break;

                case "Umiiruka":
                    spell.fieldZoneEffect = new ContinuousEffect() {
                        @Override
                        public void activate() {
                            for (MonsterFieldArea myField :
                                    GameplayController.getInstance().getGameplay().getCurrentPlayer().getField().getMonstersField()) {
                                if (myField.getCard() != null && ((Monster) myField.getCard()).getMonsterType().equals(MonsterType.AQUA)) {
                                    myField.setAttackPoint(myField.getAttackPoint() + 500);
                                    myField.setDefensePoint(myField.getDefensePoint() - 400);
                                }
                            }
                            for (MonsterFieldArea opponentField :
                                    GameplayController.getInstance().getGameplay().getOpponentPlayer().getField().getMonstersField()) {
                                if (opponentField.getCard() != null && ((Monster) opponentField.getCard()).getMonsterType().equals(MonsterType.AQUA)) {
                                    opponentField.setAttackPoint(opponentField.getAttackPoint() + 500);
                                    opponentField.setDefensePoint(opponentField.getDefensePoint() - 400);
                                }
                            }
                        }
                    };
                    break;
            }
        }
    }

    private boolean isLimited(String string) {
        if (string.matches("Limited")) return true;
        return false;
    }

    private Icon getIcon(String name) {
        name = name.toUpperCase();
        for (int i = 0; i < Icon.values().length; i++) {
            if (name.matches(Icon.values()[i].toString())) return Icon.values()[i];
        }
        return null;
    }

    private Attribute getAttribute(String name) {
        name = name.toUpperCase();
        for (int i = 0; i < Attribute.values().length; i++) {
            if (name.matches(Attribute.values()[i].toString())) return Attribute.values()[i];
        }
        return null;
    }

    private MonsterType getMonsterType(String name) {
        name = name.toUpperCase();
        for (int i = 0; i < MonsterType.values().length; i++) {
            if (name.matches(MonsterType.values()[i].toString())) return MonsterType.values()[i];
        }
        return null;
    }

    private CardType getCardType(String name) {
        name = name.toUpperCase();
        for (int i = 0; i < CardType.values().length; i++) {
            if (name.matches(CardType.values()[i].toString())) return CardType.values()[i];
        }
        return null;
    }

    public void createUserFromEffUser(EfficientUser tempUser) {
        System.out.println(tempUser.getAvatarID());
        ArrayList<Deck> actualDecks = new ArrayList<>();
        for (EfficientDeck deckName : tempUser.getDecks()) {
            actualDecks.add(createDeckFromStringArray(deckName.getName(), deckName.isActive(), deckName.getMainCards(), deckName.getSideCards()));
        }
        ArrayList<Card> actualInactiveCards = new ArrayList<>();
        for (String cardName : tempUser.getInactiveCards())
            actualInactiveCards.add(Card.getCardByName(cardName));
        new User(tempUser.getUsername(), tempUser.getPassword(), tempUser.getNickname(), tempUser.getAvatarID(), tempUser.getScore(),
                tempUser.getBalance(), actualDecks, actualInactiveCards);
    }

    private Deck createDeckFromStringArray(String deckName, boolean isActive, ArrayList<String> mainCards, ArrayList<String> sideCards) {
        Deck deck = new Deck(deckName, isActive);
        for (String cardName : mainCards) {
            deck.getMainCards().add(Card.getCardByName(cardName));
        }
        for (String cardName : sideCards) {
            deck.getSideCards().add(Card.getCardByName(cardName));
        }
        return deck;
    }

    public void saveAllUsers() {
        for (User user : User.getUsers()) {
            saveUser(user);
        }
    }
}
