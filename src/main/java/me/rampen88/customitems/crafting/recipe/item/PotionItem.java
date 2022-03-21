package me.rampen88.customitems.crafting.recipe.item;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.crafting.recipe.RecipeItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class PotionItem implements RecipeItem {

	private final PotionType type;
	private final boolean extended;
	private final boolean upgraded;

	public PotionItem(PotionType type, boolean extended, boolean upgraded){
		this.type = type;
		this.extended = extended;
		this.upgraded = upgraded;
	}

	@Override
	public boolean isSimilar(ItemStack itemStack){
		if(itemStack.getItemMeta() instanceof PotionMeta){
			PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
			PotionData baseData = meta.getBasePotionData();
			return baseData.getType() == type && baseData.isExtended() == extended && baseData.isUpgraded() == upgraded;
		}
		return false;
	}

	@Override
	public ItemStack getItemStack(){
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		PotionData data = new PotionData(type, extended, upgraded);
		meta.setBasePotionData(data);
		item.setItemMeta(meta);
		return item;
	}
}
