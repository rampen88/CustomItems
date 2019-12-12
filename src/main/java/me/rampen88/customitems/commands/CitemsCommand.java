package me.rampen88.customitems.commands;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.crafting.ItemHandler;
import me.rampen88.customitems.crafting.SimpleItem;
import me.rampen88.customitems.crafting.SpecialRecipeItem;
import me.rampen88.customitems.util.MiscUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CitemsCommand implements CommandExecutor{

	private CustomItems plugin;
	private ItemHandler itemHandler;
	private MiscUtil util;

	public CitemsCommand(CustomItems plugin, ItemHandler itemHandler) {
		this.plugin = plugin;
		this.itemHandler = itemHandler;

		util = plugin.getMiscUtil();
	}

	// TODO: add command to get 1 of each item.
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
		if(args.length < 1){
			helpCommand(commandSender);
			return true;
		}else{
			switch (args[0].toLowerCase()){
				case "list":
					if(!util.hasPerm(commandSender, "custom.items.list", false))
						break;

					listCommand(commandSender);
					break;
				case "get":
					if(!util.hasPerm(commandSender, "custom.items.get", false))
						break;

					// Util hasPerm checks if commandSender is player, so it should be safe to cast.
					getCommand((Player) commandSender, args);
					break;
				case "give":
					if(!util.hasPerm(commandSender, "custom.items.give", false))
						break;

					giveCommand(commandSender, args);
					break;
				case "help":
					helpCommand(commandSender);
					break;
				case "view":
					if(!util.hasPerm(commandSender, "custom.items.view", true))
						break;
					viewRecipe((Player) commandSender, args);
					break;
				case "reload":
					if(!util.hasPerm(commandSender, "custom.items.reload", false))
						break;
					plugin.reload();
					commandSender.sendMessage(util.getMessage("Commands.Reload"));
					break;
				default:
					commandSender.sendMessage(util.getMessage("UnknownCommand"));
					break;
			}
			return true;
		}
	}

	private void helpCommand(CommandSender target){
		if(!util.hasPerm(target, "custom.items.help", false)) return;

		List<String> help = plugin.getConfig().getStringList("Messages.Commands.Help");
		help.add("&7" + plugin.getDescription().getName() + " version "+ plugin.getDescription().getVersion() + " made by rampen88");

		help = util.translateColors(help);
		help.forEach(target::sendMessage);
	}

	private void getCommand(Player target, String[] args){
		if(args.length < 3){
			target.sendMessage(util.getMessage("Commands.Get.Usage"));
			return;
		}
		giveItem(target, target, args[1], args[2], false);
	}

	private void giveItem(CommandSender sender, Player target, String itemName, String amountString, boolean sendMessageToSender){
		SimpleItem item = itemHandler.getItemByName(itemName);
		if(item == null){
			sender.sendMessage(util.getMessage("Commands.ItemError"));
			return;
		}

		Integer amount = util.parseInt(amountString);
		if(amount == null){
			sender.sendMessage(util.getMessage("Commands.NotANumber"));
			return;
		}

		ItemStack itemStack = item.getItem();
		itemStack.setAmount(amount);

		target.getInventory().addItem(itemStack);
		target.sendMessage(util.getMessage("Commands.Received").replace("%amount%", amount.toString()).replace("%item%", item.getName()));
		if(sendMessageToSender)
			sender.sendMessage(util.getMessage("Commands.Give.Success").replace("%amount%", amount.toString()).replace("%item%", item.getName()).replace("%player%", target.getName()));
	}

	private void giveCommand(CommandSender sender, String[] args){
		if(args.length < 4){
			sender.sendMessage(util.getMessage("Commands.Give.Usage"));
			return;
		}

		Player player = Bukkit.getPlayer(args[1]);
		if(player == null){
			sender.sendMessage(util.getMessage("Commands.Give.NotOnline").replace("%player%", args[1]));
			return;
		}
		giveItem(sender, player, args[2], args[3], true);
	}

	private void listCommand(CommandSender target){
		StringBuilder stringBuilder = new StringBuilder();

		// Append all item names in the StringBuilder, then remove the last ", "
		itemHandler.getAllItems().forEach(i -> stringBuilder.append(i.getName()).append(", "));
		stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());

		target.sendMessage(util.getMessage("Commands.List.Items"));
		target.sendMessage(stringBuilder.toString());
	}

	private void viewRecipe(Player sender, String[] args){
		if(args.length < 2){
			sender.sendMessage(util.getMessage("Commands.View.Usage"));
			return;
		}

		SimpleItem item = plugin.getItemHandler().getItemByName(args[1]);
		if((item instanceof SpecialRecipeItem)){
			SpecialRecipeItem specialRecipeItem = (SpecialRecipeItem) item;
			Inventory inventory = specialRecipeItem.getInventoryShowingRecipe();
			inventory.setItem(0, item.getItem()); // Set the result in the crafting bench to what it should be.
			sender.openInventory(inventory);
		}else{
			sender.sendMessage(util.getMessage("Commands.View.NoRecipe"));
		}
	}

}
