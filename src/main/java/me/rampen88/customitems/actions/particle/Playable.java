package me.rampen88.customitems.actions.particle;

import org.bukkit.entity.Player;

public abstract class Playable{

	int amount;
	double offsetX;
	double offsetY;
	double offsetZ;
	private int delay;

	private int currentDelay;

	Playable(int amount, double offsetX, double offsetY, double offsetZ, int delay) {
		this.amount = amount;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.delay = delay;
	}

	public boolean play(Player player){
		if(delay >= currentDelay){
			currentDelay = 0;
			return spawn(player);
		}else{
			currentDelay++;
		}
		return true;
	}

	protected abstract boolean spawn(Player player);

}
