package me.rampen88.customitems.actions;

import me.rampen88.customitems.actions.particle.Playable;
import me.rampen88.customitems.exceptions.CancelActionsException;
import me.rampen88.customitems.listener.ParticleHandler;
import org.bukkit.entity.Player;

public abstract class PlayableAction implements ItemAction {

	int amount;
	double offsetX;
	double offsetY;
	double offsetZ;
	int delay;

	PlayableAction(int amount, double offsetX, double offsetY, double offsetZ, int delay) {
		this.amount = amount;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.delay = delay;
	}

	@Override
	public boolean doTheThing(Player p) throws CancelActionsException {
		Playable playableParticle = getPlayable();
		ParticleHandler.toggleParticles(p, playableParticle);
		return false;
	}

	protected abstract Playable getPlayable();

}
