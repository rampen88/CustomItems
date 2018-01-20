package me.rampen88.customitems.actions;

import me.rampen88.customitems.exceptions.CancelActionsException;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundAction implements ItemAction {

	private Sound sound;
	private float volume;
	private float pitch;

	SoundAction(Sound sound, float volume, float pitch) {
		this.sound = sound;
		this.volume = volume;
		this.pitch = pitch;
	}

	@Override
	public boolean doTheThing(Player p) throws CancelActionsException {
		p.getWorld().playSound(p.getLocation(), sound, volume, pitch);
		return false;
	}
}
