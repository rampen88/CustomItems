package me.rampen88.customitems.actions;

import org.bukkit.entity.Player;

public class ExtraSaturationAction implements ItemAction {

	private int amount;

	ExtraSaturationAction(int amount) {
		this.amount = amount;
	}

	@Override
	public boolean doTheThing(Player p) {
		float currSaturation = p.getSaturation();

		currSaturation += amount;
		if (currSaturation > 20) currSaturation = 20;
		else if(currSaturation < 0) currSaturation = 0;

		p.setSaturation(currSaturation);
		return false;
	}
}
