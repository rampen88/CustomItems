package me.rampen88.customitems.actions.cosmetic.particle;

import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class LimitedCosmeticParticle extends CosmeticParticle {

	private int timesToPlay;
	private int currentlyPlayed;

	public LimitedCosmeticParticle(int id, Particle particle, int amount, double offsetX, double offsetY, double offsetZ, int delay, int timesToPlay, String itemId) {
		super(id, particle, amount, offsetX, offsetY, offsetZ, delay, itemId);
		this.timesToPlay = timesToPlay;
	}

	@Override
	protected boolean spawn(Player player) {
		super.spawn(player);
		currentlyPlayed++;
		return currentlyPlayed >= timesToPlay;
	}
}
