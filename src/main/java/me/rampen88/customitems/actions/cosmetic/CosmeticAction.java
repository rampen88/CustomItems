package me.rampen88.customitems.actions.cosmetic;

import me.rampen88.customitems.actions.ItemAction;
import me.rampen88.customitems.actions.cosmetic.particle.Cosmetic;
import me.rampen88.customitems.exceptions.CancelActionsException;
import me.rampen88.customitems.listener.CosmeticHandler;
import org.bukkit.entity.Player;

public abstract class CosmeticAction implements ItemAction {

	int amount;
	double offsetX;
	double offsetY;
	double offsetZ;
	int delay;
	int id;
	String itemId;

	CosmeticAction(int id, int amount, double offsetX, double offsetY, double offsetZ, int delay, String itemId) {
		this.id = id;
		this.amount = amount;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.delay = delay;
		this.itemId = itemId;
	}

	@Override
	public boolean doTheThing(Player p) throws CancelActionsException {
		Cosmetic cosmeticParticle = getCosmetic();
		CosmeticHandler.toggleCosmetic(p, cosmeticParticle);
		return false;
	}

	protected abstract Cosmetic getCosmetic();

}
