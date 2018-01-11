package me.rampen88.customitems.actions;

import me.rampen88.customitems.actions.particle.LimitedPlayableParticle;
import me.rampen88.customitems.actions.particle.Playable;
import me.rampen88.customitems.actions.particle.PlayableParticle;
import org.bukkit.Particle;

public class ParticleAction extends PlayableAction{

	private Particle particle;
	private int timesToPlay;

	ParticleAction(Particle particle, int amount, double offsetX, double offsetY, double offsetZ, int delay, int timesToPlay) {
		super(amount, offsetX, offsetY, offsetZ, delay);
		this.particle = particle;
		this.timesToPlay = timesToPlay;
	}

	@Override
	protected Playable getPlayable() {
		if(timesToPlay == 0){
			return new PlayableParticle(particle, amount, offsetX, offsetY, offsetZ, delay);
		}else{
			return new LimitedPlayableParticle(particle, amount, offsetX, offsetY, offsetZ, delay, timesToPlay);
		}
	}
}
