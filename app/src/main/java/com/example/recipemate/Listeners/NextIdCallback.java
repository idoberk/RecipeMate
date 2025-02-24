package com.example.recipemate.Listeners;

public interface NextIdCallback {
	void onIdGenerated(long id);
	void onError(String error);
}
