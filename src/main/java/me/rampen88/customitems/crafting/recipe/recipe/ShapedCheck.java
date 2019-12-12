package me.rampen88.customitems.crafting.recipe.recipe;

import me.rampen88.customitems.crafting.recipe.RecipeItem;
import me.rampen88.customitems.inventory.CustomItemsInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

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

	@Override
	public boolean canCraft(ItemStack[] craftingMatrix){
		List<List<ItemStack>> matrixShape = getListFromMatrix(craftingMatrix);
		List<List<ItemStack>> actualShape = getListFromShape();
		// Compare the shapes
		if(matrixShape.size() != actualShape.size()){
			return false;
		}else{
			for(int i = 0; i < matrixShape.size(); i++){
				List<ItemStack> matrixRow = matrixShape.get(i);
				List<ItemStack> shapeRow = actualShape.get(i);
				if(matrixRow.size() != shapeRow.size()){
					return false;
				}else{
					for(int j = 0; j < matrixRow.size(); j++){
						ItemStack matrixItem = matrixRow.get(j);
						ItemStack shapeItem = shapeRow.get(j);
						// If both are null, keep going through the loop.
						if(shapeItem != null || matrixItem != null){
							// If only one is null, or the item is not similar, it's not the correct recipe.
							if(shapeItem == null || matrixItem == null || !shapeItem.isSimilar(matrixItem) || matrixItem.getAmount() < shapeItem.getAmount()){
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
		List<List<ItemStack>> actualShape = getListFromShape();

		// Compare the shapes
		if(matrixShape.size() != actualShape.size()){
			return null;
		}else{
			for(int i = 0; i < matrixShape.size(); i++){
				List<ItemStack> matrixRow = matrixShape.get(i);
				List<ItemStack> shapeRow = actualShape.get(i);
				if(matrixRow.size() != shapeRow.size()){
					return null;
				}else{
					for(int j = 0; j < matrixRow.size(); j++){
						ItemStack matrixItem = matrixRow.get(j);
						ItemStack shapeItem = shapeRow.get(j);
						// If both are null, keep going through the loop.
						if(shapeItem != null && matrixItem != null){
							if(shapeItem.getAmount() > 1){
								// Remove 1 less than what the recipe requires, since normal crafting removes 1 of each
								matrixItem.setAmount(matrixItem.getAmount() - (shapeItem.getAmount() - 1));
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
		for(int i = 0; i < 9; i += 3){
			if((i == 0 || i == 6) && isNull(craftingMatrix[i], craftingMatrix[i + 1], craftingMatrix[i + 2])){
				continue;
			}
			List<ItemStack> row = new ArrayList<>(Arrays.asList(craftingMatrix).subList(i, i + 3));
			matrixShape.add(row);
		}
		return matrixShape;
	}

	private List<List<ItemStack>> getListFromShape(){
		List<List<ItemStack>> actualShape = new ArrayList<>();
		// Build a list similar to the one above, but for the actual shape
		for(String s : shape){
			List<ItemStack> row = new ArrayList<>();
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
