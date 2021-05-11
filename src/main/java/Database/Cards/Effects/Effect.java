package Database.Cards.Effects;

import Gameplay.Gameplay;

public abstract class Effect {
    public abstract void execute(Gameplay gameplay);
    public EffectTypes effectType = null;
}

