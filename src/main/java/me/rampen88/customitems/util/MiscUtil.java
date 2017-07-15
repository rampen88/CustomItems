package me.rampen88.customitems.util;

import me.rampen88.customitems.CustomItems;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MiscUtil {

	private CustomItems plugin;

	public MiscUtil(CustomItems plugin) {
		this.plugin = plugin;
	}

	public Integer parseInt(String input){
		try{

			return Integer.valueOf(input);

		}catch (NumberFormatException ignored){
			plugin.getLogger().info(input + " is not a valid number!");
		}

		return null;
	}

	public String getMessage(String path){
		return ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Messages." + path, "&4Error: Failed to find message. Please inform staff."));
	}

	private String getPermissionMessage(){
		return getMessage("NoPermission");
	}

	public boolean hasPerm(CommandSender sender, String perm, boolean needsPlayer){
		if(sender.hasPermission(perm)){
			if(needsPlayer){
				if(sender instanceof Player)
					return true;
				else{
					sender.sendMessage(getPermissionMessage());
					return false;
				}
			}else return true;
		}else{
			sender.sendMessage(getPermissionMessage());
			return false;
		}
	}

	public List<String> translateColors(List<String> lore){
		List<String> list = new ArrayList<>();
		for(String s : lore){
			list.add(ChatColor.translateAlternateColorCodes('&', s));
		}
		return list;
	}

}
