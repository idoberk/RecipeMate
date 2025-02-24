package com.example.recipemate.Listeners;

import com.example.recipemate.Modals.Recipe;

import java.util.List;

public interface UserRecipesCallback {
	void onSuccess(List<Recipe> recipes);
	void onError(String error);
}
