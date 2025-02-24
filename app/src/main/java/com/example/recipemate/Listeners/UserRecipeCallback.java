package com.example.recipemate.Listeners;

import com.example.recipemate.Modals.Recipe;

public interface UserRecipeCallback {
	void onSuccess(Recipe recipe);
	void onError(String error);
}
