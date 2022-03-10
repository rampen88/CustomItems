package me.rampen88.customitems.commands.subcommands;

import me.rampen88.customitems.CustomItems;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand{

	public ReloadCommand(CustomItems plugin){
		super(plugin, "custom.items.reload", "reload");
	}

	@Override
	protected void execute(CommandSender sender, String[] args){
		plugin.reload();
		sender.sendMessage(util.getMessage("Commands.Reload"));
	}
}
