package com.example.recipemate.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipemate.Adapters.IngredientsAdapter;
import com.example.recipemate.Listeners.RecipeDetailsListener;
import com.example.recipemate.Listeners.UserRecipeDetailsCallback;
import com.example.recipemate.Managers.FavoriteManager;
import com.example.recipemate.Managers.UserRecipeManager;
import com.example.recipemate.Modals.Recipe;
import com.example.recipemate.Modals.RecipeDetailsResponse;
import com.example.recipemate.R;
import com.example.recipemate.api.RequestManager;
import com.squareup.picasso.Picasso;


public class RecipeDetailsFragment extends Fragment {
	private static final String ARG_RECIPE_ID = "recipeId";

	private boolean isFavorite = false;
	private ImageView imageView_DishImage;
    private TextView textView_DishDetailsTitle, textViewIngredients, textViewInstructionsText;
    private RecyclerView recyclerViewIngredientsRecView;
	private ImageButton favoriteButton;
	private RequestManager requestManager;
	private FavoriteManager favoriteManager;
	private UserRecipeManager recipeManager;
	private IngredientsAdapter ingredientAdapter;
	private Recipe recipe;

	public RecipeDetailsFragment() {
		// Required empty public constructor
	}

	public static RecipeDetailsFragment newInstance(int id) {
		RecipeDetailsFragment fragment = new RecipeDetailsFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_RECIPE_ID, id);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        initViews(view);
		loadRecipeDetails(getArguments().getInt(ARG_RECIPE_ID));

		return view;
	}

	private void initViews(View view) {
		imageView_DishImage = view.findViewById(R.id.DishImage);
		favoriteButton = view.findViewById(R.id.RecipeDetailsFavoriteButton);
		textView_DishDetailsTitle = view.findViewById(R.id.DishDetailsTitle);
		textViewIngredients = view.findViewById(R.id.IngredientsTitle);
		recyclerViewIngredientsRecView = view.findViewById(R.id.IngredientsRecView);
		textViewInstructionsText = view.findViewById(R.id.InstructionsText);
		requestManager = new RequestManager(requireContext());
		favoriteManager = new FavoriteManager();
		recipeManager = new UserRecipeManager();

//		favoriteButton.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				toggleFavorite();
//			}
//		});
	}


	private void loadRecipeDetails(int recipeId) {
		Log.d("RecipeDetailsFragment", "loadRecipeDetails: " + recipeId);

		if (recipeId < 8000000) {
			requestManager.getRecipeDetails(new RecipeDetailsListener() {
				@Override
				public void didFetch(RecipeDetailsResponse response, String message) {
					loadApiRecipeDetails(response);
				}

				@Override
				public void didError(String message) {
					Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
				}
			}, recipeId);
		} else {
            loadUserRecipeDetails(recipeId);
		}
	}

	private void loadApiRecipeDetails(RecipeDetailsResponse response) {
		textView_DishDetailsTitle.setText(response.title);
		if (response.instructions.isEmpty()) {
			textViewInstructionsText.setText("No instructions available");
		} else {
			textViewInstructionsText.setText(cleanHTMLTags(response.instructions));
		}
		Picasso.get().load(response.image).into(imageView_DishImage);
		recyclerViewIngredientsRecView.setHasFixedSize(true);
		recyclerViewIngredientsRecView.setLayoutManager(new LinearLayoutManager(requireContext()));
		ingredientAdapter = new IngredientsAdapter(requireContext(), response.extendedIngredients);
		recyclerViewIngredientsRecView.setAdapter(ingredientAdapter);

		recipe = new Recipe();
		recipe.id = response.id;
		recipe.title = response.title;
		recipe.image = response.image;

		// checkFavoriteStatus();
		setupFavoriteButton();
	}

	private void loadUserRecipeDetails(int recipeId) {
		recipeManager.getRecipeDetails(new UserRecipeDetailsCallback() {
			@Override
			public void didFetch(Recipe userRecipe, String message) {
				textView_DishDetailsTitle.setText(userRecipe.title);
				if (userRecipe.instructions.isEmpty()) {
					textViewInstructionsText.setText("No instructions available");
				} else {
					textViewInstructionsText.setText(cleanHTMLTags(userRecipe.instructions));
				}
				Picasso.get().load(userRecipe.image).into(imageView_DishImage);
				// recyclerViewIngredientsRecView.setHasFixedSize(true);
				// recyclerViewIngredientsRecView.setLayoutManager(new LinearLayoutManager(requireContext()));
				// ingredientAdapter = new IngredientsAdapter(requireContext(), recipe.extendedIngredients);
				// recyclerViewIngredientsRecView.setAdapter(ingredientAdapter);

				recipe = new Recipe();
				recipe.id = userRecipe.id;
				recipe.title = userRecipe.title;
				recipe.image = userRecipe.image;

				// checkFavoriteStatus();
				setupFavoriteButton();
			}

			@Override
			public void didError(String message) {
				Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
			}
		}, recipeId);
	}

	private void checkFavoriteStatus() {
		favoriteManager.isRecipeFavorite(recipe, new FavoriteManager.FavoriteCheckCallback() {
			@Override
			public void onSuccess(boolean isFavorite) {
				favoriteButton.setSelected(isFavorite);
				favoriteButton.setImageResource(
						isFavorite ? R.drawable.ic_favorite_selected : R.drawable.ic_favorites
				);
			}

			@Override
			public void onError(String error) {
				Log.d("Favorite", "Error: " + error);
				Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void setupFavoriteButton() {
		checkFavoriteStatus();
		favoriteButton.setOnClickListener(view -> {
			favoriteManager.toggleFavorite(recipe, new FavoriteManager.FavoriteCallback() {
				@Override
				public void onSuccess() {
					boolean buttonState = !favoriteButton.isSelected();

					favoriteButton.setSelected(buttonState);
					favoriteButton.setImageResource(
							buttonState ? R.drawable.ic_favorite_selected : R.drawable.ic_favorites
					);

				}

				@Override
				public void onError(String error) {
					Log.d("Favorite", "Error: " + error);
					Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
				}
			});
		});
	}

	private String cleanHTMLTags(String html) {
		String formattedString = html.replaceAll("<[^>]*>", " ");
		String withoutHtml = Html.fromHtml(formattedString, Html.FROM_HTML_MODE_COMPACT).toString();

		return withoutHtml.replaceAll("\\s+\n", "\n")
				.replaceAll("\n\\s+", "\n")
				.replaceAll("\\s+", " ")
				.replaceAll("\n\n+", "\n\n")
				.trim();
	}
}