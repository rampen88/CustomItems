package me.rampen88.customitems.listener;

import me.rampen88.customitems.crafting.ItemHandler;
import me.rampen88.customitems.crafting.SimpleItem;
import me.rampen88.customitems.crafting.SpecialRecipeItem;
import me.rampen88.customitems.inventory.CustomItemsInventoryHolder;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public class InventoryListener implements Listener {

	private ItemHandler itemHandler;

	public InventoryListener(ItemHandler itemHandler){
		this.itemHandler = itemHandler;
	}

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

	@EventHandler(priority = EventPriority.LOWEST)
	public void test(PrepareItemCraftEvent event){
		ItemStack[] matrix = event.getInventory().getMatrix();
		Optional<SpecialRecipeItem> item = itemHandler.getRecipeItemFromRecipe(matrix);
		item.ifPresent(recipeItem -> {
			if(viewersHavePermission(recipeItem, event.getInventory().getViewers())){
				event.getInventory().setResult(recipeItem.getItem());
			}
		});
	}

	private boolean viewersHavePermission(SimpleItem item, List<HumanEntity> permissible){
		for(HumanEntity humanEntity : permissible){
			if(!item.hasPermission(humanEntity))
				return false;
		}
		return true;
	}

}
