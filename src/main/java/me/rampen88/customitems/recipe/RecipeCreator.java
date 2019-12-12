package me.rampen88.customitems.recipe;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.crafting.SimpleItem;
import me.rampen88.customitems.crafting.recipe.RecipeItem;
import me.rampen88.customitems.crafting.recipe.item.CustomItem;
import me.rampen88.customitems.crafting.recipe.item.MaterialItem;
import me.rampen88.customitems.crafting.recipe.recipe.ShapedCheck;
import me.rampen88.customitems.crafting.recipe.recipe.ShapelessCheck;
import me.rampen88.customitems.util.MiscUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.function.Consumer;

public class RecipeCreator{

	CustomItems plugin;
	private MiscUtil util;

	public RecipeCreator(CustomItems plugin) {
		this.plugin = plugin;
		util = plugin.getMiscUtil();
	}

	public ShapedRecipe getShapedRecipe(ConfigurationSection section, ItemStack item, ShapedCheck recipeCheck){
		ShapedRecipe recipe = this.createShapedRecipe(section.getName(), item);
		String[] shape = section.getString("Shape").split(",");
		recipe.shape(shape);
		recipeCheck.setShape(shape);

		section.getStringList("Ingredients").forEach(s -> {
			String[] ingredient = s.split(":");
			if(ingredient.length < 2){
				plugin.getLogger().warning("Ingredient '" + s + "'  is not valid. Please make sure its set up correctly.");
				return;
			}

			char key = ingredient[0].toCharArray()[0];
			int amount = MiscUtil.countCharInString(key, section.getString("Shape"));
			RecipeItem recipeItem = getIngredient(ingredient, (data) -> recipe.setIngredient(key, data));
			if(recipeItem != null)
				recipeCheck.addIngredient(recipeItem, amount, key);
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
			if(amount != null){
				RecipeItem recipeItem = getIngredient(ingredient, (data) -> recipe.addIngredient(amount, data));
				if(recipeItem != null)
					recipeCheck.addIngredient(recipeItem, amount);
			}
		});

		return recipe;
	}

	private RecipeItem getIngredient(String[] ingredient, Consumer<Material> consumer){
		int amount = ingredient.length <= 2 ? 1 : plugin.getMiscUtil().parseInt(ingredient[2]);
		if(ingredient[1].startsWith("CI-")){
			String[] citemString = ingredient[1].split("-");
			SimpleItem simpleItem = citemString.length < 2 ? null : plugin.getItemHandler().getItemByName(citemString[1]);
			if(simpleItem == null){
				plugin.getLogger().warning("Failed to set up custom ingredient: " + ingredient[1]);
			}else{
				ItemStack item = simpleItem.getItem();
				consumer.accept(item.getType());
				return new CustomItem(simpleItem, amount);
			}
		}else if(ingredient[1].startsWith("MI-")){
			ItemStack itemStack = CustomItems.getMythicMobsItem(ingredient[1]);
			if(itemStack != null){
				consumer.accept(itemStack.getType());
				return new CustomItem(new SimpleItem(itemStack, ingredient[1]), amount);
			}else{
				plugin.getLogger().warning("MythicMobs Item '" + ingredient[1] + "' was not found.");
			}
		}else{
			Material m = getMaterial(ingredient[1]);
			if(m == null){
				plugin.getLogger().severe("Unable to find material '" + ingredient[1] + "'. Material will be skipped for the recipe.");
			}else{
				consumer.accept(m);
				return new MaterialItem(m, amount);
			}
		}
		return null;
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
