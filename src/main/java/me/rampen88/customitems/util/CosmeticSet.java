package me.rampen88.customitems.util;

import me.rampen88.customitems.actions.cosmetic.particle.Cosmetic;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CosmeticSet {

	private Map<Integer, Cosmetic> cosmetics = new HashMap<>();
	private String itemId;

	public CosmeticSet(String itemId) {
		this.itemId = itemId;
	}

	public boolean playAll(Player player){
		Set<Integer> toRemove = cosmetics.entrySet().stream()
				.filter(entry -> entry.getValue().play(player))
				.map(Map.Entry::getKey)
				.collect(Collectors.toSet());
		toRemove.forEach(i -> cosmetics.remove(i));
		return cosmetics.isEmpty();
	}

	public void toggleCosmetic(Cosmetic cosmetic){
		if(cosmetics.containsKey(cosmetic.getId())){
			cosmetics.remove(cosmetic.getId());
		}else{
			cosmetics.put(cosmetic.getId(), cosmetic);
		}
	}

	public String getItemId() {
		return itemId;
	}
}
