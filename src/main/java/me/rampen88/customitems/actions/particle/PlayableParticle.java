package me.rampen88.customitems.actions.particle;

import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class PlayableParticle extends Playable{

	private Particle particle;

	public PlayableParticle(Particle particle, int amount, double offsetX, double offsetY, double offsetZ, int delay) {
		super(amount, offsetX, offsetY, offsetZ, delay);
		this.particle = particle;
	}

	protected boolean spawn(Player player){
		player.getWorld().spawnParticle(particle, player.getLocation().add(offsetX, offsetY, offsetZ), amount);
		return true;
	}

}
