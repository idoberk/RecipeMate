package com.example.recipemate.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipemate.Modals.ExtendedIngredient;
import com.example.recipemate.R;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsViewHolder>{

	Context context;
	List<ExtendedIngredient> ingredientList;

	public IngredientsAdapter(Context context, List<ExtendedIngredient> ingredientList) {
		this.context = context;
		this.ingredientList = ingredientList;
	}

	@NonNull
	@Override
	public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.ingredients_list,parent,false);
		return new IngredientsViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
		holder.IngredientNameText.setText(ingredientList.get(position).name);
		holder.IngredientNameText.setSelected(true);
        holder.IngredientQuantityText.setText(ingredientList.get(position).original);
	}

	@Override
	public int getItemCount() {
		return ingredientList.size();
	}
}

class IngredientsViewHolder extends RecyclerView.ViewHolder {

	TextView IngredientNameText, IngredientQuantityText;

	public IngredientsViewHolder(@NonNull View itemView) {
		super(itemView);
		IngredientNameText = itemView.findViewById(R.id.IngredientNameText);
		IngredientQuantityText = itemView.findViewById(R.id.IngredientQuantityText);
	}
}