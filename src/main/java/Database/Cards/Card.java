package Database.Cards;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Comparator;

abstract public class Card extends Rectangle {
    private static final int CARD_WIDTH = 105;
    private static final int CARD_HEIGHT = 150;
    public static Image UNKNOWN_CARD = new Image(Card.class.getResourceAsStream("/Database/Cards/Monsters/Unknown.jpg"));
    public static Comparator<Card> cardNameComparator = new Comparator<>() {
        @Override
        public int compare(Card firstCard, Card secondCard) {
            String first = firstCard.getName().toUpperCase();
            String second = secondCard.getName().toUpperCase();
            return first.compareTo(second);
        }
    };
    public Effect onDestruction;
    public Effect onFlipSummon;
    public Effect afterDamageCalculation;
    public Effect afterSummon;
    public Effect onDamageCalculation;
    public Effect onBeingAttacked;
    public ContinuousEffect onTurnStart;
    public UniqueSummon uniqueSummon;
    public Effect onSummon;
    public Effect onSpellActivation;
    public Effect inStandbyPhase;
    public EquipEffect equipEffect;
    protected String name;
    protected String description;
    protected int cardPrice = 0;
    private boolean hasEffect = false;

    public Card(String name, String description, int cardPrice) {
        super(CARD_WIDTH, CARD_HEIGHT);
        setName(name);
        setDescription(description);
        setCardPrice(cardPrice);
    }

    public static Card getCardByName(String name) {
        for (Card card : getAllCards()) {
            if (card.name.matches(name)) return card;
        }
        return null;
    }

    public static ArrayList<Card> getAllCards() {
        ArrayList<Card> allCards = new ArrayList<>();
        allCards.addAll(Monster.getMonsters());
        allCards.addAll(Trap.getTraps());
        allCards.addAll(Spell.getSpells());
        return allCards;
    }

    public static int getCardHeight() {
        return CARD_HEIGHT;
    }

    public static int getCardWidth() {
        return CARD_WIDTH;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCardPrice() {
        return cardPrice;
    }

    public void setCardPrice(int cardPrice) {
        this.cardPrice = cardPrice;
    }

    public boolean isHasEffect() {
        return hasEffect;
    }

    public void setHasEffect(boolean hasEffect) {
        this.hasEffect = hasEffect;
    }
}
