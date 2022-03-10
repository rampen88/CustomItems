package me.rampen88.customitems.commands.subcommands;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.crafting.SimpleItem;
import me.rampen88.customitems.crafting.SpecialRecipeItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ViewRecipeCommand extends SubCommand{

	public ViewRecipeCommand(CustomItems plugin){
		super(plugin, "custom.items.view", "view");
	}

	@Override
	protected void execute(CommandSender sender, String[] args){
		if(args.length < 2){
			sender.sendMessage(util.getMessage("Commands.View.Usage"));
			return;
		}else if(!(sender instanceof Player)){
			sender.sendMessage(util.getMessage("NoPermission"));
			return;
		}
		SimpleItem item = plugin.getItemHandler().getItemByName(args[1]);
		if((item instanceof SpecialRecipeItem)){
			SpecialRecipeItem specialRecipeItem = (SpecialRecipeItem) item;
			Inventory inventory = specialRecipeItem.getInventoryShowingRecipe();
			inventory.setItem(0, item.getItem()); // Set the result in the crafting bench to what it should be.
			((Player)sender).openInventory(inventory);
		}else{
			sender.sendMessage(util.getMessage("Commands.View.NoRecipe"));
		}
	}

}
