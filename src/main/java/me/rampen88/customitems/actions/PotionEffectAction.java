package me.rampen88.customitems.actions;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionEffectAction implements ItemAction {

	private PotionEffectType type;
	private int duration;
	private int amplifier;

	PotionEffectAction(PotionEffectType type, int duration, int amplifier) {
		this.type = type;
		this.duration = duration;
		this.amplifier = amplifier;
	}

	@Override
	public boolean doTheThing(Player p) {
		p.addPotionEffect(new PotionEffect(type, duration, amplifier), true);
		return false;
	}

}
