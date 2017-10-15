package me.rampen88.customitems.actions;

import me.rampen88.customitems.exceptions.CancelActionsException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class RemoveItemAction implements ItemAction{

	private ItemStack itemStack;
	private int amountToRemove;

	RemoveItemAction(ItemStack itemStack, int amountToRemove) {
		this.itemStack = itemStack;
		this.amountToRemove = amountToRemove;
	}

	@Override
	public boolean doTheThing(Player p) {
		PlayerInventory playerInventory = p.getInventory();
		attemptRemoveItems(playerInventory);
		p.updateInventory();
		return false;
	}

	private void attemptRemoveItems(PlayerInventory inventory){
		ItemStack[] contents = inventory.getStorageContents();
		removeItems(contents);
		inventory.setContents(contents);
	}

	private void removeItems(ItemStack[] contents){
		int remaining = amountToRemove;
		for (ItemStack item : contents) {
			if (item != null && remaining > 0 && item.isSimilar(itemStack)) {
				if (item.getAmount() > remaining) {
					item.setAmount(item.getAmount() - remaining);
					remaining = 0;
				} else if (item.getAmount() == remaining) {
					item.setType(Material.AIR);
					remaining = 0;
				} else {
					remaining -= item.getAmount();
					item.setType(Material.AIR);
				}
			}
		}
		if(remaining > 0)
			throw new CancelActionsException();
	}




}
