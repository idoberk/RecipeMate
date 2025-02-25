package com.example.recipemate.Modals;

public class UserIngredient {
	private String name;
	private String original;
	private boolean isSelected;

	public UserIngredient() {
		this.isSelected = true;
	}

	public UserIngredient(String name, String original) {
		this.name = name;
		this.original = original;
		this.isSelected = true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		this.isSelected = selected;
	}
}
