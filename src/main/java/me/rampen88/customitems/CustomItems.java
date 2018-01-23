package me.rampen88.customitems;

import me.rampen88.customitems.commands.CitemsCommand;
import me.rampen88.customitems.crafting.ItemHandler;
import me.rampen88.customitems.listener.ItemListener;
import me.rampen88.customitems.listener.CosmeticHandler;
import me.rampen88.customitems.recipe.KeyRecipeCreator;
import me.rampen88.customitems.recipe.RecipeCreator;
import me.rampen88.customitems.util.ItemBuilder;
import me.rampen88.customitems.util.MiscUtil;

import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CustomItems extends JavaPlugin {

	private static ItemBuilder itemBuilder = new ItemBuilder();

	private RecipeCreator recipeCreator;
	private ItemHandler itemHandler;
	private MiscUtil miscUtil;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		miscUtil = new MiscUtil(this);

		List<String> versionsToCheck = getConfig().getStringList("VersionCheck");
		if(versionsToCheck == null)
			versionsToCheck = Collections.singletonList("MC: 1.11");

		String version = getServer().getVersion();

		for(String s : versionsToCheck){
			if(version.contains(s)){
				recipeCreator = new RecipeCreator(this);
				break;
			}
		}
		if(recipeCreator == null)
			recipeCreator = new KeyRecipeCreator(this);

		itemHandler = new ItemHandler(this, miscUtil);
		itemHandler.loadItems();

		registerListeners();
		registerCommands();
	}

	private void registerListeners(){
		PluginManager pluginManager = getServer().getPluginManager();

		pluginManager.registerEvents(new ItemListener(this, itemHandler), this);
		pluginManager.registerEvents(new CosmeticHandler(this), this);
	}

	private void registerCommands(){
		PluginCommand citems = getCommand("citems");
		citems.setExecutor(new CitemsCommand(this, itemHandler));
		citems.setAliases(Arrays.asList("citem","customitem","customitems"));
	}

	@Override
	public void onDisable() {
		HandlerList.unregisterAll(this);
	}

	public RecipeCreator getRecipeCreator() {
		return recipeCreator;
	}

	public MiscUtil getMiscUtil() {
		return miscUtil;
	}

	public static ItemBuilder getItemBuilder() {
		return itemBuilder;
	}

	public ItemHandler getItemHandler() {
		return itemHandler;
	}
}