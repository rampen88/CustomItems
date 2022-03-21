package me.rampen88.customitems.recipe;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.crafting.SimpleItem;
import me.rampen88.customitems.crafting.recipe.RecipeItem;
import me.rampen88.customitems.crafting.recipe.item.CustomItem;
import me.rampen88.customitems.crafting.recipe.item.MaterialItem;
import me.rampen88.customitems.crafting.recipe.item.PotionItem;
import me.rampen88.customitems.crafting.recipe.recipe.ShapedCheck;
import me.rampen88.customitems.crafting.recipe.recipe.ShapelessCheck;
import me.rampen88.customitems.util.MiscUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.function.Consumer;

public class RecipeCreator{

	CustomItems plugin;
	private MiscUtil util;

	public RecipeCreator(CustomItems plugin) {
		this.plugin = plugin;
		util = plugin.getMiscUtil();
	}

	public RecipeWrapper<ShapedRecipe> getShapedRecipe(ConfigurationSection section, ItemStack item, ShapedCheck recipeCheck){
		RecipeWrapper<ShapedRecipe> recipeWrapper = this.createShapedRecipe(section.getName(), item);
		ShapedRecipe recipe = recipeWrapper.getRecipe();
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

		return recipeWrapper;
	}

	public RecipeWrapper<ShapelessRecipe> getShapelessRecipe(ConfigurationSection section, ItemStack item, ShapelessCheck recipeCheck){
		RecipeWrapper<ShapelessRecipe> recipeWrapper = this.createShapelessRecipe(section.getName(), item);
		ShapelessRecipe recipe = recipeWrapper.getRecipe();
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

		return recipeWrapper;
	}

	private RecipeItem getIngredient(String[] ingredient, Consumer<Material> consumer){
		Integer amount = ingredient.length <= 2 ? 1 : plugin.getMiscUtil().parseInt(ingredient[2]);
		if(amount == null){
			plugin.getLogger().warning("Unable to get amount for item: " + Arrays.toString(ingredient) + ", item will be skipped.");
			return null;
		}
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
		}else if(ingredient[1].startsWith("POTION")){
			if(ingredient.length < 4){
				plugin.getLogger().severe("Unable to set up for Potion Item in recipe due to incorrect configuration. Potion will be skipped for the recipe.");
				return null;
			}
			PotionType type = getPotionType(ingredient[3]);
			String extra = ingredient.length == 5 ? ingredient[4] : "none";
			if(type == null){
				plugin.getLogger().severe("Unable to set up for Potion Item due to " + ingredient[3] + " not being a valid potion type. Potion will be skipped for the recipe.");
				return null;
			}
			PotionItem item = new PotionItem(type, extra.equalsIgnoreCase("extended"), extra.equalsIgnoreCase("upgraded"));
			consumer.accept(item.getItemStack().getType());
			return item;
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

	private PotionType getPotionType(String type){
		try{
			return PotionType.valueOf(type);
		}catch(IllegalArgumentException e){
			return null;
		}
	}

	private Material getMaterial(String name){
		Material m = Material.getMaterial(name);
		if(m == null)
			plugin.getLogger().warning("Invalid material: " + name);

		return m;
	}
	public RecipeWrapper<ShapelessRecipe> createShapelessRecipe(String name, ItemStack item) {
		NamespacedKey key = getKey(name);
		return new RecipeWrapper<>(key, new ShapelessRecipe(key, item));
	}

	public RecipeWrapper<ShapedRecipe> createShapedRecipe(String name, ItemStack item) {
		NamespacedKey key = getKey(name);
		return new RecipeWrapper<>(key, new ShapedRecipe(key, item));
	}

	private NamespacedKey getKey(String name){
		return new NamespacedKey(plugin, name);
	}

}
