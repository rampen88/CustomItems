package me.rampen88.customitems.commands.subcommands;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.util.MiscUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;

public abstract class SubCommand {
	private String[] aliases;
	private String permission;
	protected CustomItems plugin;
	protected MiscUtil util;

	public SubCommand(CustomItems plugin, String permission, String... aliases){
		this.plugin = plugin;
		this.permission = permission;
		this.aliases = aliases;
		util = plugin.getMiscUtil();
	}

	public boolean isAlias(String string){
		for(String s : aliases){
			if(s.equalsIgnoreCase(string))
				return true;
		}
		return false;
	}

	public boolean hasPermission(Permissible permissible){
		return permissible.hasPermission(permission);
	}

	public void executeCommand(CommandSender sender, String[] args){
		if(hasPermission(sender)){
			execute(sender, args);
		}else{
			sender.sendMessage(util.getMessage("NoPermission"));
		}
	}

	protected abstract void execute(CommandSender sender, String[] args);

}
