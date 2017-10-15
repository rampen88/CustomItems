package me.rampen88.customitems.actions;

import me.rampen88.customitems.exceptions.CancelActionsException;
import org.bukkit.entity.Player;

public interface ItemAction {

	// Im out of ideas for method names, okay?
	boolean doTheThing(Player p) throws CancelActionsException;

}
