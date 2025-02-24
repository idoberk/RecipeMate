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
import com.example.recipemate.Listeners.RandomRecipeResponseListener;
import com.example.recipemate.Listeners.RecipeClickListener;
import com.example.recipemate.Modals.RandomRecipeApiResponse;
import com.example.recipemate.R;
import com.example.recipemate.api.RequestManager;

import org.apache.commons.lang3.StringUtils;

public class RecipeListFragment extends Fragment {

	public RecipeListFragment() {
		// Required empty public constructor
	}

	private RequestManager requestManager;
	private RandomRecipeAdapter randomRecipeAdapter;
	private RecyclerView recyclerView;
	private TextView categoryTitle, resultText;
	private String category;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			category = getArguments().getString("category", "");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
		recyclerView = view.findViewById(R.id.RecipeListRV);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		categoryTitle = view.findViewById(R.id.CategoryText);
		resultText = view.findViewById(R.id.ResultsText);
		resultText.setVisibility(View.VISIBLE);
		categoryTitle.setText(StringUtils.capitalize(category));

		if (getArguments() != null) {
			category = getArguments().getString("category", "");
			Log.d("Category", "Received category: " + category);
		}

		requestManager = new RequestManager(requireContext());
		fetchRecipes();

		return view;
	}

	private void fetchRecipes() {
		Log.d("Category", "Fetching recipes for category: " + category);
		requestManager.getRandomRecipes(category, new RandomRecipeResponseListener() {
			@Override
			public void didFetch(RandomRecipeApiResponse response, String message) {
				Log.d("Category", "Fetched recipes for category: " + category);
				randomRecipeAdapter = new RandomRecipeAdapter(requireContext(), response.recipes, recipeClickListener, 2);
				recyclerView.setAdapter(randomRecipeAdapter);
			}

			@Override
			public void didError(String message) {
				Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
			}
		});
	}

	private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
		@Override
		public void onRecipeClick(String recipeId) {
			Bundle bundle = new Bundle();
			bundle.putInt("recipeId", Integer.parseInt(recipeId));
			Navigation.findNavController(requireView()).navigate(R.id.action_recipeListFragment_to_recipeDetailsFragment, bundle);
			Log.d("RecipeListFragment RecipeClickListener", "Recipe clicked: " + recipeId);
			Toast.makeText(getContext(), "Recipe clicked: " + recipeId, Toast.LENGTH_LONG).show();
		}
	};
}