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
			CustomItems.getInstance().getLogger().info("Crafting Table Potion Item:" + itemStack);
			CustomItems.getInstance().getLogger().info("Config Potion Item:" + getItemStack());
			PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
			PotionData baseData = meta.getBasePotionData();
			boolean isSimilar = baseData.getType() == type && baseData.isExtended() == extended && baseData.isUpgraded() == upgraded;
			CustomItems.getInstance().getLogger().info("Base Potion is similar: " + isSimilar);
			return isSimilar;
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
