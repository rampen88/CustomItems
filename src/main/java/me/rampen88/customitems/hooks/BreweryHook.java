package me.rampen88.customitems.hooks;

import com.dre.brewery.BPlayer;
import com.dre.brewery.api.BreweryApi;
import org.bukkit.entity.Player;

public class BreweryHook {

	public boolean removeDrunkenness(Player player, int amount){
		BPlayer p = BreweryApi.getBPlayer(player);
		if(p != null && p.getDrunkeness() > 0){
			amount = Math.max(p.getDrunkeness() - amount, 0);
			BreweryApi.setPlayerDrunk(player, amount, p.getQuality());
			return true;
		}
		return false;
	}

	public void showDrunkenness(Player player){
		BPlayer p = BreweryApi.getBPlayer(player);
		if(p != null){
			p.showDrunkeness(player);
		}
	}

}
