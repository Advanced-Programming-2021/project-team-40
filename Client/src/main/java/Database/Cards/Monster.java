package Database.Cards;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import java.util.ArrayList;

public class Monster extends Card {

    private Attribute attribute;
    private MonsterType monsterType;
    private CardType cardType;
    private int level;
    private int attackPoints;
    private int defensePoints;
    private static ArrayList<Monster> monsters = new ArrayList<>();

    public Monster(String name, int level, Attribute attribute, MonsterType monsterType, CardType cardType,
                   int attackPoints, int defensePoints,
                   String description, int cardPrice) {
        super(name, description, cardPrice);
        String temp = name.replaceAll("\\s","");
        setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/Database/Cards/Monsters/" + temp + ".jpg"))));
        setAttribute(attribute);
        setMonsterType(monsterType);
        setCardType(cardType);
        setLevel(level);
        setAttackPoints(attackPoints);
        setDefensePoints(defensePoints);

        monsters.add(this);
    }

    public int getNumberOfTributes() {
        if (level < 5) return 0;
        if (level < 7) return 1;
        return 2;
    }

    public static ArrayList<Monster> getMonsters() {
        return monsters;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setMonsterType(MonsterType monsterType) {
        this.monsterType = monsterType;
    }

    public MonsterType getMonsterType() {
        return monsterType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setAttackPoints(int attackPoints) {
        this.attackPoints = attackPoints;
    }

    public int getAttackPoints() {
        return attackPoints;
    }

    public void setDefensePoints(int defensePoints) {
        this.defensePoints = defensePoints;
    }

    public int getDefensePoints() {
        return defensePoints;
    }


}
