package me.rampen88.customitems.listener;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.actions.cosmetic.particle.Cosmetic;
import me.rampen88.customitems.util.CosmeticSet;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

public class CosmeticHandler implements Listener{

	private static Map<Player, CosmeticSet> playerCosmetics = new WeakHashMap<>();

	public CosmeticHandler(CustomItems plugin) {
		new BukkitRunnable(){
			@Override
			public void run() {
				playParticles();
			}
		}.runTaskTimer(plugin,0, 1);
	}

	@EventHandler
	public void playerLeave(PlayerQuitEvent event){
		playerCosmetics.remove(event.getPlayer());
	}

	private void playParticles(){
		Set<Player> toRemove = playerCosmetics.entrySet().stream()
				.filter(entry -> entry.getValue().playAll(entry.getKey()))
				.map(Map.Entry::getKey)
				.collect(Collectors.toSet());
		toRemove.forEach(player -> playerCosmetics.remove(player));
	}

	public static void toggleCosmetic(Player player, Cosmetic cosmetic){
		CosmeticSet current = playerCosmetics.get(player);
		if((current != null && !current.getItemId().equalsIgnoreCase(cosmetic.getItemId())) || cosmetic == null){
			playerCosmetics.remove(player);
		}else{
			current = playerCosmetics.computeIfAbsent(player, player1 -> new CosmeticSet(cosmetic.getItemId()));
			current.toggleCosmetic(cosmetic);
		}
	}

}
