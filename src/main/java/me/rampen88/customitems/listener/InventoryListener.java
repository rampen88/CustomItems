package me.rampen88.customitems.listener;

import me.rampen88.customitems.inventory.CustomItemsInventoryHolder;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryView;

public class InventoryListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void inventoryCloseEvent(InventoryCloseEvent event){
		clearTopInventoryIfCustom(event.getPlayer());
	}

	@EventHandler
	public void playerQuitEvent(PlayerQuitEvent event){
		clearTopInventoryIfCustom(event.getPlayer());
	}

	@EventHandler
	public void inventoryClickEvent(InventoryClickEvent event){
		if(event.getView().getTopInventory().getHolder() instanceof CustomItemsInventoryHolder){
			event.setCancelled(true);
		}
	}

	private void clearTopInventoryIfCustom(HumanEntity player){
		InventoryView view = player.getOpenInventory();
		if(view.getTopInventory().getHolder() instanceof CustomItemsInventoryHolder){
			view.getTopInventory().clear();
		}
	}

}
