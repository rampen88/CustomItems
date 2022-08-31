package me.rampen88.customitems.crafting.recipe.item;

import me.rampen88.customitems.CustomItems;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import javax.naming.NameAlreadyBoundException;

public class CustomPotionItem extends PotionItem{

	private PotionEffectType type;
	private int duration;
	private int amplifier;

	public CustomPotionItem(PotionEffectType type, int duration, int amplifier){
		super(PotionType.UNCRAFTABLE, false, false);
		this.type = type;
		this.duration = duration;
		this.amplifier = amplifier;
	}

	@Override
	public boolean isSimilar(ItemStack itemStack){
		if(super.isSimilar(itemStack)){
			PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
			CustomItems.getInstance().getLogger().info("Required potion type: " + type + ", duration: " + duration + ", amplifier: " + amplifier);
			for(PotionEffect customEffect : meta.getCustomEffects()){
				CustomItems.getInstance().getLogger().info("Custom effect from potion is: type: " + customEffect.getType() + ", duration: " + customEffect.getDuration() + ", amplifier: " + customEffect.getAmplifier());
				if(customEffect.getType() == type && customEffect.getDuration() == duration && customEffect.getAmplifier() == amplifier){
					CustomItems.getInstance().getLogger().info("Potions matched!");
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public ItemStack getItemStack(){
		ItemStack basePotion = super.getItemStack();
		PotionEffect effect = new PotionEffect(type, duration, amplifier);
		PotionMeta meta = (PotionMeta) basePotion.getItemMeta();
		meta.addCustomEffect(effect, true);
		basePotion.setItemMeta(meta);
		return basePotion;
	}
}
