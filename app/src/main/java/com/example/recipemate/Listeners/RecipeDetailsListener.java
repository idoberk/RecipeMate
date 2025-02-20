package com.example.recipemate.Listeners;

import com.example.recipemate.Modals.RecipeDetailsResponse;

public interface RecipeDetailsListener {
	void didFetch(RecipeDetailsResponse response, String message);
	void didError(String message);
}
