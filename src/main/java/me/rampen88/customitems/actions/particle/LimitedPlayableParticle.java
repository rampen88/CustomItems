package me.rampen88.customitems.actions.particle;

import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class LimitedPlayableParticle extends PlayableParticle{

	private int timesToPlay;
	private int currentlyPlayed;

	public LimitedPlayableParticle(Particle particle, int amount, double offsetX, double offsetY, double offsetZ, int delay, int timesToPlay) {
		super(particle, amount, offsetX, offsetY, offsetZ, delay);
		this.timesToPlay = timesToPlay;
	}

	@Override
	protected boolean spawn(Player player) {
		super.spawn(player);
		currentlyPlayed++;
		return currentlyPlayed < timesToPlay;
	}
}
