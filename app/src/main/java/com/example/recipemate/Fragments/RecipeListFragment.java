package com.example.recipemate.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.recipemate.Activities.MainActivity;
import com.example.recipemate.Adapters.RandomRecipeAdapter;
import com.example.recipemate.Listeners.RandomRecipeResponseListener;
import com.example.recipemate.Modals.RandomRecipeApiResponse;
import com.example.recipemate.R;
import com.example.recipemate.api.RequestManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeListFragment extends Fragment {

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	public RecipeListFragment() {
		// Required empty public constructor
	}

//	MainActivity mainActivity;
	RequestManager requestManager;
	RandomRecipeAdapter randomRecipeAdapter;
	RecyclerView recyclerView;
	String category;

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment RecipeListFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static RecipeListFragment newInstance(String param1, String param2) {
		RecipeListFragment fragment = new RecipeListFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			category = getArguments().getString("category", "");
		}

//        requestManager = new RequestManager(mainActivity);
//        requestManager.getRandomRecipes(randomRecipeResponseListener);

	}

//	private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
//		@Override
//		public void didFetch(RandomRecipeApiResponse response, String message) {
//            randomRecipeAdapter = new RandomRecipeAdapter(requireContext(), response.recipes);
//			recyclerView.setAdapter(randomRecipeAdapter);
//		}
//
//		@Override
//		public void didError(String message) {
//			Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
//		}
//	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
		recyclerView = view.findViewById(R.id.RecipeListRV);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

		// requestManager.getRandomRecipes(category, randomRecipeResponseListener);

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
				randomRecipeAdapter = new RandomRecipeAdapter(requireContext(), response.recipes);
				recyclerView.setAdapter(randomRecipeAdapter);
			}

			@Override
			public void didError(String message) {
				Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
			}
		});
	}
}