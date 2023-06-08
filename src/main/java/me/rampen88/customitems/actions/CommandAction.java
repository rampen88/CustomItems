package me.rampen88.customitems.actions;

import me.rampen88.customitems.exceptions.CancelActionsException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandAction implements ItemAction{

	private final String command;
	private final boolean console;

	public CommandAction(String command, boolean console){
		this.command = command;
		this.console = console;
	}

	@Override
	public boolean doTheThing(Player p) throws CancelActionsException{
		String actualCommand = command.replace("%player%", p.getName());
		if(console){
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), actualCommand);
		}else{
			Bukkit.getServer().dispatchCommand(p, actualCommand);
		}
		return false;
	}
}
