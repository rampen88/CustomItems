package me.rampen88.customitems.actions.cosmetic.particle;

import org.bukkit.Effect;
import org.bukkit.entity.Player;

public class CosmeticEffect extends Cosmetic {

	private Effect effect;

	public CosmeticEffect(int id, Effect effect, int data, double offsetX, double offsetY, double offsetZ, int delay, String itemId) {
		super(id, data, offsetX, offsetY, offsetZ, delay, itemId);
		this.effect = effect;
	}

	@Override
	protected boolean spawn(Player player) {
		player.getWorld().playEffect(player.getLocation().add(offsetX, offsetY, offsetZ), effect, amount);
		return false;
	}
}
