package Database.Cards;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

abstract public class SpellAndTrap extends Card {

    protected Icon icon;
    protected boolean isLimited;

    public SpellAndTrap(String name, Icon icon, String description, boolean isLimited, int cardPrice) {
        super(name, description, cardPrice);
        String temp = name.replaceAll("\\s","");
        setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/Database/Cards/SpellTrap/" + temp + ".jpg"))));
        setIcon(icon);
        setLimited(isLimited);
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setLimited(boolean limited) {
        isLimited = limited;
    }

    public boolean isLimited() {
        return isLimited;
    }

    public void activateSpellOrTrap() {

    }
}
