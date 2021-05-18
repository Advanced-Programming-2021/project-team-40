package Controller.DatabaseController;

import Controller.DuelController.GameplayController;
import Controller.ProgramController.ProgramController;
import Controller.ProgramController.Regex;
import Database.Cards.*;
import Database.Deck;
import Database.EfficientDeck;
import Database.EfficientUser;
import Database.User;
import Gameplay.*;
import View.Exceptions.InvalidCardSelectionException;
import View.Exceptions.MonsterZoneFullException;
import View.Exceptions.NoCardFoundException;
import View.Exceptions.SpecialSummonNotPossibleException;
import View.GameplayView;
import View.GraveyardView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.ToDoubleBiFunction;
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
        initializeUsers();
    }

    public void saveUser(User user) {
        File userFile = new File("./src/main/resources/Users/" + user.getUsername() + ".json");
        EfficientUser efficientUser = new EfficientUser(user);
        try {
            userFile.createNewFile();
            Gson gson = new GsonBuilder().create();
            String writeToFile = gson.toJson(efficientUser);
            FileWriter fw = new FileWriter(userFile);
            BufferedWriter out = new BufferedWriter(fw);
            out.write(writeToFile);
            out.close();
            fw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void initializeSpellAndTrapCards() {
        try {
            File spellAndTrapCards = new File("./src/main/resources/Cards/SpellTrap.csv");
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
    }

    private void initializeMonsterCards() {
        try {
            File monsterCards = new File("./src/main/resources/Cards/Monster.csv");
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

    private void initializeMonsterCardEffects() {
        //TODO add effects to cards
        Gameplay gameplay = GameplayController.getInstance().getGameplay();
        for (Card card :
                Card.getAllCards()) {
            switch (card.getName()) {
                case "Yomi Ship":
                    card.setHasEffect(true);
                    card.onDestruction = new Effect() {
                        @Override
                        public void execute(Object... objects) {
                            Player cardOwner = (Player) objects[0];
                            if (cardOwner.equals(gameplay.getOpponentPlayer()))
                                GameplayController.getInstance().destroyMonsterCard(gameplay.getCurrentPlayer(), (MonsterFieldArea) gameplay.getAttacker());
                        }
                    };
                    break;
                case "Man-Eater Bug":
                    card.setHasEffect(true);
                    card.onFlipSummon = new Effect() {
                        @Override
                        public void execute(Object... objects) {//TODO: implement
                            System.out.println("please enter a monster id");
                            Player player = GameplayController.getInstance().getGameplay().getOpponentPlayer();
                            String idToCheck;
                            while (true) {
                                idToCheck = ProgramController.getInstance().getScanner().nextLine();
                                try {
                                    if (GameplayController.getInstance().getMonsterFieldCount(player) == 0)
                                        throw new NoCardFoundException();
                                    if (GameplayController.getInstance().isLocationNumberInvalid(idToCheck))
                                        throw new InvalidCardSelectionException();
                                    int id = Integer.parseInt(idToCheck) - 1;
                                    MonsterFieldArea monster = player.getField().getMonstersFieldById(id);
                                    GameplayController.getInstance().destroyMonsterCard(player, monster);
                                    break;
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        }
                    };
                    break;
                case "Suijin":
                    card.setHasEffect(true);
                    card.onBeingAttacked = new Effect() {
                        @Override
                        public void execute(Object... objects) {
                            String command;
                            while (true) {
                                System.out.println("do you want to activate Suijin?");
                                command = ProgramController.getInstance().getScanner().nextLine();
                                if (command.equalsIgnoreCase("yes")) {
                                    ((MonsterFieldArea) gameplay.getAttacker()).setAttackPoint(0);
                                    (gameplay.getBeingAttacked()).setEffectAvailable(false);
                                    //TODO:
                                    // return after one turn
                                    // implement
                                    break;
                                } else if (command.equalsIgnoreCase("no")) break;
                                else System.out.println("invalid command");
                            }
                        }
                    };
                    break;
                case "Exploder Dragon":
                    card.setHasEffect(true);
                    card.onDestruction = new Effect() {
                        @Override
                        public void execute(Object... objects) {
                            Player cardOwner = (Player) objects[0];
                            if (cardOwner.equals(gameplay.getOpponentPlayer()))
                                GameplayController.getInstance().destroyMonsterCard(gameplay.getCurrentPlayer(), (MonsterFieldArea) gameplay.getAttacker());
                        }
                    };
                    card.onDamageCalculation = new Effect() {
                        @Override
                        public void execute(Object... objects) {

                        }
                    };
                    break;
                case "Marshmallon":
                    card.setHasEffect(true);
                    card.onDestruction = new Effect() {
                        @Override
                        public void execute(Object... objects) throws Exception {
                            if (gameplay.getAttacker() != null) throw new Exception("this card can't be destroyed");
                        }
                    };
                    card.afterDamageCalculation = new Effect() {
                        @Override
                        public void execute(Object... objects) {
                            boolean visibility = (boolean) objects[0];
                            if (!visibility)
                                gameplay.getCurrentPlayer().setLifePoints(gameplay.getCurrentPlayer().getLifePoints() - 1000);
                        }
                    };
                    break;
                case "Terratiger, the Empowered Warrior":
                    card.setHasEffect(true);
                    card.afterSummon = new Effect() {
                        @Override
                        public void execute(Object... objects) throws Exception {
                            boolean hasMonster = false;
                            for (HandFieldArea hand :
                                    gameplay.getCurrentPlayer().getPlayingHand()) {
                                if (hand.getCard() instanceof Monster) {
                                    hasMonster = true;
                                    break;
                                }
                            }
                            if (!hasMonster) throw new SpecialSummonNotPossibleException();
                            System.out.println("now you can choose one level 4 or lower monster from your hand to summon");
                            System.out.println("please enter a hand field id:");
                            String input;
                            while (true) {
                                input = ProgramController.getInstance().getScanner().nextLine();
                                try {
                                    //TODO check id validity
                                    int id = Integer.parseInt(input);
                                    Card temp = gameplay.getCurrentPlayer().getPlayingHand().get(id).getCard();
                                    if (temp.isHasEffect() || ((Monster) temp).getLevel() > 4)
                                        throw new InvalidCardSelectionException();
                                    MonsterFieldArea monster = gameplay.getCurrentPlayer().getField().getFreeMonsterFieldArea();
                                    if (monster == null) throw new MonsterZoneFullException();
                                    monster.putCard(temp, false);
                                } catch (InvalidCardSelectionException e) {
                                    System.out.println(e.getMessage());
                                } catch (MonsterZoneFullException e) {
                                    System.out.println(e.getMessage());
                                    break;
                                }
                            }
                        }
                    };
                    break;
                case "The Tricky":
                    card.uniqueSummon = new UniqueSummon() {
                        @Override
                        public void summon() throws Exception {
                            if (gameplay.getCurrentPlayer().getPlayingHand().size() == 0)
                                throw new SpecialSummonNotPossibleException();
                            System.out.println("discard one card from your hand to special summon this card:");
                            System.out.println("please enter a hand field id:");
                            String input;
                            while (true) {
                                //TODO check id validity
                                input = ProgramController.getInstance().getScanner().nextLine();
                                if (input.matches(Regex.cancelAction)) {
                                    System.out.println("operation cancelled");
                                    break;
                                }
                                int id = Integer.parseInt(input);
                                Card toDiscard = gameplay.getCurrentPlayer().getPlayingHand().get(id).getCard();
                                gameplay.getCurrentPlayer().getPlayingHand().remove(id);
                                GameplayController.getInstance().moveCardToGraveyard(gameplay.getCurrentPlayer(), toDiscard);
                            }
                        }
                    };
                    break;
            }
        }
    }

    private void initializeSpellEffects() {
        for (Spell spell : Spell.getSpells()) {
            switch (spell.getName()) {
                case "Monster Reborn":
                    spell.spellEffect = new Effect() {
                        @Override
                        public void execute(Object... objects) throws Exception {
                            if (gameplay.getCurrentPlayer().getField().getGraveyard().size() == 0 && gameplay.getOpponentPlayer().getField().getGraveyard().size() == 0)
                                throw new Exception("you can't use this card's effect");
                            if (gameplay.getCurrentPlayer().getField().getFreeMonsterFieldArea() == null)
                                throw new MonsterZoneFullException();
                            System.out.println("Please select a card from you or your opponent's graveyard:");
                            String input;
                            Card cardToSpecialSummon;
                            while (true) {
                                Matcher matcher;
                                ArrayList<Card> graveyard = new ArrayList<>();
                                input = ProgramController.getInstance().getScanner().nextLine();
                                for (Card card:gameplay.getCurrentPlayer().getField().getGraveyard()) {
                                    if (card instanceof Monster) graveyard.add(card);
                                }
                                for (Card card:gameplay.getOpponentPlayer().getField().getGraveyard()) {
                                    if (card instanceof Monster) graveyard.add(card);
                                }
                                GraveyardView.showGraveyard(graveyard);
                                if (input.matches("\\d+")) {
                                    if (Integer.parseInt(input) > graveyard.size())
                                        System.out.println("location invalid in this graveyard");
                                    else {
                                        cardToSpecialSummon = graveyard.get(Integer.parseInt(input) - 1);
                                        graveyard.remove(Integer.parseInt(input) - 1);
                                        if (gameplay.getCurrentPlayer().getField().getGraveyard().contains(cardToSpecialSummon))
                                            gameplay.getCurrentPlayer().getField().getGraveyard().remove(cardToSpecialSummon);
                                        else gameplay.getOpponentPlayer().getField().getGraveyard().remove(cardToSpecialSummon);
                                        GameplayController.getInstance().specialSummon(cardToSpecialSummon);
                                        break;
                                    }

                                }
                            }

                        }
                    };
                    break;

                case "Terraforming":
                    spell.spellEffect = new Effect() {
                        @Override
                        public void execute(Object... objects) throws Exception {
                            boolean foundCard = false;
                            System.out.println("looking for field spell card to add to hand...");
                            for (Card card : gameplay.getCurrentPlayer().getPlayingDeck().getMainCards()) {
                                if (((SpellAndTrap) card).getIcon().equals(Icon.FIELD)) {
                                    HandFieldArea handFieldArea = new HandFieldArea(card);
                                    gameplay.getCurrentPlayer().getPlayingHand().add(handFieldArea);
                                    gameplay.getCurrentPlayer().getPlayingDeck().getMainCards().remove(card);
                                    System.out.println("new card added to the hand : " + card.getName());
                                    foundCard = true;
                                    break;
                                }
                            }
                            if (!foundCard) System.out.println("couldn't find any field spell cards in deck");

                        }
                    };
                    break;

                case "Pot of Greed":
                    spell.spellEffect = new Effect() {
                        @Override
                        public void execute(Object... objects) throws Exception {
                            Card card;
                            card = GameplayController.getInstance().drawCard();
                            System.out.println("new card added to the hand : " + card.getName());
                            card = GameplayController.getInstance().drawCard();
                            System.out.println("new card added to the hand : " + card.getName());
                        }
                    };
                    break;

                case "Raigeki":
                    spell.spellEffect = new Effect() {
                        @Override
                        public void execute(Object... objects) throws Exception {
                            MonsterFieldArea[] opponentMonsters = gameplay.getOpponentPlayer().getField().getMonstersField();
                            for (int i = 0; i < 5; i++) {
                                if (opponentMonsters[i].getCard() != null)
                                    GameplayController.getInstance().destroyMonsterCard(gameplay.getOpponentPlayer(), opponentMonsters[i]);
                            }
                        }
                    };
                    break;

                case "Change of Heart":
                    //TODO
                    break;

                case "Harpie's Feather Duster":
                    spell.spellEffect = new Effect() {
                        @Override
                        public void execute(Object... objects) throws Exception {
                            SpellAndTrapFieldArea[] opponentSpellZone = gameplay.getOpponentPlayer().getField().getSpellAndTrapField();
                            for (int i = 0; i < 5; i++) {
                                if (opponentSpellZone[i].getCard() != null)
                                    GameplayController.getInstance().destroySpellAndTrapCard(gameplay.getOpponentPlayer(), opponentSpellZone[i]);
                            }
                        }
                    };
                    break;

                case "Swords of Revealing Light":
                    //TODO
                    break;

                case "Dark Hole":
                    spell.spellEffect = new Effect() {
                        @Override
                        public void execute(Object... objects) throws Exception {
                            MonsterFieldArea[] monsters = gameplay.getOpponentPlayer().getField().getMonstersField();
                            for (int i = 0; i < 5; i++) {
                                if (monsters[i].getCard() != null)
                                    GameplayController.getInstance().destroyMonsterCard(gameplay.getOpponentPlayer(), monsters[i]);
                            }
                            monsters = gameplay.getCurrentPlayer().getField().getMonstersField();
                            for (int i = 0; i < 5; i++) {
                                if (monsters[i].getCard() != null)
                                    GameplayController.getInstance().destroyMonsterCard(gameplay.getCurrentPlayer(), monsters[i]);
                            }
                        }
                    };
                    break;

                case "Supply Squad":
                    //TODO
                    break;

                case "Spell Absorption":
                    //TODO
                    break;

                case "Messenger of peace":
                    //TODO
                    break;

                case "Twin Twisters":
                    spell.spellEffect = new Effect() {
                        @Override
                        public void execute(Object... objects) throws Exception {
                            Matcher matcher;
                            GameplayController.getInstance().discardACard();
                            int counter = 0;
                            GameplayController.getInstance().deselectCard();
                            while (counter < 2) {
                                try {
                                    String input = ProgramController.getInstance().getScanner().nextLine();
                                    if ((matcher = Regex.getCommandMatcher(input, Regex.selectSpellCard)).matches()) {
                                        boolean isOpponent = (matcher.group("isOpponent") != null);
                                        GameplayController.getInstance().selectCard(matcher.group("id"), "--spell", isOpponent);
                                        if (GameplayController.getInstance().getGameplay().getSelectedField().getCard() == null)
                                            continue;
                                        counter++;
                                        Player player = GameplayController.getInstance().getGameplay().getCurrentPlayer();
                                        if (isOpponent)
                                            player = GameplayController.getInstance().gameplay.getOpponentPlayer();
                                        GameplayController.getInstance().destroySpellAndTrapCard(player, (SpellAndTrapFieldArea) GameplayController.getInstance().getGameplay().getSelectedField());
                                    } else if (input.matches("done")) break;
                                    else if (input.matches(Regex.help))
                                        System.out.println("Select at most " + (2 - counter) + " spell and trap cards to destroy, or type done");
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            }

                        }
                    };
                    break;

                case "Mystical space typhoon":
                    spell.spellEffect = new Effect() {
                        @Override
                        public void execute(Object... objects) throws Exception {
                            Matcher matcher;
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
                                        GameplayController.getInstance().destroySpellAndTrapCard(player, (SpellAndTrapFieldArea) GameplayController.getInstance().getGameplay().getSelectedField());
                                        break;
                                    } else if (input.matches(Regex.help))
                                        System.out.println("Select one spell and trap cards to destroy, or type done");
                                } catch (Exception e) {
                                    e.getMessage();
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
        for (int i = 0; i < MonsterType.values().length; i++) {
            if (name.matches(MonsterType.values()[i].toString())) return CardType.values()[i];
        }
        return null;
    }

    private void initializeUsers() {
        File userDirectory = new File("./src/main/resources/Users");
        userDirectory.mkdir();
        if (userDirectory.listFiles() == null) return;
        for (File userFile : userDirectory.listFiles()) {
            try {
                Scanner fileScanner = new Scanner(userFile);
                String userJson = fileScanner.nextLine();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                EfficientUser tempUser = gson.fromJson(userJson, EfficientUser.class);
                ArrayList<Deck> actualDecks = new ArrayList<>();
                for (EfficientDeck deckName : tempUser.getDecks()) {
                    actualDecks.add(createDeckFromStringArray(deckName.getName(), deckName.isActive(), deckName.getMainCards(), deckName.getSideCards()));
                }
                ArrayList<Card> actualInactiveCards = new ArrayList<>();
                for (String cardName : tempUser.getInactiveCards())
                    actualInactiveCards.add(Card.getCardByName(cardName));
                new User(tempUser.getUsername(), tempUser.getPassword(), tempUser.getNickname(), tempUser.getScore(),
                        tempUser.getBalance(), actualDecks, actualInactiveCards);
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
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

}
