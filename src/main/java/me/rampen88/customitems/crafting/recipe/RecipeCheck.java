package me.rampen88.customitems.crafting.recipe;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface RecipeCheck {

	boolean canCraft(ItemStack[] craftingMatrix);

	Inventory getInventoryShowingRecipe();

	/**
	 * If the recipe requires more than 1 item in one of the item stacks, this removes the extra items and returns an array of what the matrix should look like after crafting.
	 * @param matrix The current crafting matrix
	 * @return A new crafting matrix, or null if no changes were made.
	 */
	ItemStack[] removeExtraItemsOnCraft(ItemStack[] matrix);

}
