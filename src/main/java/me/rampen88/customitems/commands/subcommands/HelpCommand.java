package me.rampen88.customitems.commands.subcommands;

import me.rampen88.customitems.CustomItems;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpCommand extends SubCommand{

	public HelpCommand(CustomItems plugin){
		super(plugin, "custom.items.help", "help");
	}

	@Override
	protected void execute(CommandSender sender, String[] args){
		List<String> help = plugin.getConfig().getStringList("Messages.Commands.Help");
		help.add("&7" + plugin.getDescription().getName() + " version "+ plugin.getDescription().getVersion() + " made by rampen88");

		help = util.translateColors(help);
		help.forEach(sender::sendMessage);
	}
}
