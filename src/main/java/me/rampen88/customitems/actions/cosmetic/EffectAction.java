package me.rampen88.customitems.actions.cosmetic;

import me.rampen88.customitems.actions.cosmetic.particle.Cosmetic;
import me.rampen88.customitems.actions.cosmetic.particle.CosmeticEffect;
import org.bukkit.Effect;

public class EffectAction extends CosmeticAction {

	private Effect effect;

	public EffectAction(int id, Effect effect, int amount, double offsetX, double offsetY, double offsetZ, int delay, String itemId) {
		super(id, amount, offsetX, offsetY, offsetZ, delay, itemId);
		this.effect = effect;
	}

	@Override
	protected Cosmetic getCosmetic() {
		// TODO: Add some form of limited effect
		return new CosmeticEffect(id, effect, amount, offsetX, offsetY, offsetZ, delay, itemId);
	}
}
