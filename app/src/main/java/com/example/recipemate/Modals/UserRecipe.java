package com.example.recipemate.Modals;

import java.util.Date;

public class UserRecipe {
	private String id;
	private String userId;
	private String title;
	private String description;
	private String imageUrl;
	private int servings;
	private int cookingTime;
	private Date createdAt;

	public UserRecipe() {
	}

	public UserRecipe(String userId, String title, String description,
	                  String imageUrl, int servings, int cookingTime) {
		this.userId = userId;
		this.title = title;
		this.description = description;
		this.imageUrl = imageUrl;
		this.servings = servings;
		this.cookingTime = cookingTime;
		this.createdAt = new Date();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getServings() {
		return servings;
	}

	public void setServings(int servings) {
		this.servings = servings;
	}

	public int getCookingTime() {
		return cookingTime;
	}

	public void setCookingTime(int cookingTime) {
		this.cookingTime = cookingTime;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
