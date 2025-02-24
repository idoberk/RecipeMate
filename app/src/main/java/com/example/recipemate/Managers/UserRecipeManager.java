package com.example.recipemate.Managers;

import android.net.Uri;

import com.example.recipemate.Listeners.NextIdCallback;
import com.example.recipemate.Listeners.UserRecipeCallback;
import com.example.recipemate.Listeners.UserRecipeDetailsCallback;
import com.example.recipemate.Listeners.UserRecipesCallback;
import com.example.recipemate.Modals.Recipe;
import com.example.recipemate.Modals.UserRecipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class UserRecipeManager {
	private FirebaseAuth mAuth;
	private FirebaseFirestore db;
	private FirebaseStorage storage;
	private static final String USER_RECIPES_COLLECTION = "user_recipes";
	private static final String USERS_COLLECTION = "users";
	private static final long USER_RECIPE_ID_START = 8000000L;
	private static final String STORAGE_PATH = "recipe_images";

	public UserRecipeManager() {
		mAuth = FirebaseAuth.getInstance();
		db = FirebaseFirestore.getInstance();
		storage = FirebaseStorage.getInstance();
	}

	private void getNextRecipeId(NextIdCallback callback) {
		DocumentReference counterRef = db.collection("counters").document("recipe_counter");

		db.runTransaction(transaction -> {
			Long currentId = transaction.get(counterRef).getLong("current_id");
			long nextId = (currentId == null) ? USER_RECIPE_ID_START : currentId + 1;

			transaction.set(counterRef, java.util.Collections.singletonMap("current_id", nextId));
			return nextId;
		}).addOnSuccessListener(callback::onIdGenerated)
				.addOnFailureListener(e -> callback.onError("Failed to generate recipe id"));
	}

	public void addRecipe(Recipe recipe, Uri imageUri, UserRecipeCallback callback) {
		FirebaseUser currentUser = mAuth.getCurrentUser();

		if (currentUser == null) {
			callback.onError("User not logged in");
			return;
		}

		recipe.userId = currentUser.getUid();
		getNextRecipeId(new NextIdCallback() {
			@Override
			public void onIdGenerated(long id) {
				recipe.id = (int) id;
				saveRecipe(recipe, imageUri, callback);
			}

			@Override
			public void onError(String error) {
				callback.onError(error);
			}
		});
	}

	public void getUserRecipes(UserRecipesCallback callback) {
		FirebaseUser currentUser = mAuth.getCurrentUser();
		if (currentUser == null) {
			callback.onError("User not logged in");
			return;
		}

		db.collection(USER_RECIPES_COLLECTION)
				.whereEqualTo("userId", currentUser.getUid())
				.get()
				.addOnSuccessListener(queryDocumentSnapshots -> {
					List<Recipe> recipes = queryDocumentSnapshots.toObjects(Recipe.class);
					callback.onSuccess(recipes);
				})
				.addOnFailureListener(e -> callback.onError("Failed to fetch recipes"));
	}

	
	
	private void saveRecipe(Recipe recipe, Uri imageUri, UserRecipeCallback callback) {
		db.collection(USER_RECIPES_COLLECTION)
				.document(String.valueOf(recipe.id))
				.set(recipe)
				.addOnSuccessListener(aVoid -> {
					if (imageUri != null) {
						uploadImage(recipe, imageUri, callback);
					} else {
						callback.onSuccess(recipe);
					}
				})
				.addOnFailureListener(e -> callback.onError("Failed to save recipe"));
	}

	private void uploadImage(Recipe recipe, Uri imageUri, UserRecipeCallback callback) {
		String imagePath = STORAGE_PATH + "/" + recipe.userId + "/" + System.currentTimeMillis() + ".jpg";
		StorageReference imageRef = storage.getReference().child(imagePath);

		imageRef.putFile(imageUri)
				.continueWithTask(task -> {
					if (!task.isSuccessful()) {
						throw task.getException();
					}
					return imageRef.getDownloadUrl();
				})
				.addOnSuccessListener(uri -> {
					recipe.image = uri.toString();
					// recipe.sourceUrl = uri.toString();
					updateRecipe(recipe, callback);
					// recipe.userId = currentUser.getUid();

//					db.collection(USER_RECIPES_COLLECTION)
//							.add(recipe).addOnSuccessListener(documentReference -> {
//								recipe.id = Integer.parseInt(documentReference.getId());
//								callback.onSuccess(recipe);
//							})
//							.addOnFailureListener(e -> callback.onError("Failed to add recipe"));
				})
				.addOnFailureListener(e -> callback.onError("Failed to upload image"));
	}

	private void updateRecipe(Recipe recipe, UserRecipeCallback callback) {
		db.collection(USER_RECIPES_COLLECTION)
				.document(String.valueOf(recipe.id))
				.set(recipe)
				.addOnSuccessListener(aVoid -> callback.onSuccess(recipe))
				.addOnFailureListener(e -> callback.onError("Failed to update recipe"));
	}
	
	public void getRecipeDetails(UserRecipeDetailsCallback callback, int recipeId) {
		 db.collection(USER_RECIPES_COLLECTION)
			 .document(String.valueOf(recipeId))
			 .get()
			 .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Recipe recipe = new Recipe();
					recipe.id = recipeId;
					recipe.userId = documentSnapshot.getString("userId");
					recipe.title = documentSnapshot.getString("title");
					recipe.instructions = documentSnapshot.getString("instructions");
					recipe.image = documentSnapshot.getString("image");

					if (documentSnapshot.contains("servings")) {
						recipe.servings = documentSnapshot.getLong("servings").intValue();
					}

	                if (documentSnapshot.contains("readyInMinutes")) {
		                recipe.cookingMinutes = documentSnapshot.getLong("readyInMinutes").intValue();
	                }

                    callback.didFetch(recipe, "Recipe found");

                } else {
                    callback.didError("Recipe not found");
                }
			 })
			 .addOnFailureListener(e -> callback.didError("Failed to retrieve recipe"));
        }
}
