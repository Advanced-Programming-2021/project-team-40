package Gameplay;

import Database.Cards.Card;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class FieldArea extends VBox {
    private static final double FIELD_AREA_WIDTH = 75;
    private static final double FIELD_AREA_HEIGHT = 75;
    protected boolean canBePutOnBoard = false;
    protected Card card;
    protected Rectangle cardView = new Rectangle(52.5, 75);
    protected boolean visibility;
    private boolean isEffectAvailable = true;

    public FieldArea() {
        super();
        super.setPrefSize(FIELD_AREA_WIDTH, FIELD_AREA_HEIGHT);
        getCardView().setFill(Color.TRANSPARENT);
        getChildren().add(getCardView());
    }

    public static double getFieldAreaWidth() {
        return FIELD_AREA_WIDTH;
    }

    public static double getFieldAreaHeight() {
        return FIELD_AREA_HEIGHT;
    }

    public Rectangle getCardView() {
        return cardView;
    }

    public Card getCard() {
        return card;
    }

    public void putCard(Card card, boolean visibility) {
        this.card = card;
        this.visibility = visibility;
        if (card == null) {
            cardView.setFill(Color.TRANSPARENT);
            return;
        }
        cardView.setFill(card.getFill());
        if (this instanceof HandFieldArea) return;
        if (!visibility) {
            getCardView().setFill(new ImagePattern(Card.UNKNOWN_CARD));
            getCardView().setRotate(-90);
        }
        else getCardView().setRotate(0);
    }

    public boolean visibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public boolean canBePutOnBoard() {
        return canBePutOnBoard;
    }

    public boolean isEffectAvailable() {
        return isEffectAvailable;
    }

    public void setEffectAvailable(boolean effectAvailable) {
        isEffectAvailable = effectAvailable;
    }
}
