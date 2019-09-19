package me.rampen88.customitems.crafting.recipe.recipe;

import me.rampen88.customitems.crafting.recipe.RecipeItem;
import me.rampen88.customitems.inventory.CustomItemsInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ShapedCheck extends ShapelessCheck{

	private String[] shape;
	private Map<Character, ItemStack> ingredientMap = new HashMap<>();

	public void setShape(String[] shape){
		this.shape = shape;
	}

	public void addIngredient(RecipeItem item, int amount, char key){
		ingredientMap.put(key, item.getItemStack());
		super.addIngredient(item, amount);
	}

	@Override
	public Inventory getInventoryShowingRecipe(){
		Inventory inventory = Bukkit.createInventory(new CustomItemsInventoryHolder(), InventoryType.WORKBENCH);
		for(int i = 0; i < shape.length; i++){
			for(int j = 0; j < shape[i].length(); j++){
				ItemStack itemStack = ingredientMap.get(shape[i].toCharArray()[j]);
				if(itemStack == null)
					continue;
				inventory.setItem(i * 3 + j + 1, itemStack);
			}
		}
		return inventory;
	}
}
