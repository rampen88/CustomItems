package me.rampen88.customitems.crafting;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.actions.ItemActionSet;
import me.rampen88.customitems.crafting.recipe.recipe.ShapedCheck;
import me.rampen88.customitems.crafting.recipe.recipe.ShapelessCheck;
import me.rampen88.customitems.recipe.RecipeCreator;
import me.rampen88.customitems.util.ItemBuilder;
import me.rampen88.customitems.util.ItemEnchant;

import me.rampen88.customitems.util.MiscUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;

import java.util.*;

public class ItemHandler {

	private ItemBuilder itemBuilder = CustomItems.getItemBuilder();
	private CustomItems plugin;
	private MiscUtil miscUtil;

	private HashSet<SimpleItem> items = new HashSet<>();

	public ItemHandler(CustomItems plugin, MiscUtil miscUtil) {
		this.plugin = plugin;
		this.miscUtil = miscUtil;
	}

	public void reload(){
		plugin.getServer().resetRecipes();
		items.clear();
		loadItems();
	}

	public void loadItems(){
		ConfigurationSection items = plugin.getConfig().getConfigurationSection("Items");
		for(String s : items.getKeys(false)){

			ConfigurationSection section = items.getConfigurationSection(s);
			ItemStack item = loadItem(section.getConfigurationSection("ResultItem"));
			if(item == null)
				continue;

			SimpleItem simpleItem;
			// Add recipe if section contains required things.
			if(section.contains("Ingredients")){
				RecipeCreator recipeCreator = plugin.getRecipeCreator();
				ShapelessCheck recipeCheck;
				Recipe recipe;
				if(section.contains("Shape")){
					recipeCheck = new ShapedCheck();
					recipe = recipeCreator.getShapedRecipe(section, item, (ShapedCheck) recipeCheck);
				}else{
					recipeCheck = new ShapelessCheck();
					recipe = recipeCreator.getShapelessRecipe(section, item, recipeCheck);
				}
				simpleItem = new SpecialRecipeItem(item, section, recipeCheck);
				plugin.getServer().addRecipe(recipe);
			}else{
				simpleItem = new SimpleItem(item, section);
			}
			this.items.add(simpleItem);

			List<String> consumeActions = section.getStringList("OnConsume");
			List<String> clickActions = section.getStringList("OnClick");
			if(consumeActions != null)
				simpleItem.setConsumeActions(new ItemActionSet(plugin, consumeActions, item, s));

			if(clickActions != null)
				simpleItem.setClickActions(new ItemActionSet(plugin, clickActions, item, s));
		}
	}

	public Optional<SpecialRecipeItem> getRecipeItemFromRecipe(ItemStack[] matrix){
		return items.stream()
				.filter(simpleItem -> simpleItem instanceof SpecialRecipeItem)
				.map(item -> (SpecialRecipeItem)item)
				.filter(recipeItem -> recipeItem.canCraft(matrix))
				.findFirst();
	}

	public Set<SimpleItem> getAllItems(){
		return items;
	}

	public SimpleItem getItemByName(String name){
		for(SimpleItem citem : items){
			if(citem.getName().equalsIgnoreCase(name))
				return citem;
		}
		return null;
	}

	public SimpleItem getItem(ItemStack item){
		for(SimpleItem citem : items){
			if(citem.isSimilar(item))
				return citem;
		}
		return null;
	}

	private ItemStack loadItem(ConfigurationSection section){
		String materialName = section.getString("Material", "");
		if(materialName.startsWith("MI-")){
			return CustomItems.getMythicMobsItem(materialName);
		}
		Material mat = Material.getMaterial(section.getString("Material").toUpperCase());
		if(mat == null){
			plugin.getLogger().info("Invalid material: " + section.getString("Material"));
			return null;
		}

		String name = section.getString("Name");
		int damage = section.getInt("Damage", 0);
		int amount = section.getInt("Amount", 1);

		List<String> lore = section.getStringList("Lore");
		List<ItemEnchant> itemEnchants = getItemEnchants(section.getStringList("Enchantments"));
		List<ItemFlag> itemFlags = getItemFlags(section.getStringList("ItemFlags"));

		return itemBuilder.buildItem(mat, amount, name, damage, itemEnchants, lore, itemFlags);
	}

	private List<ItemFlag> getItemFlags(List<String> flags){
		if(flags != null) {
			ArrayList<ItemFlag> itemFlags = new ArrayList<>();
			for (String s : flags) {
				ItemFlag flag = miscUtil.valueOf(s, s + " is not a valid ItemFlag", ItemFlag.class);
				if(flag != null)
					itemFlags.add(flag);
			}
			return itemFlags;
		}
		return null;
	}

	private List<ItemEnchant> getItemEnchants(List<String> enchants){
		if(enchants != null){
			ArrayList<ItemEnchant> itemEnchants = new ArrayList<>();
			for(String s : enchants){
				String[] enchant = s.split(",");
				if(enchant.length != 2)
					continue;

				Integer level = miscUtil.parseInt(enchant[1]);
				Enchantment enchantment = Enchantment.getByName(enchant[0]);
				if(enchantment == null || level == null ){
					plugin.getLogger().info("Invalid enchantment '" + enchant[0] + "' or level '" + enchant[1] + "'");
					continue;
				}
				itemEnchants.add(new ItemEnchant(enchantment, level));
			}
			return itemEnchants;
		}
		return null;
	}

}
