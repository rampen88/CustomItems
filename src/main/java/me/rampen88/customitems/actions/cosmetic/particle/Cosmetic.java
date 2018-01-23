package me.rampen88.customitems.actions.cosmetic.particle;

import org.bukkit.entity.Player;

public abstract class Cosmetic {

	int amount;
	double offsetX;
	double offsetY;
	double offsetZ;
	private int delay;
	private int id;
	private String itemId;

	private int currentDelay;

	Cosmetic(int id, int amount, double offsetX, double offsetY, double offsetZ, int delay, String itemId) {
		this.id = id;
		this.amount = amount;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.delay = delay;
		this.itemId = itemId;
	}

	public boolean play(Player player){
		if(delay >= currentDelay){
			currentDelay = 0;
			return spawn(player);
		}else{
			currentDelay++;
		}
		return false;
	}

	protected abstract boolean spawn(Player player);

	public int getId() {
		return id;
	}

	public String getItemId() {
		return itemId;
	}
}
