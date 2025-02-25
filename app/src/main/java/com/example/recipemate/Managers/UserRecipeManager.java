package com.example.recipemate.Managers;

import android.net.Uri;

import com.example.recipemate.Listeners.DeleteRecipeCallback;
import com.example.recipemate.Listeners.NextIdCallback;
import com.example.recipemate.Listeners.UserRecipeCallback;
import com.example.recipemate.Listeners.UserRecipeDetailsCallback;
import com.example.recipemate.Listeners.UserRecipesCallback;
import com.example.recipemate.Modals.ExtendedIngredient;
import com.example.recipemate.Modals.Recipe;
import com.example.recipemate.Modals.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		if (recipe.extendedIngredients == null) {
			recipe.extendedIngredients = new ArrayList<>();
		}

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
					updateRecipe(recipe, callback);
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

	public void deleteRecipe(int recipeId, DeleteRecipeCallback callback) {
		db.collection(USER_RECIPES_COLLECTION)
				.document(String.valueOf(recipeId))
				.get()
				.addOnSuccessListener(documentSnapshot -> {
					if (documentSnapshot.exists()) {
						Recipe recipe = documentSnapshot.toObject(Recipe.class);

						removeFromAllUsersFavorites(recipeId, new DeleteRecipeCallback() {
							@Override
							public void onSuccess() {
								db.collection(USER_RECIPES_COLLECTION)
										.document(String.valueOf(recipeId))
										.delete()
										.addOnSuccessListener(aVoid -> {
											if (recipe != null && recipe.image != null && recipe.image.contains("firebase")) {
												String imagePath = recipe.image.substring(recipe.image.indexOf("o/") + 2,
														recipe.image.indexOf("?"));
												imagePath = imagePath.replace("%2F", "/");

												storage.getReference().child(imagePath).delete()
														.addOnSuccessListener(unused -> callback.onSuccess())
														.addOnFailureListener(e -> callback.onSuccess());
											} else {
												callback.onSuccess();
											}
										})
										.addOnFailureListener(e -> callback.onError("Failed to delete recipe"));
							}

							@Override
							public void onError(String error) {
								callback.onError(error);
							}
						});
					} else {
						callback.onError("Recipe not found");
					}
				})
				.addOnFailureListener(e -> callback.onError("Failed to delete recipe"));
	}

	private void removeFromAllUsersFavorites(int recipeId, DeleteRecipeCallback callback) {
		db.collection(USERS_COLLECTION)
				.get()
				.addOnSuccessListener(querySnapshot -> {
					if (querySnapshot.isEmpty()) {
						callback.onSuccess();
						return;
					}
					
					final int[] updateCount = {0};
					final int totalDocuments = querySnapshot.size();
					final boolean[] hasError = {false};
					
					for (DocumentSnapshot userDoc : querySnapshot.getDocuments()) {
						User user = userDoc.toObject(User.class);
						if (user == null) {
							updateCount[0]++;
							continue;
						}
						
						boolean hadRecipe = false;
						if (user.getFavoriteRecipes() != null) {
							ArrayList<Recipe> updatedFavorites = new ArrayList<>(user.getFavoriteRecipes());
							
							for (int i = 0; i < updatedFavorites.size(); i++) {
								if (updatedFavorites.get(i).id == recipeId) {
									updatedFavorites.remove(i);
									hadRecipe = true;
									break;
								}
							}
							
							if (hadRecipe) {
								user.setFavoriteRecipes(updatedFavorites);
								db.collection(USERS_COLLECTION)
										.document(user.getUserid())
										.set(user)
										.addOnSuccessListener(aVoid -> {
											updateCount[0]++;
											if (updateCount[0] >= totalDocuments && !hasError[0]) {
												callback.onSuccess();
											}
										})
										.addOnFailureListener(e -> {
											hasError[0] = true;
											callback.onError("Failed to update user favorites");
										});
							} else {
								updateCount[0]++;
							}
						} else {
							updateCount[0]++;
						}

						if (updateCount[0] >= totalDocuments && !hasError[0]) {
							callback.onSuccess();
						}
					}
				})
				.addOnFailureListener(e -> callback.onError("Failed to remove recipe from favorites"));
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
					recipe.extendedIngredients = new ArrayList<>();

					if (documentSnapshot.contains("servings")) {
						recipe.servings = documentSnapshot.getLong("servings").intValue();
					}

	                if (documentSnapshot.contains("readyInMinutes")) {
		                recipe.cookingMinutes = documentSnapshot.getLong("readyInMinutes").intValue();
	                }

					List<Map<String, Object>> ingredientsData = (List<Map<String, Object>>) documentSnapshot.get("extendedIngredients");
					if (ingredientsData != null && !ingredientsData.isEmpty()) {
						for (Map<String, Object> ingredientData : ingredientsData) {
							ExtendedIngredient ingredient = new ExtendedIngredient();

							if (ingredientData.containsKey("name")) {
								ingredient.name = (String) ingredientData.get("name");
							}

							if (ingredientData.containsKey("original")) {
								ingredient.original = (String) ingredientData.get("original");
							}

							if (ingredientData.containsKey("amount")) {
								Object amountObj = ingredientData.get("amount");
								if (amountObj instanceof Double) {
									ingredient.amount = (Double) amountObj;
								} else if (amountObj instanceof Long) {
									ingredient.amount = ((Long) amountObj).doubleValue();
								}
							}

							ingredient.unit = (String) ingredientData.get("unit");
							ingredient.aisle = (String) ingredientData.get("aisle");
							recipe.extendedIngredients.add(ingredient);
						}
					}

                    callback.didFetch(recipe, "Recipe found");
                } else {
                    callback.didError("Recipe not found");
                }
			 })
			 .addOnFailureListener(e -> callback.didError("Failed to retrieve recipe"));
        }
}
