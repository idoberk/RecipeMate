package com.example.recipemate.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipemate.Activities.LoadingDialog;
import com.example.recipemate.Adapters.RandomRecipeAdapter;
import com.example.recipemate.Adapters.UserIngredientAdapter;
import com.example.recipemate.Listeners.DeleteRecipeCallback;
import com.example.recipemate.Listeners.IngredientActionListener;
import com.example.recipemate.Listeners.RecipeClickListener;
import com.example.recipemate.Listeners.UserRecipeCallback;
import com.example.recipemate.Listeners.UserRecipesCallback;
import com.example.recipemate.Managers.UserRecipeManager;
import com.example.recipemate.Modals.ExtendedIngredient;
import com.example.recipemate.Modals.Recipe;
import com.example.recipemate.Modals.UserIngredient;
import com.example.recipemate.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyRecipesFragment extends Fragment {

	private UserRecipeManager recipeManager;
	private Uri selectedImageUri;
	private LoadingDialog loadingDialog;

	private RecyclerView recipesRecyclerView;
	private FloatingActionButton addRecipeButton;
	private View emptyStateView;
	private TextView emptyStateText;

	private View addRecipeFormContainer;
	private ImageView recipeImageView;
	private EditText recipeTitleEditText, recipeInstructionsEditText, recipeServingsEditText, recipeCookingTimeEditText;
	private Button submitRecipeButton;

	private Button addIngredientButton;
	private RecyclerView ingredientsRecyclerView;
	private UserIngredientAdapter ingredientAdapter;
	private List<UserIngredient> userIngredientList = new ArrayList<>();
	private int editingIngredientPosition = -1;
    private ItemTouchHelper itemTouchHelper;

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

		loadingDialog = new LoadingDialog(getActivity());

		loadingDialog.startAlertDialog();
		initManagers();
		initViews(view);
		setupListeners();
		setupIngredientsRecyclerView();
		swipeToDelete();
		loadUserRecipes();

		loadingDialog.dismissDialog();

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

		addIngredientButton = view.findViewById(R.id.addIngredientButton);
		ingredientsRecyclerView = view.findViewById(R.id.ingredientsRecyclerView);

		recipesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

		addRecipeFormContainer.setVisibility(View.GONE);
	}

	private void setupListeners() {
		addRecipeButton.setOnClickListener(v -> showAddRecipeForm());
		submitRecipeButton.setOnClickListener(v -> submitRecipe());
		recipeImageView.setOnClickListener(v -> openImagePicker());
		addIngredientButton.setOnClickListener(v -> showAddIngredientDialog(null, -1));
	}

	private void setupIngredientsRecyclerView() {
		ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		IngredientActionListener ingredientListener = new IngredientActionListener() {
			@Override
			public void onEditIngredient(int position, UserIngredient ingredient) {
				showAddIngredientDialog(ingredient, position);
			}

			@Override
			public void onIngredientSelectionChanged(int position, boolean isSelected) {

			}
		};

		ingredientAdapter = new UserIngredientAdapter(getContext(), userIngredientList, ingredientListener);
		ingredientsRecyclerView.setAdapter(ingredientAdapter);

		updateIngredientListVisibility();
	}

	private void openImagePicker() {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		imagePickerLauncher.launch(intent);
	}

	private void showAddRecipeForm() {
		addRecipeFormContainer.setVisibility(View.VISIBLE);
		addRecipeButton.setVisibility(View.GONE);
		recipesRecyclerView.setVisibility(View.GONE);
		emptyStateView.setVisibility(View.GONE);
		emptyStateText.setVisibility(View.GONE);
		userIngredientList.clear();
		updateIngredientListVisibility();
	}

	private void hideAddRecipeForm() {
		addRecipeFormContainer.setVisibility(View.GONE);
		addRecipeButton.setVisibility(View.VISIBLE);
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
		userIngredientList.clear();
		updateIngredientListVisibility();
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

		loadingDialog.startAlertDialog();

		String recipeTitle = recipeTitleEditText.getText().toString();
		String recipeInstruction = recipeInstructionsEditText.getText().toString();
		int recipeServings = Integer.parseInt(recipeServingsEditText.getText().toString());
		int recipeCookingTime = Integer.parseInt(recipeCookingTimeEditText.getText().toString());

		Recipe recipe = new Recipe();
		recipe.title = recipeTitle;
		recipe.instructions = recipeInstruction;
		recipe.servings = recipeServings;
		recipe.readyInMinutes = recipeCookingTime;

		ArrayList<ExtendedIngredient> extendedIngredients = new ArrayList<>();
		for (UserIngredient userIngredient : userIngredientList) {
			if (userIngredient.isSelected()) {
				ExtendedIngredient ingredient = new ExtendedIngredient();
				ingredient.name = userIngredient.getName();
				ingredient.original = userIngredient.getOriginal();
				extendedIngredients.add(ingredient);
			}
		}

		recipe.extendedIngredients = extendedIngredients;

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

		boolean hasSelectedIngredient = false;
		for (UserIngredient ingredient : userIngredientList) {
			if (ingredient.isSelected()) {
				hasSelectedIngredient = true;
				break;
			}
		}

		if (!hasSelectedIngredient) {
			Toast.makeText(getContext(), "Please select at least one ingredient", Toast.LENGTH_LONG).show();
			return false;
		}

		return true;
	}

	private void showAddIngredientDialog(UserIngredient ingredientToEdit, int position) {
		editingIngredientPosition = position;

		AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
		LayoutInflater inflater = requireActivity().getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.add_ingredient_dialog, null);

		EditText nameEditText = dialogView.findViewById(R.id.ingredientNameEditText);
		EditText quantityEditText = dialogView.findViewById(R.id.ingredientQuantityEditText);
		Button cancelButton = dialogView.findViewById(R.id.cancelButton);
		Button addButton = dialogView.findViewById(R.id.addButton);

		TextView titleText = dialogView.findViewById(R.id.dialogTitle);

		if (ingredientToEdit != null) {
			titleText.setText("Edit Ingredient");
			addButton.setText("Update");
			nameEditText.setText(ingredientToEdit.getName());
			quantityEditText.setText(ingredientToEdit.getOriginal());
		} else {
			titleText.setText("Add Ingredient");
			addButton.setText("Add");
		}

		builder.setView(dialogView);
		AlertDialog dialog = builder.create();

		cancelButton.setOnClickListener(v -> dialog.dismiss());

		addButton.setOnClickListener(v -> {
			String name = nameEditText.getText().toString().trim();
			String quantity = quantityEditText.getText().toString().trim();

			if (name.isEmpty()) {
				nameEditText.setError("Name is required");
				return;
			}

			if (quantity.isEmpty()) {
				quantityEditText.setError("Quantity is required");
				return;
			}

			String original = quantity + " " + name;

			if (position == -1) {
				UserIngredient newIngredient = new UserIngredient(name, original);
				newIngredient.setSelected(true);
				userIngredientList.add(newIngredient);
			} else {
				UserIngredient ingredient = userIngredientList.get(position);
				ingredient.setName(name);
				ingredient.setOriginal(original);
			}

			ingredientAdapter.notifyDataSetChanged();
			updateIngredientListVisibility();
			dialog.dismiss();
		});

		dialog.show();
	}

	private void updateIngredientListVisibility() {
		if (userIngredientList.isEmpty()) {
			ingredientsRecyclerView.setVisibility(View.GONE);
		} else {
			ingredientsRecyclerView.setVisibility(View.VISIBLE);
			ingredientAdapter.updateIngredients(userIngredientList);
		}
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

	private void swipeToDelete() {
		itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
			@Override
			public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
				int position = viewHolder.getAdapterPosition();

				RandomRecipeAdapter adapter = (RandomRecipeAdapter) recipesRecyclerView.getAdapter();
				if (adapter != null) {
					Recipe recipe = adapter.getRecipeAt(position);

					new AlertDialog.Builder(requireContext())
							.setTitle("Delete Recipe")
							.setMessage("Are you sure you want to delete this recipe?")
							.setPositiveButton("Delete", (dialog, which) -> {
								recipeManager.deleteRecipe(recipe.id, new DeleteRecipeCallback() {
									@Override
									public void onSuccess() {
										loadUserRecipes();
									}

									@Override
									public void onError(String error) {
										adapter.notifyItemChanged(position);
									}
								});
							})
							.setNegativeButton("Cancel", (dialog, which) -> {
								adapter.notifyItemChanged(position);
							})
							.setOnCancelListener(dialog -> {
								adapter.notifyItemChanged(position);
							})
							.create()
							.show();
				}


			}
		});

		itemTouchHelper.attachToRecyclerView(recipesRecyclerView);
	}

	private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
		@Override
		public void onRecipeClick(String recipeId) {
			Bundle bundle = new Bundle();
			bundle.putInt("recipeId", Integer.parseInt(recipeId));
			Navigation.findNavController(requireView()).navigate(R.id.action_myRecipesFragment_to_recipeDetailsFragment, bundle);
		}
	};
}