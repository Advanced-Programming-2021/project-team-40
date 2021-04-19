package Database.Cards;

abstract public class SpellAndTrap extends Card{

    protected Icon icon;

    public SpellAndTrap(Icon icon, String name, String description, String cardNumber, int cardPrice){
        super(name, description, cardNumber, cardPrice);
        setIcon(icon);
        allCards.add(this);
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Icon getIcon() {
        return icon;
    }

    public void activateSpellOrTrap(){

    }
}
