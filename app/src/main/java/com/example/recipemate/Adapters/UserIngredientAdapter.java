package com.example.recipemate.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipemate.Listeners.IngredientActionListener;
import com.example.recipemate.Modals.UserIngredient;
import com.example.recipemate.R;

import java.util.List;

public class UserIngredientAdapter extends RecyclerView.Adapter<IngredientViewHolder> {
	private Context context;
	private List<UserIngredient> userIngredientList;
	private IngredientActionListener listener;

	public UserIngredientAdapter(Context context, List<UserIngredient> userIngredientList, IngredientActionListener listener) {
		this.context = context;
		this.userIngredientList = userIngredientList;
		this.listener = listener;
	}

	@NonNull
	@Override
	public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.ingredient_item, parent, false);
		return new IngredientViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
		UserIngredient ingredient = userIngredientList.get(position);

		holder.ingredientNameText.setText(ingredient.getName());
		holder.ingredientQuantityText.setText(ingredient.getOriginal());
		holder.editIngredientButton.setSelected(ingredient.isSelected());

		holder.editIngredientButton.setOnClickListener(v -> {
			if (listener != null) {
				listener.onEditIngredient(holder.getAdapterPosition(), ingredient);
			}
		});

		holder.ingredientCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
			if (listener != null) {
				ingredient.setSelected(isChecked);
				listener.onIngredientSelectionChanged(holder.getAdapterPosition(), isChecked);
			}
		});
	}

	@Override
	public int getItemCount() {
		return userIngredientList.size();
	}

	public void updateIngredients(List<UserIngredient> ingredients) {
		this.userIngredientList = ingredients;
		notifyDataSetChanged();
	}
}

class IngredientViewHolder extends RecyclerView.ViewHolder{

	CheckBox ingredientCheckBox;
	TextView ingredientNameText, ingredientQuantityText;
	ImageButton editIngredientButton;


	public IngredientViewHolder(@NonNull View itemView){
		super(itemView);

		ingredientCheckBox = itemView.findViewById(R.id.ingredientCheckBox);
		ingredientNameText = itemView.findViewById(R.id.ingredientNameText);
		ingredientQuantityText = itemView.findViewById(R.id.ingredientQuantityText);
		editIngredientButton = itemView.findViewById(R.id.editIngredientButton);
	}
}