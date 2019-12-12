package me.rampen88.customitems.crafting.recipe.item;

import me.rampen88.customitems.crafting.SimpleItem;
import me.rampen88.customitems.crafting.recipe.RecipeItem;
import org.bukkit.inventory.ItemStack;

public class CustomItem implements RecipeItem {

	private SimpleItem item;
	private int amount;

	public CustomItem(SimpleItem item, int amount) {
		this.item = item;
		this.amount = amount;
	}

	@Override
	public boolean isSimilar(ItemStack itemStack) {
		return item.isSimilar(itemStack);
	}

	@Override
	public ItemStack getItemStack(){
		ItemStack itemStack = item.getItem();
		itemStack.setAmount(amount);
		return itemStack;
	}


}
