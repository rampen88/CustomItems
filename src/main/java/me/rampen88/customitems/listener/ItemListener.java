package me.rampen88.customitems.listener;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.crafting.ItemHandler;
import me.rampen88.customitems.crafting.SimpleItem;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ItemListener implements Listener{

	private ItemHandler craftingMaster;
	private boolean attemptUpdateInv;

	public ItemListener(CustomItems plugin, ItemHandler craftingMaster) {
		this.craftingMaster = craftingMaster;
		attemptUpdateInv = plugin.getConfig().getBoolean("UpdateInventoryOnCancel", true);
	}

	@EventHandler(ignoreCancelled = true)
	public void onCraftItem(CraftItemEvent e){
		ItemStack item = e.getRecipe().getResult();
		if(item == null)
			return;

		SimpleItem simpleItem = craftingMaster.getItem(item);
		if(simpleItem == null)
			return;

		if(!simpleItem.hasPermission(e.getWhoClicked()) || !simpleItem.canCraft(e.getInventory().getMatrix())){
			e.setCancelled(true);
			if(attemptUpdateInv){
				((Player)e.getWhoClicked()).updateInventory();
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCraftPrepare(PrepareItemCraftEvent event){
		if(event.getRecipe() == null || event.getRecipe().getResult() == null)
			return;

		SimpleItem item = craftingMaster.getItem(event.getRecipe().getResult());
		if(item != null){
			CraftingInventory craftingInventory = event.getInventory();
			ItemStack[] items = craftingInventory.getMatrix();
			if(!item.canCraft(items)){
				craftingInventory.setResult(null);
			}
		}
	}

	@EventHandler
	public void onItemClick(PlayerInteractEvent event){
		if(event.getHand() != EquipmentSlot.HAND || (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK))
			return;

		ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
		if(heldItem == null)
			return;

		SimpleItem simpleItem = craftingMaster.getItem(heldItem);
		if(simpleItem != null && simpleItem.itemClicked(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onItemConsume(PlayerItemConsumeEvent e){
		ItemStack item = e.getItem();
		if(item == null)
			return;

		SimpleItem simpleItem = craftingMaster.getItem(item);
		if(simpleItem != null && simpleItem.itemConsumed(e.getPlayer())) {
			e.setCancelled(true);
		}
	}

}
