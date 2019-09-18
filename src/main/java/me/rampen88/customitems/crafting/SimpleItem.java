package me.rampen88.customitems.crafting;

import me.rampen88.customitems.actions.ItemActionSet;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;

public class SimpleItem {

	private ItemActionSet consumeActions;
	private ItemActionSet clickActions;
	private ItemStack item;
	private String permission;
	private String name;

	public SimpleItem(ItemStack item, String name){
		this.item = item;
		this.name = name;
	}

	SimpleItem(ItemStack item, ConfigurationSection section){
		this.item = item;
		permission = section.getString("Permission");
		name = section.getName();
	}

	public boolean hasPermission(Permissible p){
		return permission == null || p.hasPermission(permission);
	}

	public boolean isSimilar(ItemStack item){
		return this.item.isSimilar(item);
	}

	void setConsumeActions(ItemActionSet consumeActions) {
		this.consumeActions = consumeActions;
	}

	void setClickActions(ItemActionSet clickActions){
		this.clickActions = clickActions;
	}

	public boolean itemConsumed(Player p){
		return consumeActions != null && consumeActions.triggerAll(p);
	}

	public boolean itemClicked(Player p){
		return clickActions != null && clickActions.triggerAll(p);
	}

	public ItemStack getItem() {
		return new ItemStack(item);
	}

	public String getName() {
		return name;
	}

	public boolean canCraft(ItemStack[] itemMatrix){
		return true;
	}

}
