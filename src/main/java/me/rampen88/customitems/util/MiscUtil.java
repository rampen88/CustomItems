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

	public Double parseDouble(String input){
		try{
			return Double.valueOf(input);
		}catch (NumberFormatException ignored){
			plugin.getLogger().info(input + " is not a valid number!");
		}
		return null;
	}

	public <T extends Enum<T>> T valueOf(String toParse, String errorMessage, Class<T> clazz){
		try{
			return T.valueOf(clazz, toParse);
		}catch (IllegalArgumentException | NullPointerException e){
			plugin.getLogger().info(errorMessage);
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

	public static int countCharInString(char key, String string){
		int total = 0;
		for (char c : string.toCharArray()) {
			if(c == key)
				total++;
		}
		return total;
	}

}
