package me.rampen88.customitems.crafting;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.actions.ItemActionSet;
import me.rampen88.customitems.recipe.RecipeCreator;
import me.rampen88.customitems.util.ItemBuilder;
import me.rampen88.customitems.util.ItemEnchant;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemMaster {

	private ItemBuilder itemBuilder = CustomItems.getItemBuilder();
	private CustomItems plugin;

	private HashSet<SimpleItem> items = new HashSet<>();

	public ItemMaster(CustomItems plugin) {
		this.plugin = plugin;
		loadItems();
	}

	private void loadItems(){

		ConfigurationSection items = plugin.getConfig().getConfigurationSection("Items");

		for(String s : items.getKeys(false)){

			ConfigurationSection section = items.getConfigurationSection(s);
			ItemStack item = loadItem(section.getConfigurationSection("ResultItem"));

			SimpleItem simpleItem = new SimpleItem(item, section);
			this.items.add(simpleItem);

			// Add recipe if section contains required things.
			if(section.contains("Ingredients")){
				RecipeCreator recipeCreator = plugin.getRecipeCreator();
				Recipe recipe = section.contains("Shape") ? recipeCreator.getShapedRecipe(section, item) : recipeCreator.getShapelessRecipe(section, item);

				plugin.getServer().addRecipe(recipe);
			}

			List<String> consumeActions = section.getStringList("OnConsume");
			List<String> clickActions = section.getStringList("OnClick");
			if(consumeActions != null)
				simpleItem.setConsumeActions(new ItemActionSet(plugin, consumeActions, item));

			if(clickActions != null)
				simpleItem.setClickActions(new ItemActionSet(plugin, clickActions, item));
		}
	}

	public Set<SimpleItem> getAllItems(){
		return items;
	}

	public SimpleItem getItemByName(String name){
		for(SimpleItem citem : items){
			if(!citem.getName().equalsIgnoreCase(name)) continue;

			return citem;
		}
		return null;
	}

	public SimpleItem getItem(ItemStack item){
		for(SimpleItem citem : items){
			if(!citem.isSimilar(item)) continue;

			return citem;
		}
		return null;
	}

	private ItemStack loadItem(ConfigurationSection section){

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

				try {

					itemFlags.add(ItemFlag.valueOf(s));

				} catch (IllegalArgumentException | NullPointerException e) {
					plugin.getLogger().info("Invalid ItemFlag: " + s);
				}
			}
			return itemFlags;
		}
		return null;
	}

	private List<ItemEnchant> getItemEnchants(List<String> ench){
		if(ench != null){

			ArrayList<ItemEnchant> itemEnchants = new ArrayList<>();

			for(String s : ench){

				String[] enchant = s.split(",");

				// TODO: log to console
				if(enchant.length != 2) continue;

				try{

					int level = Integer.valueOf(enchant[1]);
					Enchantment enchantment = Enchantment.getByName(enchant[0]);

					if(enchantment == null){
						plugin.getLogger().info("Invalid enchantment: " + enchant[0]);
						continue;
					}

					itemEnchants.add(new ItemEnchant(enchantment, level));

				}catch (NumberFormatException e){
					plugin.getLogger().info("Invalid enchantment level: " + enchant[1]);
				}
			}
			return itemEnchants;
		}
		return null;
	}

}
