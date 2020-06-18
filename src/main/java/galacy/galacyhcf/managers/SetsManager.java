package galacy.galacyhcf.managers;

import cn.nukkit.potion.Effect;

public class SetsManager {
    public final static Effect[] minerEffects = new Effect[]{
            Effect.getEffect(Effect.NIGHT_VISION).setAmplifier(3).setDuration(999999999),
            Effect.getEffect(Effect.HASTE).setAmplifier(3).setDuration(999999999),
            Effect.getEffect(Effect.FIRE_RESISTANCE).setAmplifier(1).setDuration(999999999),
    };
    public final static Effect[] bardEffects = new Effect[]{
            Effect.getEffect(Effect.SPEED).setAmplifier(1).setDuration(999999999),
            Effect.getEffect(Effect.REGENERATION).setAmplifier(0).setDuration(999999999),
            Effect.getEffect(Effect.DAMAGE_RESISTANCE).setAmplifier(0).setDuration(999999999),
            Effect.getEffect(Effect.FIRE_RESISTANCE).setAmplifier(1).setDuration(999999999),
    };
    public final static Effect[] archerEffects = new Effect[]{
            Effect.getEffect(Effect.SPEED).setAmplifier(2).setDuration(999999999),
            Effect.getEffect(Effect.REGENERATION).setAmplifier(0).setDuration(999999999),
            Effect.getEffect(Effect.DAMAGE_RESISTANCE).setAmplifier(1).setDuration(999999999),
    };
    public final static Effect[] rogueEffects = new Effect[]{
            Effect.getEffect(Effect.SPEED).setAmplifier(2).setDuration(999999999),
            Effect.getEffect(Effect.JUMP).setAmplifier(0).setDuration(999999999),
    };

    public enum Sets {
        Nothing,
        Miner,
        Bard,
        Archer,
        Rogue
    }
}
