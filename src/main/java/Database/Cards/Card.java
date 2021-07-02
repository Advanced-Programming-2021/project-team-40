package Database.Cards;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.*;

abstract public class Card extends Rectangle {
    private static final int CARD_WIDTH = 0;
    private static final int CARD_HEIGHT = 0;
    public Effect onDestruction;//player
    public Effect onFlipSummon;//null
    public Effect afterDamageCalculation;//player
    public Effect afterSummon;
    public Effect onDamageCalculation;//player,damage,attack message,return int
    public Effect onBeingAttacked;//player
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
//        super(CARD_WIDTH,CARD_HEIGHT,new ImagePattern(new Image()));
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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCardPrice(int cardPrice) {
        this.cardPrice = cardPrice;
    }

    public int getCardPrice() {
        return cardPrice;
    }

    public static ArrayList<Card> getAllCards() {
        ArrayList<Card> allCards = new ArrayList<>();
        allCards.addAll(Monster.getMonsters());
        allCards.addAll(Trap.getTraps());
        allCards.addAll(Spell.getSpells());
        return allCards;
    }

    public static Comparator<Card> cardNameComparator = new Comparator<>() {
        @Override
        public int compare(Card firstCard, Card secondCard) {
            String first = firstCard.getName().toUpperCase();
            String second = secondCard.getName().toUpperCase();
            return first.compareTo(second);
        }
    };

    public boolean isHasEffect() {
        return hasEffect;
    }

    public void setHasEffect(boolean hasEffect) {
        this.hasEffect = hasEffect;
    }
}
