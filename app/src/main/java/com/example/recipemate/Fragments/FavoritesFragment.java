package com.example.recipemate.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipemate.Adapters.RandomRecipeAdapter;
import com.example.recipemate.Listeners.RecipeClickListener;
import com.example.recipemate.Managers.FavoriteManager;
import com.example.recipemate.Modals.Recipe;
import com.example.recipemate.R;

import java.util.List;

public class FavoritesFragment extends Fragment {

	private TextView titleTextView, resultTextView;
	private RecyclerView favoritesRecyclerView;
	private FavoriteManager favoriteManager;
	private RandomRecipeAdapter adapter;

	public FavoritesFragment() {
			// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

		initViews(view);
		loadFavorites();
		return view;
	}

	private void loadFavorites() {
		Log.d("FavoritesFragment", "Loading favorites");
		favoriteManager.getFavorites(new FavoriteManager.FavoritesCallback() {
			@Override
			public void onSuccess(List<Recipe> recipes) {
				adapter = new RandomRecipeAdapter(requireContext(), recipes, recipeClickListener, 2);
				favoritesRecyclerView.setAdapter(adapter);

				if (recipes.isEmpty()) {
					Log.d("FavoritesFragment", "No favorites found");
					Toast.makeText(getContext(), "No favorites found", Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onError(String error) {
				Log.d("FavoritesFragment", "Error loading favorites: " + error);
				Toast.makeText(getContext(), "Error loading favorites: " + error, Toast.LENGTH_LONG).show();
			}
		});
	}

	private void initViews(View view) {
		resultTextView = view.findViewById(R.id.ResultsText);
		resultTextView.setVisibility(View.GONE);
		titleTextView = view.findViewById(R.id.CategoryText);
		titleTextView.setText("Favorites");
		favoritesRecyclerView = view.findViewById(R.id.RecipeListRV);
		favoritesRecyclerView.setHasFixedSize(true);
		favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		favoriteManager = new FavoriteManager();
	}

	private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
		@Override
		public void onRecipeClick(String recipeId) {
			Bundle bundle = new Bundle();
			bundle.putInt("recipeId", Integer.parseInt(recipeId));
			Navigation.findNavController(requireView()).navigate(R.id.action_favoritesFragment_to_recipeDetailsFragment, bundle);
			Log.d("FavoritesFragment RecipeClickListener", "Recipe clicked: " + recipeId);
			Toast.makeText(getContext(), "Recipe clicked: " + recipeId, Toast.LENGTH_LONG).show();
		}
	};
}