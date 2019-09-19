package me.rampen88.customitems.crafting;

import me.rampen88.customitems.crafting.recipe.RecipeCheck;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.Queue;

public class SpecialRecipeItem extends SimpleItem{

	private RecipeCheck recipeCheck;

	SpecialRecipeItem(ItemStack item, ConfigurationSection section, RecipeCheck recipeCheck) {
		super(item, section);
		this.recipeCheck = recipeCheck;
	}

	@Override
	public boolean canCraft(ItemStack[] itemMatrix) {
		return recipeCheck.canCraft(itemMatrix);
	}

	public Inventory getInventoryShowingRecipe(){
		return recipeCheck.getInventoryShowingRecipe();
	}

}
