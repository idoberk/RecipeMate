package com.example.recipemate.Models;

import java.util.ArrayList;

public class User {
	private String user_id;
	private String user_email;
	private ArrayList<String> user_recipes;

	public User() {
	}

	public User(String id, String email, ArrayList<String> recipes) {
		this.user_id = id;
		this.user_email = email;
		this.user_recipes = recipes;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String id) {
		this.user_id = id;
	}

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String email) {
		this.user_email = email;
	}

	public ArrayList<String> getUser_recipes() {
		return user_recipes;
	}

	public void setUser_recipes(ArrayList<String> recipes) {
		this.user_recipes = recipes;
	}
}
