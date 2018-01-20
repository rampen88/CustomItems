package me.rampen88.customitems.recipe;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.crafting.SimpleItem;
import me.rampen88.customitems.crafting.recipe.item.CustomItem;
import me.rampen88.customitems.crafting.recipe.item.MaterialItem;
import me.rampen88.customitems.crafting.recipe.recipe.ShapelessCheck;
import me.rampen88.customitems.util.MiscUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.material.MaterialData;

import java.util.function.Consumer;

public class RecipeCreator{

	CustomItems plugin;
	private MiscUtil util;

	public RecipeCreator(CustomItems plugin) {
		this.plugin = plugin;
		util = plugin.getMiscUtil();
	}

	public ShapedRecipe getShapedRecipe(ConfigurationSection section, ItemStack item, ShapelessCheck recipeCheck){
		ShapedRecipe recipe = this.createShapedRecipe(section.getName(), item);
		String[] shape = section.getString("Shape").split(",");
		recipe.shape(shape);

		section.getStringList("Ingredients").forEach(s -> {
			String[] ingredient = s.split(":");
			if(ingredient.length < 2){
				plugin.getLogger().warning("Ingredient '" + s + "'  is not valid. Please make sure its set up correctly.");
				return;
			}

			char key = ingredient[0].toCharArray()[0];
			int amount = MiscUtil.countCharInString(key, section.getString("Shape"));
			addIngredient(ingredient, amount, recipeCheck, (data) -> recipe.setIngredient(key, data));
		});

		return recipe;
	}

	public ShapelessRecipe getShapelessRecipe(ConfigurationSection section, ItemStack item, ShapelessCheck recipeCheck){
		ShapelessRecipe recipe = this.createShapelessRecipe(section.getName(), item);
		section.getStringList("Ingredients").forEach(s -> {
			String[] ingredient = s.split(":");
			if(ingredient.length < 2){
				plugin.getLogger().warning("Ingredient '" + s + "' is not valid. Please make sure its set up correctly.");
				return;
			}

			Integer amount = util.parseInt(ingredient[0]);
			if(amount == null)
				return;
			addIngredient(ingredient, amount, recipeCheck, (data) -> recipe.addIngredient(amount, data));
		});

		return recipe;
	}

	private void addIngredient(String[] ingredient, int amount, ShapelessCheck recipeCheck, Consumer<MaterialData> consumer){
		if(ingredient[1].startsWith("CI-")){
			String[] citemString = ingredient[1].split("-");
			SimpleItem simpleItem = citemString.length < 2 ? null : plugin.getItemMaster().getItemByName(citemString[1]);
			if(simpleItem == null){
				plugin.getLogger().warning("Failed to set up custom ingredient: " + ingredient[1]);
			}else{
				ItemStack item = simpleItem.getItem();
				consumer.accept(item.getData());
				recipeCheck.addIngredient(new CustomItem(simpleItem), amount);
			}
		}else{
			Material m = getMaterial(ingredient[1]);
			Integer damageValue = ingredient.length < 3 ? 0 : util.parseInt(ingredient[2]);
			// util.parseInt can return null
			//noinspection ConstantConditions
			if(damageValue != null || m == null){
				consumer.accept(getMaterialData(m, damageValue));
				recipeCheck.addIngredient(new MaterialItem(m), amount);
			}
		}
	}

	private MaterialData getMaterialData(Material m, int damageValue){
		// Using item.getData() avoids the deprecated constructor for MaterialData(Material, byte)
		ItemStack item = new ItemStack(m, 1, (short) damageValue);
		return item.getData();
	}

	private Material getMaterial(String name){
		Material m = Material.getMaterial(name);
		if(m == null)
			plugin.getLogger().warning("Invalid material: " + name);

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
