package main.java.Database.Cards;

public class Monster extends Card {

    private Attribute attribute;
    private MonsterType monsterType;
    private SummonType summonType;
    private EffectType effectType;
    private int level;
    private int attackPoints;
    private int defensePoints;

    public Monster(Attribute attribute, MonsterType monsterType, SummonType summonType, EffectType effectType,
                   int level, int attackPoints, int defensePoints,
                   String name, String description, String cardNumber, int cardPrice) {
        super(name, description, cardNumber, cardPrice);
        setAttribute(attribute);
        setMonsterType(monsterType);
        setSummonType(summonType);
        setEffectType(effectType);
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

    public void setSummonType(SummonType summonType) {
        this.summonType = summonType;
    }

    public SummonType getSummonType() {
        return summonType;
    }

    public void setEffectType(EffectType effectType) {
        this.effectType = effectType;
    }

    public EffectType getEffectType() {
        return effectType;
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

    @Override
    public void printCard() {
        System.out.println("PRINT MONSTER CARDS HERE");
    }
}
