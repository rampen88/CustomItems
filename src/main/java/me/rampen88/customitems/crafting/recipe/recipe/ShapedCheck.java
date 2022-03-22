package me.rampen88.customitems.crafting.recipe.recipe;

import me.rampen88.customitems.CustomItems;
import me.rampen88.customitems.crafting.recipe.RecipeItem;
import me.rampen88.customitems.inventory.CustomItemsInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ShapedCheck extends ShapelessCheck{

	private String[] shape;
	private final Map<Character, RecipeItem> ingredientMap = new HashMap<>();

	public void setShape(String[] shape){
		this.shape = shape;
	}

	public void addIngredient(RecipeItem item, int amount, char key){
		ingredientMap.put(key, item);
		super.addIngredient(item, amount);
	}

	@Override
	public Inventory getInventoryShowingRecipe(){
		Inventory inventory = Bukkit.createInventory(new CustomItemsInventoryHolder(), InventoryType.WORKBENCH);
		for(int i = 0; i < shape.length; i++){
			for(int j = 0; j < shape[i].length(); j++){
				RecipeItem itemStack = ingredientMap.get(shape[i].toCharArray()[j]);
				if(itemStack == null)
					continue;
				inventory.setItem(i * 3 + j + 1, itemStack.getItemStack());
			}
		}
		return inventory;
	}

	@Override
	public boolean canCraft(ItemStack[] craftingMatrix){
		List<List<ItemStack>> matrixShape = getListFromMatrix(craftingMatrix);
		List<List<RecipeItem>> actualShape = getListFromShape();
		// Compare the shapes
		if(matrixShape.size() != actualShape.size()){
			return false;
		}else{
			for(int i = 0; i < matrixShape.size(); i++){
				List<ItemStack> matrixRow = matrixShape.get(i);
				List<RecipeItem> shapeRow = actualShape.get(i);
				if(matrixRow.size() != shapeRow.size()){
					return false;
				}else{
					for(int j = 0; j < matrixRow.size(); j++){
						ItemStack matrixItem = matrixRow.get(j);
						RecipeItem shapeItem = shapeRow.get(j);
						// If both are null, keep going through the loop.
						if(shapeItem != null || matrixItem != null){
							// If only one is null, or the item is not similar, it's not the correct recipe.
							if(shapeItem == null || matrixItem == null || !shapeItem.isSimilar(matrixItem) || matrixItem.getAmount() < shapeItem.getItemStack().getAmount()){
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public ItemStack[] removeExtraItemsOnCraft(ItemStack[] craftingMatrix){
		List<List<ItemStack>> matrixShape = getListFromMatrix(craftingMatrix);
		List<List<RecipeItem>> actualShape = getListFromShape();
		// Compare the shapes
		if(matrixShape.size() != actualShape.size()){
			return null;
		}else{
			for(int i = 0; i < matrixShape.size(); i++){
				List<ItemStack> matrixRow = matrixShape.get(i);
				List<RecipeItem> shapeRow = actualShape.get(i);
				if(matrixRow.size() != shapeRow.size()){
					return null;
				}else{
					for(int j = 0; j < matrixRow.size(); j++){
						ItemStack matrixItem = matrixRow.get(j);
						RecipeItem shapeItem = shapeRow.get(j);
						// If both are null, keep going through the loop.
						if(shapeItem != null && matrixItem != null){
							ItemStack shapeItemStack = shapeItem.getItemStack();
							if(shapeItemStack.getAmount() > 1){
								// Remove 1 less than what the recipe requires, since normal crafting removes 1 of each
								matrixItem.setAmount(matrixItem.getAmount() - (shapeItemStack.getAmount() - 1));
							}
						}
					}
				}
			}
		}
		return craftingMatrix;
	}

	private List<List<ItemStack>> getListFromMatrix(ItemStack[] craftingMatrix){
		List<List<ItemStack>> matrixShape = new ArrayList<>();
		if(craftingMatrix.length == 4){ // Crafting in the inventory
			for(int i = 0; i < 4; i += 2){
				if(isNull(craftingMatrix[i], craftingMatrix[i + 1])){
					continue;
				}
				List<ItemStack> row = new ArrayList<>(Arrays.asList(craftingMatrix).subList(i, i + 2));
				matrixShape.add(row);
			}
			if(matrixShape.size() == 0)
				matrixShape.add(new ArrayList<>(Arrays.asList(craftingMatrix).subList(0, 1)));
		}else{ // Crafting with a crafting table, probably.
			for(int i = 0; i < 9; i += 3){
				// Basically, only check if the middle row is empty if the first row was also empty.
				if(((i == 3 && matrixShape.size() != 1) || i == 0 || i == 6) && isNull(craftingMatrix[i], craftingMatrix[i + 1], craftingMatrix[i + 2])){
					continue;
				}
				List<ItemStack> row = new ArrayList<>(Arrays.asList(craftingMatrix).subList(i, i + 3));
				matrixShape.add(row);
			}
			if(matrixShape.size() == 0)
				matrixShape.add(new ArrayList<>(Arrays.asList(craftingMatrix).subList(0, 2)));
		}
		// Remove any trailing NULL values in the lists.
		for(List<ItemStack> row : matrixShape){
			for(int j = (row.size() - 1); j >= 0; j--){
				if(row.get(j) == null){
					row.remove(j);
				}else{
					break;
				}
			}
		}
		// Get the longest row
		int longestRow = 0;
		for(List<ItemStack> itemStacks : matrixShape){
			longestRow = Math.max(itemStacks.size(), longestRow);
		}
		// Add trailing NULLs to match the longest row
		for(List<ItemStack> itemStacks : matrixShape){
			while(itemStacks.size() < longestRow){
				itemStacks.add(null);
			}
		}
		removeFirstElementOfEachRowIfAllAreNull(matrixShape);
		removeFirstElementOfEachRowIfAllAreNull(matrixShape);
		return matrixShape;
	}

	private void removeFirstElementOfEachRowIfAllAreNull(List<List<ItemStack>> matrixShape){
		if(matrixShape.size() == 0){
			return;
		}
		for(List<ItemStack> itemStacks : matrixShape){
			if(itemStacks.size() == 0)
				return;
		}
		ItemStack item = null;
		for(List<ItemStack> itemStacks : matrixShape){
			if(item == null)
				item = itemStacks.get(0);
		}
		if(item == null){ // All started with null
			for(List<ItemStack> itemStacks : matrixShape){
				itemStacks.remove(0);
			}
		}
	}

	private List<List<RecipeItem>> getListFromShape(){
		List<List<RecipeItem>> actualShape = new ArrayList<>();
		// Build a list similar to the one above, but for the actual shape
		for(String s : shape){
			List<RecipeItem> row = new ArrayList<>();
			for(char c : s.toCharArray()){
				row.add(ingredientMap.get(c));
			}
			actualShape.add(row);
		}
		return actualShape;
	}

	private boolean isNull(Object... objects){
		for(Object o : objects){
			if(o != null)
				return false;
		}
		return true;
	}

}
