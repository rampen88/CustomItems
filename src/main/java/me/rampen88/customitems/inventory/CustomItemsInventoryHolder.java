package me.rampen88.customitems.inventory;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class CustomItemsInventoryHolder implements InventoryHolder {
	@NotNull
	@Override
	public Inventory getInventory(){
		return Bukkit.createInventory(null, 54);
	}
}
