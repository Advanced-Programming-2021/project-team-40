package Cards;

public class Monster extends Card {

    enum SummonType {
        NORMAL_SUMMON, RITUAL_SUMMON, SPECIAL_SUMMON, TRIBUTE_SUMMON;
    }

    enum MonsterType {
        WARRIOR, WARRIOR_EFFECT, WARRIOR_RITUAL, INSECT_EFFECT, BEAST, BEAST_WARRIOR, BEAST_WARRIOR_EFFECT, FIEND,
        AQUA_EFFECT, AQUA_RITUAL, SEA_SERPENT, SPELLCASTER, SPELLCASTER_EFFECT, THUNDER, THUNDER_EFFECT, DRAGON,
        DRAGON_EFFECT, MACHINE, MACHINE_EFFECT, PYRO, ROCK, CYBERSE, CYBERSE_EFFECT, FAIRY_EFFECT;
    }

    enum Attribute {
        DARK, EARTH, FIRE, WATER, LIGHT, WIND;
    }

    enum EffectType {
        NORMAL, QUICK, TRIGGER, IGNITION, CONTINUOUS, NONE;
    }

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

    public void activateEffect(){

    }

    @Override
    public void printCard() {
        System.out.println("PRINT MONSTER CARDS HERE");
    }
}
