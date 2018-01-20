package me.rampen88.customitems.actions;

import org.bukkit.entity.Player;

public class ExtraFoodAction implements ItemAction {

	private int amount;

	ExtraFoodAction(int amount) {
		this.amount = amount;
	}

	@Override
	public boolean doTheThing(Player p) {
		int currFood = p.getFoodLevel();

		currFood += amount;
		if (currFood > 20) currFood = 20;
		else if(currFood < 0) currFood = 0;

		p.setFoodLevel(currFood);
		return false;
	}
}
