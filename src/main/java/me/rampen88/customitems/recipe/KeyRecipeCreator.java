package me.rampen88.customitems.recipe;

import me.rampen88.customitems.CustomItems;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class KeyRecipeCreator extends RecipeCreator{

	public KeyRecipeCreator(CustomItems plugin) {
		super(plugin);
	}

	@Override
	public ShapelessRecipe createShapelessRecipe(String name, ItemStack item) {
		NamespacedKey key = getKey(name);
		return new ShapelessRecipe(key, item);
	}

	@Override
	public ShapedRecipe createShapedRecipe(String name, ItemStack item) {
		NamespacedKey key = getKey(name);
		return new ShapedRecipe(key, item);
	}

	private NamespacedKey getKey(String name){
		return new NamespacedKey(plugin, name);
	}

}
