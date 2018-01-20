package me.rampen88.customitems.crafting.recipe.recipe;

import me.rampen88.customitems.crafting.recipe.RecipeCheck;
import me.rampen88.customitems.crafting.recipe.RecipeItem;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ShapelessCheck implements RecipeCheck {

	private Map<RecipeItem, Integer> ingredients = new HashMap<>();

	public void addIngredient(RecipeItem item, int amount){
		ingredients.compute(item, (k, v) -> v == null ? amount : v + amount);
	}

	@Override
	public boolean canCraft(ItemStack[] craftingMatrix) {
		Map<ItemStack, Integer> current = Arrays.stream(craftingMatrix)
				.filter(Objects::nonNull)
				.map(ItemStack::new) // Create new ItemStack objects so it doesn't set the amount of the actual ItemStack to 1.
				.peek(key -> key.setAmount(1)) // ItemStacks with different amount of items count as different keys for the map, so set amount to 1
				.collect(Collectors.toMap(key -> key, key -> 1, (oldValue, newValue) -> ++oldValue));

		for(Map.Entry<ItemStack, Integer> entry : current.entrySet()){
			RecipeItem item = get(entry.getKey());
			if(item == null || entry.getValue().intValue() != ingredients.get(item).intValue()){
				return false;
			}
		}
		return true;
	}

	private RecipeItem get(ItemStack toGet){
		for(Map.Entry<RecipeItem, Integer> entry : ingredients.entrySet()){
			if(entry.getKey().isSimilar(toGet)){
				return entry.getKey();
			}
		}
		return null;
	}

}
