package Database.Cards;

public class Monster extends Card {

    private Attribute attribute;
    private MonsterType monsterType;
    private CardType cardType;
    private int level;
    private int attackPoints;
    private int defensePoints;

    public Monster(String name, int level, Attribute attribute, MonsterType monsterType, CardType cardType,
                   int attackPoints, int defensePoints,
                   String description, int cardPrice) {
        super(name, description, cardPrice);
        setAttribute(attribute);
        setMonsterType(monsterType);
        setCardType(cardType);
        setLevel(level);
        setAttackPoints(attackPoints);
        setDefensePoints(defensePoints);
        allCards.add(this);
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

    public void activateEffect() {

    }

}
