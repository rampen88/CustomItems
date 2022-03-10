package me.rampen88.customitems.commands.subcommands;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.crafting.ItemHandler;
import org.bukkit.command.CommandSender;

public class ListCommand extends SubCommand{

	private ItemHandler itemHandler;

	public ListCommand(CustomItems plugin){
		super(plugin, "custom.items.list", "list");
		itemHandler = plugin.getItemHandler();
	}

	@Override
	protected void execute(CommandSender sender, String[] args){
		StringBuilder stringBuilder = new StringBuilder();

		// Append all item names in the StringBuilder, then remove the last ", "
		itemHandler.getAllItems().forEach(i -> stringBuilder.append(i.getName()).append(", "));
		stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());

		sender.sendMessage(util.getMessage("Commands.List.Items"));
		sender.sendMessage(stringBuilder.toString());
	}
}
