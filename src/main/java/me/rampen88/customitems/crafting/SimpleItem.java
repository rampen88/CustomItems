package me.rampen88.customitems.crafting;

import me.rampen88.customitems.actions.ItemActionSet;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;

public class SimpleItem {


	private ItemActionSet consumeActions;
	private ItemStack item;
	private String permission;
	private String name;

	SimpleItem(ItemStack item, ConfigurationSection section){
		this.item = item;
		permission = section.getString("Permission");
		name = section.getName();
	}

	public boolean hasPermission(Permissible p){
		return permission == null || p.hasPermission(permission);
	}

	boolean isSimilar(ItemStack item){
		return this.item.isSimilar(item);
	}

	void setConsumeActions(ItemActionSet consumeActions) {
		this.consumeActions = consumeActions;
	}

	public boolean itemConsumed(Player p){
		return consumeActions.triggerAll(p);
	}

	public ItemStack getItem() {
		return new ItemStack(item);
	}

	public String getName() {
		return name;
	}
}
