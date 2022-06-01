package me.rampen88.customitems.listener;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.crafting.ItemHandler;
import me.rampen88.customitems.crafting.SimpleItem;

import me.rampen88.customitems.crafting.SpecialRecipeItem;
import org.bukkit.Keyed;
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
import org.bukkit.inventory.Recipe;

public class ItemListener implements Listener{

	private ItemHandler craftingMaster;
	private boolean attemptUpdateInv;

	public ItemListener(CustomItems plugin, ItemHandler craftingMaster) {
		this.craftingMaster = craftingMaster;
		attemptUpdateInv = plugin.getConfig().getBoolean("UpdateInventoryOnCancel", true);
	}

	@EventHandler(ignoreCancelled = true)
	public void onCraftItem(CraftItemEvent e){
		ItemStack item = e.getInventory().getResult();
		if(item == null)
			return;

		SimpleItem simpleItem = craftingMaster.getItem(item);
		if(simpleItem == null || !recipeMatches(simpleItem, e.getRecipe()))
			return;

		if(!simpleItem.hasPermission(e.getWhoClicked()) || !simpleItem.canCraft(e.getInventory().getMatrix())){
			e.setCancelled(true);
			if(attemptUpdateInv){
				((Player)e.getWhoClicked()).updateInventory();
			}
		}else{
			// Make a copy of the current matrix before modifying it, so that items don't get removed when cancelling the event.
			ItemStack[] currentMatrix = e.getInventory().getMatrix();
			ItemStack[] tempMatrix = new ItemStack[currentMatrix.length];
			for(int i = 0; i < tempMatrix.length; i++){
				if(currentMatrix[i] != null)
					tempMatrix[i] = new ItemStack(currentMatrix[i]);
			}
			ItemStack[] newMatrix = simpleItem.removeExtraItemsOnCraft(tempMatrix);
			if(newMatrix != null){
				if(e.isShiftClick()){
					e.setCancelled(true);
					return;
				}
				e.getInventory().setMatrix(newMatrix);
				if(e.getInventory().getResult() == null)
					e.getInventory().setResult(simpleItem.getItem());
				e.getInventory().getViewers().forEach(h -> ((Player)h).updateInventory());
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCraftPrepare(PrepareItemCraftEvent event){
		if(event.getRecipe() == null || event.getRecipe().getResult() == null)
			return;

		SimpleItem item = craftingMaster.getItem(event.getInventory().getResult());
		if(item != null && recipeMatches(item, event.getRecipe())){
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

	public static boolean recipeMatches(SimpleItem simpleItem, Recipe recipe){
		if(recipe instanceof Keyed && simpleItem instanceof SpecialRecipeItem){
			Keyed key = (Keyed) recipe;
			SpecialRecipeItem item = (SpecialRecipeItem) simpleItem;
			return key.getKey().equals(item.getKey());
		}
		return true;
	}
}
