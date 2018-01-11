package me.rampen88.customitems.listener;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.actions.particle.Playable;
import me.rampen88.customitems.actions.particle.PlayableParticle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

public class ParticleHandler implements Listener{

	private static Map<Player, Playable> playerParticles = new WeakHashMap<>();

	public ParticleHandler(CustomItems plugin) {
		new BukkitRunnable(){
			@Override
			public void run() {
				playParticles();
			}
		}.runTaskTimer(plugin,0, 1);
	}

	@EventHandler
	public void playerLeave(PlayerQuitEvent event){
		playerParticles.remove(event.getPlayer());
	}

	private void playParticles(){
		Set<Player> toRemove = playerParticles.entrySet().stream()
				.filter(entry -> !entry.getValue().play(entry.getKey()))
				.map(Map.Entry::getKey)
				.collect(Collectors.toSet());
		toRemove.forEach(player -> playerParticles.remove(player));
	}

	public static void toggleParticles(Player player, Playable particle){
		Playable current = playerParticles.get(player);
		if(current != null || particle == null){
			playerParticles.remove(player);
		}else{
			playerParticles.put(player, particle);
		}
	}


}
