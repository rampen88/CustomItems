package me.rampen88.customitems.commands;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.commands.subcommands.*;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CitemsCommand implements CommandExecutor{

	private MiscUtil util;
	private Set<SubCommand> subCommands = new HashSet<>();

	public CitemsCommand(CustomItems plugin) {
		util = plugin.getMiscUtil();
		subCommands.add(new ReloadCommand(plugin));
		subCommands.add(new HelpCommand(plugin));
		subCommands.add(new GiveCommand(plugin));
		subCommands.add(new ListCommand(plugin));
		subCommands.add(new ViewRecipeCommand(plugin));
	}

	// TODO: add command to get 1 of each item.
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
		SubCommand subCommand = getSubCommand(args.length == 0 ? "help" : args[0].toLowerCase());
		if(subCommand != null)
			subCommand.executeCommand(commandSender, args);
		else
			commandSender.sendMessage(util.getMessage("UnknownCommand"));
		return true;
	}

	private SubCommand getSubCommand(String cmd){
		for (SubCommand subCommand : subCommands) {
			if(subCommand.isAlias(cmd))
				return subCommand;
		}
		return null;
	}

}
