package me.rampen88.customitems.actions;

import me.rampen88.customitems.exceptions.CancelActionsException;
import org.bukkit.entity.Player;

public class MessageAction implements ItemAction {

	private String message;

	MessageAction(String message) {
		this.message = message;
	}

	@Override
	public boolean doTheThing(Player p) throws CancelActionsException {
		p.sendMessage(message);
		return false;
	}
}
