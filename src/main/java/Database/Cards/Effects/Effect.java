package Database.Cards.Effects;

import Gameplay.*;

public abstract class Effect {
    public abstract void execute(Player cardOwner, Gameplay gameplay);
    public EffectTypes effectType = null;
}

