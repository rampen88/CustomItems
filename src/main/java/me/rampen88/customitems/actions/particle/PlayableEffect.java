package me.rampen88.customitems.actions.particle;

import org.bukkit.Effect;
import org.bukkit.entity.Player;

public class PlayableEffect extends Playable{

	private Effect effect;

	public PlayableEffect(Effect effect, int data, double offsetX, double offsetY, double offsetZ, int delay) {
		super(data, offsetX, offsetY, offsetZ, delay);
		this.effect = effect;
	}

	@Override
	protected boolean spawn(Player player) {
		player.getWorld().playEffect(player.getLocation().add(offsetX, offsetY, offsetZ), effect, amount);
		return true;
	}
}
