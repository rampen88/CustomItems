package me.rampen88.customitems.actions.cosmetic;

import me.rampen88.customitems.actions.cosmetic.particle.LimitedCosmeticParticle;
import me.rampen88.customitems.actions.cosmetic.particle.Cosmetic;
import me.rampen88.customitems.actions.cosmetic.particle.CosmeticParticle;
import org.bukkit.Particle;

public class ParticleAction extends CosmeticAction {

	private Particle particle;
	private int timesToPlay;

	public ParticleAction(int id, Particle particle, int amount, double offsetX, double offsetY, double offsetZ, int delay, int timesToPlay, String itemId) {
		super(id, amount, offsetX, offsetY, offsetZ, delay, itemId);
		this.particle = particle;
		this.timesToPlay = timesToPlay;
	}

	@Override
	protected Cosmetic getCosmetic() {
		if(timesToPlay == 0){
			return new CosmeticParticle(id, particle, amount, offsetX, offsetY, offsetZ, delay, itemId);
		}else{
			return new LimitedCosmeticParticle(id, particle, amount, offsetX, offsetY, offsetZ, delay, timesToPlay, itemId);
		}
	}
}
