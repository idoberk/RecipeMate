package com.example.recipemate.Listeners;

import com.example.recipemate.Modals.UserIngredient;

public interface IngredientActionListener {
	void onEditIngredient(int position, UserIngredient ingredient);
	void onIngredientSelectionChanged(int position, boolean isSelected);
}
