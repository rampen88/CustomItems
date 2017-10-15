package me.rampen88.customitems.actions;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.exceptions.CancelActionsException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ItemActionSet {

	private CustomItems plugin;
	private Set<ItemAction> actionSet = new LinkedHashSet<>();

	public ItemActionSet(CustomItems plugin, List<String> actionStringList, ItemStack item) {
		this.plugin = plugin;
		loadActions(actionStringList, item);
	}

	public boolean triggerAll(Player p){
		boolean cancel = false;
		try {
			for (ItemAction item : actionSet) {
				if (!cancel)
					cancel = item.doTheThing(p);
				else
					item.doTheThing(p);
			}
		}catch (CancelActionsException ignored){}
		return cancel;
	}

	private void loadActions(List<String> list, ItemStack item){
		list.forEach(s -> {
			String[] action = s.split(":");
			if(action.length < 1){
				plugin.getLogger().info("Invalid item action: " + s);
				return;
			}

			String[] actionInfo = action[0].split("-");
			ItemAction toAdd = null;

			String toCheck = actionInfo.length > 1 ? actionInfo[0] : action[0];
			switch (toCheck.toUpperCase()){
				case "PE":
					toAdd = getPotionEffectAction(action);
					break;
				case "EF":
					toAdd = getExtraAction(action, false);
					break;
				case "CE":
					// Just add and return, as this task cannot be delayed.
					actionSet.add(new CancelAction());
					return;
				case "ES":
					toAdd = getExtraAction(action, true);
					break;
				case "RI":
					toAdd = getRemoveItemAction(action, item);
					break;
				default:
					plugin.getLogger().info("Unknown item action: " + action[0]);
			}

 			if(toAdd == null)
 				return;

			if(actionInfo.length > 1){

				// Get the wanted delay
				Integer delay = plugin.getMiscUtil().parseInt(actionInfo[1]);
				if(delay == 0){
					plugin.getLogger().info("Ignoring attempted delay option, as number was not valid for '" + s + "'");
					actionSet.add(toAdd);
					return;
				}
				// Create a ItemAction which delays the actual item action.
				ItemAction delayed = new DelayedItemAction(toAdd, delay, plugin);
				actionSet.add(delayed);
			}else{
				actionSet.add(toAdd);
			}
		});
	}

	private ItemAction getRemoveItemAction(String[] args, ItemStack item){
		if(args.length < 2){
			plugin.getLogger().info("Invalid Effect, missing arguments?");
			return null;
		}

		Integer amount = plugin.getMiscUtil().parseInt(args[1]);
		if(amount == null) return null;

		return new RemoveItemAction(item, amount);
	}

	private ItemAction getExtraAction(String[] args, boolean saturation){
		if(args.length < 2){
			plugin.getLogger().info("Invalid Potion Effect, missing arguments?");
			return null;
		}

		Integer amount = plugin.getMiscUtil().parseInt(args[1]);
		if(amount == null) return null;

		if(saturation){
			return new ExtraSaturationAction(amount);
		}else{
			return new ExtraFoodAction(amount);
		}

	}

	private ItemAction getPotionEffectAction(String[] args){
		if(args.length < 3){
			plugin.getLogger().info("Invalid Potion Effect, missing arguments?");
			return null;
		}


		PotionEffectType type = PotionEffectType.getByName(args[1]);
		if(type == null){
			plugin.getLogger().info("Invalid PotionEffectType: " + args[1]);
			return null;
		}

		Integer duration = plugin.getMiscUtil().parseInt(args[2]);
		if(duration == null) return null;

		Integer amplifier = args.length >= 4 ? plugin.getMiscUtil().parseInt(args[3]) : Integer.valueOf(0);
		if(amplifier == null) return null;

		return new PotionEffectAction(type, duration, amplifier);
	}

}
