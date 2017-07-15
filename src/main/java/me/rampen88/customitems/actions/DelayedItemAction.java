package me.rampen88.customitems.actions;

import me.rampen88.customitems.CustomItems;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DelayedItemAction implements ItemAction{

	private CustomItems plugin;
	private ItemAction toDelay;
	private int delay;

	DelayedItemAction(ItemAction toDelay, int delay, CustomItems plugin) {
		this.toDelay = toDelay;
		this.delay = delay;
		this.plugin = plugin;
	}

	@Override
	public boolean doTheThing(Player p) {
		new BukkitRunnable() {

			@Override
			public void run() {
				toDelay.doTheThing(p);
			}

		}.runTaskLater(plugin, delay);
		return false;
	}
}
