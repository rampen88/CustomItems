package me.rampen88.customitems.crafting.recipe;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface RecipeCheck {

	boolean canCraft(ItemStack[] craftingMatrix);

	Inventory getInventoryShowingRecipe();

}
