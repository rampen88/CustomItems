package me.rampen88.customitems.crafting.recipe.item;

import me.rampen88.customitems.crafting.recipe.RecipeItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialItem implements RecipeItem {

	private Material material;

	public MaterialItem(Material material) {
		this.material = material;
	}

	@Override
	public boolean isSimilar(ItemStack itemStack) {
		return itemStack.getType() == material;
	}

}
