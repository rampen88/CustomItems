package me.rampen88.customitems.actions;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.actions.cosmetic.EffectAction;
import me.rampen88.customitems.actions.cosmetic.ParticleAction;
import me.rampen88.customitems.exceptions.CancelActionsException;
import me.rampen88.rampencore.util.MessageUtil;
import org.bukkit.Effect;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

public class ItemActionSet {

	private CustomItems plugin;
	private Set<ItemAction> actionSet = new LinkedHashSet<>();
	private String itemId;

	public ItemActionSet(CustomItems plugin, List<String> actionStringList, ItemStack item, String itemId) {
		this.plugin = plugin;
		this.itemId = itemId;
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
				case "PT":
					toAdd = getParticleAction(action);
					break;
				case "ET":
					toAdd = getEffectAction(action);
					break;
				case "MP":
					toAdd = getMessageAction(action);
					break;
				case "PS":
					toAdd = getSoundAction(action);
					break;
				case "BREWERY-RD":
				case "RD":
					toAdd = getBreweryRemoveDrunknessAction(action);
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

	private ItemAction getParticleAction(String[] args){
		if(args.length < 7){
			plugin.getLogger().info("Invalid particle trail, missing arguments");
			return null;
		}

		Particle particle = plugin.getMiscUtil().valueOf(args[1], args[1] + " is not a valid particle", Particle.class);
		Integer amount = plugin.getMiscUtil().parseInt(args[2]);
		Double offX = plugin.getMiscUtil().parseDouble(args[3]);
		Double offY = plugin.getMiscUtil().parseDouble(args[4]);
		Double offZ = plugin.getMiscUtil().parseDouble(args[5]);
		Integer delay = plugin.getMiscUtil().parseInt(args[6]);
		Integer timesToPlay = args.length > 7 ? plugin.getMiscUtil().parseInt(args[7]) : 0;
		if(particle == null || amount == null || offX == null || offY == null || offZ == null || delay == null){
			return null;
		}

		return new ParticleAction(actionSet.size(), particle, amount, offX, offY, offZ, delay, timesToPlay, itemId);
	}

	// TODO: Don't be lazy
	private ItemAction getEffectAction(String[] args){
		if(args.length < 7){
			plugin.getLogger().info("Invalid effect trail, missing arguments");
			return null;
		}

		Effect effect = plugin.getMiscUtil().valueOf(args[1], args[1] + " is not a valid effect", Effect.class);
		Integer data = plugin.getMiscUtil().parseInt(args[2]);
		Double offX = plugin.getMiscUtil().parseDouble(args[3]);
		Double offY = plugin.getMiscUtil().parseDouble(args[4]);
		Double offZ = plugin.getMiscUtil().parseDouble(args[5]);
		Integer delay = plugin.getMiscUtil().parseInt(args[6]);
		if(effect == null || data == null || offX == null || offY == null || offZ == null || delay == null){
			return null;
		}

		return new EffectAction(actionSet.size(), effect, data, offX, offY, offZ, delay, itemId);
	}

	private ItemAction getMessageAction(String[] args){
		if(args.length < 2){
			plugin.getLogger().info("Invalid message, missing arguments");
			return null;
		}

		StringJoiner stringJoiner = new StringJoiner(":");
		for (int i = 1; i < args.length; i++) {
			stringJoiner.add(args[i]);
		}
		String message = stringJoiner.toString();
		message = MessageUtil.translateColors(message);
		return new MessageAction(message);
	}

	private ItemAction getSoundAction(String[] args){
		if(args.length < 4){
			plugin.getLogger().info("Invalid sound, missing arguments");
			return null;
		}

		Sound sound = plugin.getMiscUtil().valueOf(args[1], args[1] + " is not a valid sound", Sound.class);
		double volume = plugin.getMiscUtil().parseDouble(args[2]);
		double pitch  = plugin.getMiscUtil().parseDouble(args[3]);

		return new SoundAction(sound, (float) volume, (float)pitch);
	}

	private ItemAction getBreweryRemoveDrunknessAction(String[] args){
		if(plugin.getBreweryHook() == null){
			plugin.getLogger().warning("Tried to load Brewery dependant action, but brewery hook was not found!");
		}else if(args.length > 1){
			Integer amount = plugin.getMiscUtil().parseInt(args[1]);
			boolean showDrunkenness = args.length <= 2 || Boolean.getBoolean(args[2]);
			return amount != null ? new BreweryRemoveDrunkennessAction(plugin.getBreweryHook(), amount, showDrunkenness) : null;
		}else{
			plugin.getLogger().info("Invalid brewery remove drunkness action, missing arguments");
		}
		return null;
	}

}
