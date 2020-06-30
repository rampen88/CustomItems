package me.rampen88.customitems.util;

import me.rampen88.customitems.CustomItems;

import me.rampen88.rampencore.util.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MiscUtil {

	private CustomItems plugin;
	private MessageUtil messageUtil;

	public MiscUtil(CustomItems plugin) {
		this.plugin = plugin;
		messageUtil = new MessageUtil(plugin);
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
		return messageUtil.getMessage(path);
	}

	public boolean hasPerm(CommandSender sender, String perm, boolean needsPlayer){
		return messageUtil.hasPerm(sender, perm, needsPlayer);
	}

	public List<String> translateColors(List<String> lore){
		List<String> list = new ArrayList<>();
		for(String s : lore){
			list.add(MessageUtil.translateColors(s));
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
