package me.rampen88.customitems.actions;

import me.rampen88.customitems.actions.particle.Playable;
import me.rampen88.customitems.actions.particle.PlayableEffect;
import org.bukkit.Effect;

public class EffectAction extends PlayableAction{

	private Effect effect;

	EffectAction(Effect effect, int amount, double offsetX, double offsetY, double offsetZ, int delay) {
		super(amount, offsetX, offsetY, offsetZ, delay);
		this.effect = effect;
	}

	@Override
	protected Playable getPlayable() {
		// TODO: Add some form of limited playable effect
		return new PlayableEffect(effect, amount, offsetX, offsetY, offsetZ, delay);
	}
}
