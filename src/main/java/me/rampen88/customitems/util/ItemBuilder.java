package me.rampen88.customitems.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {


	public ItemStack buildItem(Material m, int amount, String name, int damage, List<ItemEnchant> enchants, List<String> lore, List<ItemFlag>  flags){

		ItemStack item = new ItemStack(m, amount, (short)damage);

		ItemMeta meta = item.getItemMeta();

		// Attempt to add color to ItemName and lore
		if(name != null) meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		if(lore != null) meta.setLore(translateColors(lore));


		if(enchants != null){
			enchants.forEach(e -> meta.addEnchant(e.getEnchantment(), e.getLevel(), true));
		}

		if(flags != null){
			flags.forEach(meta::addItemFlags);
		}

		item.setItemMeta(meta);
		return item;
	}

	private List<String> translateColors(List<String> lore){
		List<String> list = new ArrayList<>();
		for(String s : lore){
			list.add(ChatColor.translateAlternateColorCodes('&', s));
		}
		return list;
	}

}
