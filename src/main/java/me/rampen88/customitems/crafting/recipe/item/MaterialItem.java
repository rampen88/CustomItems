package me.rampen88.customitems.crafting.recipe.item;

import me.rampen88.customitems.crafting.recipe.RecipeItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialItem implements RecipeItem {

	private Material material;
	private int amount;

	public MaterialItem(Material material, int amount) {
		this.material = material;
		this.amount = amount;
	}

	@Override
	public boolean isSimilar(ItemStack itemStack) {
		return itemStack.getType() == material;
	}

	@Override
	public ItemStack getItemStack(){
		return new ItemStack(material, amount);
	}

}
