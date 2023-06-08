package me.rampen88.customitems;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import me.rampen88.customitems.commands.CitemsCommand;
import me.rampen88.customitems.crafting.ItemHandler;
import me.rampen88.customitems.hooks.BreweryHook;
import me.rampen88.customitems.listener.InventoryListener;
import me.rampen88.customitems.listener.ItemListener;
import me.rampen88.customitems.listener.CosmeticHandler;
import me.rampen88.customitems.recipe.RecipeCreator;
import me.rampen88.customitems.util.ItemBuilder;
import me.rampen88.customitems.util.MiscUtil;

import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomItems extends JavaPlugin {

	private static Logger logger;
	private static ItemBuilder itemBuilder = new ItemBuilder();
	private static boolean mythicMobsEnabled = false;
	private static CustomItems instance;

	private RecipeCreator recipeCreator;
	private ItemHandler itemHandler;
	private BreweryHook breweryHook;
	private MiscUtil miscUtil;

	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		miscUtil = new MiscUtil(this);

		mythicMobsEnabled = getServer().getPluginManager().isPluginEnabled("MythicMobs");
		if(getServer().getPluginManager().isPluginEnabled("Brewery")){
			breweryHook = new BreweryHook();
		}
		recipeCreator = new RecipeCreator(this);

		itemHandler = new ItemHandler(this, miscUtil);
		itemHandler.loadItems();

		registerListeners();
		registerCommands();
		setLoggerLevel();
	}

	private void registerListeners(){
		PluginManager pluginManager = getServer().getPluginManager();

		pluginManager.registerEvents(new ItemListener(this, itemHandler), this);
		pluginManager.registerEvents(new CosmeticHandler(this), this);
		pluginManager.registerEvents(new InventoryListener(itemHandler), this);
	}

	private void registerCommands(){
		PluginCommand citems = getCommand("citems");
		citems.setExecutor(new CitemsCommand(this));
		citems.setAliases(Arrays.asList("citem","customitem","customitems"));
	}

	@Override
	public void onDisable() {
		HandlerList.unregisterAll(this);
	}

	public void reload(){
		reloadConfig();
		setLoggerLevel();
		itemHandler.reload();
	}

	public void setLoggerLevel(){
		String llevel = getConfig().getString("LoggerLevel", "warning");
		try{
			Level lvl = Level.parse(llevel);
			getLogger().setLevel(lvl);
			Logger.getLogger("[CustomItems]").info("Logger level set to: " + lvl);
		}catch(NullPointerException | IllegalArgumentException e){
			getLogger().setLevel(Level.WARNING);
			getLogger().warning("Invalid Logger Level in config, setting it to Warning. (900)");
		}
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

	public BreweryHook getBreweryHook(){
		return breweryHook;
	}

	public static ItemStack getMythicMobsItem(String name){
		if(mythicMobsEnabled){
			try{
				String itemName = name.startsWith("MI-")
						? name.substring(3)
						: name;
				Optional<MythicItem> mythicItem = MythicMobs.inst().getItemManager().getItem(itemName);
				return mythicItem.map(item -> BukkitAdapter.adapt(item.generateItemStack(1))).orElse(null);
			}catch(Exception e){
				e.printStackTrace();
				getInstance().getLogger().warning("Error attempting to get item '" + name + "' from MythicMobs");
				return null;
			}
		}else{
			System.out.println("[CustomItems] Attempted to get MythicMobs item " + name + " but mythic mobs is not enabled on the server?");
		}
		return null;
	}

	public static CustomItems getInstance(){
		return instance;
	}
}