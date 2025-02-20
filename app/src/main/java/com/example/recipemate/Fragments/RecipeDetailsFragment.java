package com.example.recipemate.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recipemate.R;


public class RecipeDetailsFragment extends Fragment {
	private static final String ARG_RECIPE_ID = "recipe_id";

	private int id;
	private ImageView imageView_DishImage;
    private TextView textView_DishDetailsTitle,textViewIngredients;
    private RecyclerView recyclerViewIngredientsRecView;

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
		

		return view;
	}

	private void initViews(View view) {
		imageView_DishImage = view.findViewById(R.id.DishImage);
		textView_DishDetailsTitle = view.findViewById(R.id.DishDetailsTitle);
		textViewIngredients = view.findViewById(R.id.IngredientsTitle);
		recyclerViewIngredientsRecView = view.findViewById(R.id.IngredientsRecView);
	}
}