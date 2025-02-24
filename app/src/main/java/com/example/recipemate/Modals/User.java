package com.example.recipemate.Modals;

import java.util.ArrayList;

public class User {
	private String user_id;
	private String user_email;
	private ArrayList<Recipe> favorite_recipes;

	public User() {
		favorite_recipes = new ArrayList<>();
	}

	public User(String id, String email) {
		this.user_id = id;
		this.user_email = email;
		this.favorite_recipes = new ArrayList<>();
	}

	public User(String id, String email, ArrayList<Recipe> favorite_recipes) {
		this.user_id = id;
		this.user_email = email;
		this.favorite_recipes = favorite_recipes;
	}

	public String getUserid() {
		return user_id;
	}

	public void setUserid(String id) {
		this.user_id = id;
	}

	public String getUseremail() {
		return user_email;
	}

	public void setUseremail(String email) {
		this.user_email = email;
	}

	public ArrayList<Recipe> getFavoriteRecipes() {
		return favorite_recipes;
	}

	public void setFavoriteRecipes(ArrayList<Recipe> recipes) {
		this.favorite_recipes = recipes;
	}

	public void addFavoriteRecipe(Recipe recipe) {
		if (!isFavoriteRecipe(recipe)) {
			favorite_recipes.add(recipe);
		}
	}

	public void removeFavoriteRecipe(Recipe recipe) {
		favorite_recipes.removeIf(rec -> rec.id == recipe.id);
	}

	public boolean isFavoriteRecipe(Recipe recipe) {
		return favorite_recipes.stream().anyMatch(favRecipe -> favRecipe.id == recipe.id);
	}
}
