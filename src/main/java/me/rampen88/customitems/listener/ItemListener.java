package me.rampen88.customitems.listener;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.crafting.ItemMaster;
import me.rampen88.customitems.crafting.SimpleItem;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ItemListener implements Listener{

	private ItemMaster craftingMaster;
	private boolean attemptUpdateInv;

	public ItemListener(CustomItems plugin, ItemMaster craftingMaster) {
		this.craftingMaster = craftingMaster;
		attemptUpdateInv = plugin.getConfig().getBoolean("UpdateInventoryOnCancel", true);
	}

	@EventHandler(ignoreCancelled = true)
	public void onCraftItem(CraftItemEvent e){
		ItemStack item = e.getRecipe().getResult();
		if(item == null) return;

		SimpleItem simpleItem = craftingMaster.getItem(item);
		if(simpleItem == null) return;

		if(!simpleItem.hasPermission(e.getWhoClicked())){
			e.setCancelled(true);
			if(attemptUpdateInv){
				((Player)e.getWhoClicked()).updateInventory();
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
		if(simpleItem == null)
			return;

		if(simpleItem.itemClicked(event.getPlayer())){
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onItemConsume(PlayerItemConsumeEvent e){
		ItemStack item = e.getItem();
		if(item == null)
			return;

		SimpleItem simpleItem = craftingMaster.getItem(item);
		if(simpleItem == null)
			return;

		if(simpleItem.itemConsumed(e.getPlayer())){
			e.setCancelled(true);
		}

	}


}
