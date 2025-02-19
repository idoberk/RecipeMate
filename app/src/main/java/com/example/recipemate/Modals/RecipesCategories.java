package com.example.recipemate.Modals;

public class RecipesCategories {
	private String categoryName;
	private Integer categoryImage;

	public RecipesCategories(String categoryName, Integer categoryImage) {
		this.categoryName = categoryName;
		this.categoryImage = categoryImage;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public Integer getCategoryImage() {
		return categoryImage;
	}
}
