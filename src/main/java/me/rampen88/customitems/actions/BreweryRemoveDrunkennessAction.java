package me.rampen88.customitems.actions;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.exceptions.CancelActionsException;
import me.rampen88.customitems.hooks.BreweryHook;
import org.bukkit.entity.Player;

public class BreweryRemoveDrunkennessAction implements ItemAction{

	private final int amount;
	private final BreweryHook breweryHook;
	private final boolean showDrunkenness;

	public BreweryRemoveDrunkennessAction(BreweryHook breweryHook, int amount, boolean showDrunkenness){
		this.breweryHook = breweryHook;
		this.amount = amount;
		this.showDrunkenness = showDrunkenness;
	}

	@Override
	public boolean doTheThing(Player p) throws CancelActionsException{
		if(breweryHook.removeDrunkenness(p, amount) && showDrunkenness){
			breweryHook.showDrunkenness(p);
		}
		return false;
	}
}
