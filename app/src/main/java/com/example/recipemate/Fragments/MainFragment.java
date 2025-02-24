package com.example.recipemate.Fragments;

import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.recipemate.Adapters.CategoriesGridAdapter;
import com.example.recipemate.Adapters.RandomRecipeAdapter;
import com.example.recipemate.Listeners.CategoryClickListener;
import com.example.recipemate.Listeners.RandomRecipeResponseListener;
import com.example.recipemate.Listeners.RecipeClickListener;
import com.example.recipemate.Listeners.SearchRecipeResponseListener;
import com.example.recipemate.Modals.CategoriesRecyclerViewGrid;
import com.example.recipemate.Modals.RandomRecipeApiResponse;
import com.example.recipemate.Modals.Recipe;
import com.example.recipemate.Modals.SearchRecipeApiResponse;
import com.example.recipemate.R;
import com.example.recipemate.api.RequestManager;
import com.google.android.material.navigation.NavigationView;

import java.util.List;


public class MainFragment extends Fragment implements CategoryClickListener {// , NavigationView.OnNavigationItemSelectedListener {

	public MainFragment() {
		// Required empty public constructor
	}

	private RecyclerView categoriesRecyclerView;
	private RecyclerView searchedRecipesRecyclerView;
	private CategoriesGridAdapter categoriesAdapter;
	private CategoriesRecyclerViewGrid categoriesGrid;
	private SearchView searchView;
	private RequestManager requestManager;
	private Handler searchHandler = new Handler(Looper.getMainLooper());
	private Runnable searchRunnable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_main, container, false);

		initViews(view);
		setCategoriesGrid();
		setSearchView();

		return view;
	}

	private void initViews(View view) {

		categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
		searchedRecipesRecyclerView = view.findViewById(R.id.recipesRecyclerView);
		searchView = view.findViewById(R.id.searchView);
		requestManager = new RequestManager(requireContext());

		searchedRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
	}

	private void setCategoriesGrid() {
		categoriesGrid = new CategoriesRecyclerViewGrid();
		GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), categoriesGrid.getColumnCount());

		categoriesRecyclerView.setLayoutManager(gridLayoutManager);

		categoriesAdapter = new CategoriesGridAdapter(requireContext(), categoriesGrid.getCategoriesList(), this);
		categoriesRecyclerView.setAdapter(categoriesAdapter);
	}

	@Override
	public void onCategoryClick(String categoryName) {
		Bundle bundle = new Bundle();

		bundle.putString("category", categoryName.toLowerCase());
		Navigation.findNavController(requireView()).navigate(R.id.action_mainFragment_to_recipeListFragment, bundle);
	}

	private void setSearchView() {
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				return false;
			}

			@Override
			public boolean onQueryTextChange(String s) {
				if (searchRunnable != null) {
					searchHandler.removeCallbacks(searchRunnable);
				}
				if (s.isEmpty()) {
					showCategoriesList();
				} else {
					searchRunnable = new Runnable() {
						@Override
						public void run() {
							searchRecipes(s.toLowerCase());
						}
					};

					searchHandler.postDelayed(searchRunnable, 500);
				}
				return true;
			}
		});
	}

	private void searchRecipes(String query) {
		requestManager.searchRecipes(query, new SearchRecipeResponseListener() {
			@Override
			public void didFetch(SearchRecipeApiResponse response, String message) {
				Log.d("Search", "Fetched recipes for query: " + query);
				showRecipesList(response.results);
			}

			@Override
			public void didError(String message) {
				Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
			}
		});
	}

	private void showCategoriesList() {
		categoriesRecyclerView.setVisibility(View.VISIBLE);
		searchedRecipesRecyclerView.setVisibility(View.GONE);
	}

	private void showRecipesList(List<Recipe> recipes) {
		if (recipes == null || recipes.isEmpty()) {
			Log.d("Search", "No recipes found for query");
			return;
		}

		categoriesRecyclerView.setVisibility(View.GONE);
		searchedRecipesRecyclerView.setVisibility(View.VISIBLE);
		searchedRecipesRecyclerView.setAdapter(new RandomRecipeAdapter(requireContext(), recipes, recipeClickListener, 1));
		Log.d("Search", "Showing recipes for query");
	}

	private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
		@Override
		public void onRecipeClick(String recipeId) {
			Bundle bundle = new Bundle();
			bundle.putInt("recipeId", Integer.parseInt(recipeId));
			Navigation.findNavController(requireView()).navigate(R.id.action_mainFragment_to_recipeDetailsFragment, bundle);
			Log.d("MainFragment RecipeClickListener", "Recipe clicked: " + recipeId);
			Toast.makeText(getContext(), "Recipe clicked: " + recipeId, Toast.LENGTH_LONG).show();
		}
	};
}