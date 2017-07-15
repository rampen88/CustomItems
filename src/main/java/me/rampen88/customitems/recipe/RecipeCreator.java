package me.rampen88.customitems.recipe;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.util.MiscUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.material.MaterialData;

public class RecipeCreator{

	CustomItems plugin;
	private MiscUtil util;

	public RecipeCreator(CustomItems plugin) {
		this.plugin = plugin;
		util = plugin.getMiscUtil();
	}

	public ShapedRecipe getShapedRecipe(ConfigurationSection section, ItemStack item){
		ShapedRecipe recipe = this.createShapedRecipe(section.getName(), item);

		// Set the shape of the recipe
		String[] shape = section.getString("Shape").split(",");
		recipe.shape(shape);

		section.getStringList("Ingredients").forEach(s -> {

			String[] ingredient = s.split(":");
			if(ingredient.length < 2){
				plugin.getLogger().info("Ingredient '" + s + "'  is not valid. Please make sure its set up correctly.");
				return;
			}

			Material m = getMaterial(ingredient[1]);
			if(m == null) return;

			if(ingredient.length < 3)
				recipe.setIngredient(ingredient[0].toCharArray()[0], m);
			else{

				Integer input = util.parseInt(ingredient[2]);
				if(input == null) return;

				recipe.setIngredient(ingredient[0].toCharArray()[0], getMaterialData(m, input));
			}
		});

		return recipe;
	}

	public ShapelessRecipe getShapelessRecipe(ConfigurationSection section, ItemStack item){

		ShapelessRecipe recipe = this.createShapelessRecipe(section.getName(), item);

		section.getStringList("Ingredients").forEach(s -> {

			// Check that ingredient is 'set up' correctly-ish.
			String[] ingredient = s.split(":");
			if(ingredient.length < 2){
				plugin.getLogger().info("Ingredient '" + s + "' is not valid. Please make sure its set up correctly.");
				return;
			}

			// Get and make sure input is not null. both methods log to console if its not valid, so just return if its null.
			Material m = getMaterial(ingredient[1]);
			if(m == null) return;

			Integer amount = util.parseInt(ingredient[0]);
			if(amount == null) return;

			// check if we should check for damage value, then add ingredient to recipe as needed.
			if(ingredient.length < 3)
				recipe.addIngredient(amount, m);
			else{

				Integer input = util.parseInt(ingredient[2]);
				if(input == null) return;

				recipe.addIngredient(amount, getMaterialData(m, input));
			}
		});

		return recipe;
	}

	private MaterialData getMaterialData(Material m, int damageValue){
		// Using item.getData() avoids the deprecated constructor for MaterialData(Material, byte)
		ItemStack item = new ItemStack(m, 1, (short) damageValue);
		return item.getData();
	}

	private Material getMaterial(String name){
		Material m = Material.getMaterial(name);
		// Log to console if material is not valid
		if(m == null) plugin.getLogger().info("Invalid material: " + name);

		return m;
	}
	// These methods are not deprecated in 1.11 or below, and they are overridden if server is running 1.12 or above.
	@SuppressWarnings("deprecation")
	public ShapelessRecipe createShapelessRecipe(String name, ItemStack item) {
		return new ShapelessRecipe(item);
	}

	@SuppressWarnings("deprecation")
	public ShapedRecipe createShapedRecipe(String name, ItemStack item) {
		return new ShapedRecipe(item);
	}
}
