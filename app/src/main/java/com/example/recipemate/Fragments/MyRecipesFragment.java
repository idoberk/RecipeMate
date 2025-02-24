package com.example.recipemate.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipemate.Adapters.RandomRecipeAdapter;
import com.example.recipemate.Listeners.RecipeClickListener;
import com.example.recipemate.Listeners.UserRecipeCallback;
import com.example.recipemate.Listeners.UserRecipesCallback;
import com.example.recipemate.Managers.UserRecipeManager;
import com.example.recipemate.Modals.Recipe;
import com.example.recipemate.Modals.UserRecipe;
import com.example.recipemate.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyRecipesFragment extends Fragment {

	private UserRecipeManager recipeManager;
	private Uri selectedImageUri;

	private RecyclerView recipesRecyclerView;
	private FloatingActionButton addRecipeButton;
	private View emptyStateView;
	private TextView emptyStateText;

	private View addRecipeFormContainer;
	private ImageView recipeImageView;
	private EditText recipeTitleEditText, recipeInstructionsEditText, recipeServingsEditText, recipeCookingTimeEditText;
	private Button submitRecipeButton;

	public MyRecipesFragment() {
		// Required empty public constructor
	}

	private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(), result -> {
				if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
					selectedImageUri = result.getData().getData();
					Picasso.get()
							.load(selectedImageUri)
							.into(recipeImageView);
				}
			}
	);


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_my_recipes, container, false);
		initManagers();
		initViews(view);
		setupListeners();
		loadUserRecipes();

		return view;
	}

	private void initManagers() {
		recipeManager = new UserRecipeManager();
	}

	private void initViews(View view) {
		recipesRecyclerView = view.findViewById(R.id.recipesRecyclerView);
		addRecipeButton = view.findViewById(R.id.addRecipeButton);
		emptyStateView = view.findViewById(R.id.emptyStateView);
		emptyStateText = view.findViewById(R.id.emptyStateText);

		addRecipeFormContainer = view.findViewById(R.id.addRecipeFormContainer);
		recipeImageView = view.findViewById(R.id.recipeImageView);
		recipeTitleEditText = view.findViewById(R.id.recipeTitleEditText);
		recipeInstructionsEditText = view.findViewById(R.id.recipeInstructionsEditText);
		recipeServingsEditText = view.findViewById(R.id.servingEditText);
		recipeCookingTimeEditText = view.findViewById(R.id.cookingTimeEditText);
		submitRecipeButton = view.findViewById(R.id.uploadRecipeButton);

		recipesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

		addRecipeFormContainer.setVisibility(View.GONE);
	}

	private void setupListeners() {
		addRecipeButton.setOnClickListener(v -> showAddRecipeForm());
		submitRecipeButton.setOnClickListener(v -> submitRecipe());
		recipeImageView.setOnClickListener(v -> openImagePicker());
	}

	private void openImagePicker() {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		imagePickerLauncher.launch(intent);
	}

	private void showAddRecipeForm() {
		addRecipeFormContainer.setVisibility(View.VISIBLE);
		recipesRecyclerView.setVisibility(View.GONE);
		emptyStateView.setVisibility(View.GONE);
		emptyStateText.setVisibility(View.GONE);
	}

	private void hideAddRecipeForm() {
		addRecipeFormContainer.setVisibility(View.GONE);
		loadUserRecipes();
		clearForm();
	}

	private void clearForm() {
		recipeImageView.setImageResource(R.drawable.ic_image_placeholder);
		recipeTitleEditText.setText("");
		recipeInstructionsEditText.setText("");
		recipeServingsEditText.setText("");
		recipeCookingTimeEditText.setText("");
		selectedImageUri = null;
	}

	private void showEmptyState() {
		emptyStateView.setVisibility(View.VISIBLE);
		emptyStateText.setVisibility(View.VISIBLE);
		recipesRecyclerView.setVisibility(View.GONE);
	}

	private void showRecipeList() {
		recipesRecyclerView.setVisibility(View.VISIBLE);
		emptyStateView.setVisibility(View.GONE);
		emptyStateText.setVisibility(View.GONE);
	}

	private void submitRecipe() {
		if (!validateForm()) {
			return;
		}

		String recipeTitle = recipeTitleEditText.getText().toString();
		String recipeInstruction = recipeInstructionsEditText.getText().toString();
		int recipeServings = Integer.parseInt(recipeServingsEditText.getText().toString());
		int recipeCookingTime = Integer.parseInt(recipeCookingTimeEditText.getText().toString());

//		UserRecipe recipe = new UserRecipe(
//				null,
//				recipeTitle,
//				recipeInstruction,
//				null,
//				recipeServings,
//				recipeCookingTime
//		);

		Recipe recipe = new Recipe();
		recipe.title = recipeTitle;
		recipe.instructions = recipeInstruction;
		recipe.servings = recipeServings;
		recipe.readyInMinutes = recipeCookingTime;

		recipeManager.addRecipe(recipe, selectedImageUri, new UserRecipeCallback() {
			@Override
			public void onSuccess(Recipe recipe) {
				Toast.makeText(getContext(), "Recipe added successfully", Toast.LENGTH_LONG).show();
				hideAddRecipeForm();
				loadUserRecipes();
			}

			@Override
			public void onError(String error) {
				Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
			}
		});

	}

	private boolean validateForm() {
		if (selectedImageUri == null) {
			Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_LONG).show();
			return false;
		}

		if (recipeTitleEditText.getText().toString().isEmpty()) {
			recipeTitleEditText.setError("Title is required");
			return false;
		}

		if (recipeInstructionsEditText.getText().toString().isEmpty()) {
			recipeInstructionsEditText.setError("Instructions are required");
			return false;
		}

		try {
			int servings = Integer.parseInt(recipeServingsEditText.getText().toString());
			if (servings <= 0) {
				recipeServingsEditText.setError("Servings must be greater than 0");
				return false;
			}
		} catch (NumberFormatException e) {
			recipeServingsEditText.setError("Invalid servings");
			return false;
		}

		try {
			int cookingTime = Integer.parseInt(recipeCookingTimeEditText.getText().toString());
			if (cookingTime <= 0) {
				recipeCookingTimeEditText.setError("Cooking time must be greater than 0");
				return false;
			}
		} catch (NumberFormatException e) {
			recipeCookingTimeEditText.setError("Invalid cooking time");
			return false;
		}

		return true;
	}

	private void loadUserRecipes() {
		recipeManager.getUserRecipes(new UserRecipesCallback() {
			@Override
			public void onSuccess(List<Recipe> recipes) {
				if (recipes.isEmpty()) {
					showEmptyState();
				} else {
					showRecipeList();
					recipesRecyclerView.setAdapter(new RandomRecipeAdapter(getContext(), recipes, recipeClickListener, 2));
				}
			}

			@Override
			public void onError(String error) {
				Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
			}
		});
	}

	private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
		@Override
		public void onRecipeClick(String recipeId) {
			Bundle bundle = new Bundle();
			bundle.putInt("recipeId", Integer.parseInt(recipeId));
			Navigation.findNavController(requireView()).navigate(R.id.action_myRecipesFragment_to_recipeDetailsFragment, bundle);
			Log.d("FavoritesFragment RecipeClickListener", "Recipe clicked: " + recipeId);
			Toast.makeText(getContext(), "Recipe clicked: " + recipeId, Toast.LENGTH_LONG).show();
		}
	};
}