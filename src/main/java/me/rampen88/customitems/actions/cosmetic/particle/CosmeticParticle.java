package me.rampen88.customitems.actions.cosmetic.particle;

import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class CosmeticParticle extends Cosmetic {

	private Particle particle;

	public CosmeticParticle(int id, Particle particle, int amount, double offsetX, double offsetY, double offsetZ, int delay, String itemId) {
		super(id, amount, offsetX, offsetY, offsetZ, delay, itemId);
		this.particle = particle;
	}

	protected boolean spawn(Player player){
		player.getWorld().spawnParticle(particle, player.getLocation().add(offsetX, offsetY, offsetZ), amount);
		return false;
	}

}
