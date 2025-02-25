package com.example.recipemate.Modals;

import com.example.recipemate.R;

import java.util.Arrays;
import java.util.List;

public class CategoriesRecyclerViewGrid {
	private List<RecipesCategories> categoriesList;

	public CategoriesRecyclerViewGrid() {
		 categoriesList = Arrays.asList(
				 new RecipesCategories("Pasta", R.drawable.pasta),
				 new RecipesCategories("Fish", R.drawable.fish),
				 new RecipesCategories("Bread", R.drawable.bread),
				 new RecipesCategories("Salad", R.drawable.salad),
				 new RecipesCategories("Chicken", R.drawable.chicken),
				 new RecipesCategories("Beef", R.drawable.beef),
				 new RecipesCategories("Vegetarian", R.drawable.vegetarian),
				 new RecipesCategories("Dessert", R.drawable.dessert)
		 );
	}

	public List<RecipesCategories> getCategoriesList() {
		return categoriesList;
	}

	public int getColumnCount() {
		return 2;
	}
}