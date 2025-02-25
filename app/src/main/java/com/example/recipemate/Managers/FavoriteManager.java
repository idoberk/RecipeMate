package com.example.recipemate.Managers;

import com.example.recipemate.Modals.Recipe;
import com.example.recipemate.Modals.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FavoriteManager {

	private FirebaseAuth mAuth;
	private FirebaseFirestore db;

	public FavoriteManager() {
		mAuth = FirebaseAuth.getInstance();
		db = FirebaseFirestore.getInstance();
	}

	public interface FavoriteCallback {
		void onSuccess();
		void onError(String error);
	}

	public interface FavoritesCallback {
		void onSuccess(List<Recipe> recipes);
		void onError(String error);
	}

	public void toggleFavorite(Recipe recipe, FavoriteCallback callback) {
		FirebaseUser currentUser = mAuth.getCurrentUser();

		if (currentUser == null) {
			return;
		}

		String userId = currentUser.getUid();
		DocumentReference userRef = db.collection("users").document(userId);

		userRef.get().addOnSuccessListener(document -> {
			if (document.exists()) {
				User user = document.toObject(User.class);
				if (user != null) {
					if (user.isFavoriteRecipe(recipe)) {
						user.removeFavoriteRecipe(recipe);
					} else {
						user.addFavoriteRecipe(recipe);
					}

					userRef.set(user)
							.addOnSuccessListener(v -> callback.onSuccess())
							.addOnFailureListener(e -> callback.onError(e.getMessage()));
				}
			}
		}).addOnFailureListener(e-> callback.onError(e.getMessage()));
	}

	public void getFavorites(FavoritesCallback callback) {
		FirebaseUser currentUser = mAuth.getCurrentUser();

		if (currentUser == null) {
			callback.onError("User not logged in");
			return;
		}

		String userId = currentUser.getUid();
		DocumentReference userRef = db.collection("users").document(userId);

		userRef.get().addOnSuccessListener(document -> {
			if (document.exists()) {
				User user = document.toObject(User.class);
				if (user != null) {
					callback.onSuccess(user.getFavoriteRecipes());
				}
			}
		}).addOnFailureListener(e-> callback.onError(e.getMessage()));
	}

	public void isRecipeFavorite(Recipe recipe, FavoriteCheckCallback callback) {
		FirebaseUser currentUser = mAuth.getCurrentUser();

		if (currentUser == null) {
			callback.onError("User not logged in");
			return;
		}

		String userId = currentUser.getUid();
		DocumentReference userRef = db.collection("users").document(userId);

		userRef.get().addOnSuccessListener(document -> {
			if (document.exists()) {
				User user = document.toObject(User.class);
				if (user != null) {
					callback.onSuccess(user.isFavoriteRecipe(recipe));
				}
			}
		}).addOnFailureListener(e-> callback.onError(e.getMessage()));
	}

	public interface FavoriteCheckCallback {
		void onSuccess(boolean isFavorite);
		void onError(String error);
	}
}
