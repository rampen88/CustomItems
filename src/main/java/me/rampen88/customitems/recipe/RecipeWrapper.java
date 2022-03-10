package me.rampen88.customitems.recipe;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;

public class RecipeWrapper<T extends Recipe> {

	private final T recipe;
	private final NamespacedKey key;

	public RecipeWrapper(NamespacedKey key, T recipe){
		this.recipe = recipe;
		this.key = key;
	}

	public T getRecipe(){
		return recipe;
	}

	public NamespacedKey getKey(){
		return key;
	}
}
