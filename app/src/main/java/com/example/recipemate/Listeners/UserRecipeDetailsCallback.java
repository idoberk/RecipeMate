package com.example.recipemate.Listeners;

import com.example.recipemate.Modals.Recipe;

public interface UserRecipeDetailsCallback {
	void didFetch(Recipe recipe, String message);
	void didError(String message);
}
