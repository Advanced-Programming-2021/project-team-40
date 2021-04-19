package Cards;

abstract public class SpellAndTrap extends Card{
    enum Icon{
        EQUIP, FIELD, QUICK_PLAY, RITUAL, CONTINUOUS, COUNTER, NORMAL;
    }

    enum SpellAndTrapType{
        SPELL, TRAP;
    }
    private Icon icon;
    private SpellAndTrapType spellAndTrapType;

    public SpellAndTrap(Icon icon, SpellAndTrapType spellAndTrapType,
                        String name, String description, String cardNumber, int cardPrice){
        super(name, description, cardNumber, cardPrice);
        setIcon(icon);
        setSpellAndTrapType(spellAndTrapType);
        allCards.add(this);
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setSpellAndTrapType(SpellAndTrapType spellAndTrapType) {
        this.spellAndTrapType = spellAndTrapType;
    }

    public SpellAndTrapType getSpellAndTrapType() {
        return spellAndTrapType;
    }

    public void activateSpellOrTrap(){

    }
}
