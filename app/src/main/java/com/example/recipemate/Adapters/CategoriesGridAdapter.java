package com.example.recipemate.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipemate.Modals.RecipesCategories;
import com.example.recipemate.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriesGridAdapter extends RecyclerView.Adapter<CategoriesGridAdapter.ViewHolder> {
	private List<RecipesCategories> categoriesList;
	private Context context;

	static class ViewHolder extends RecyclerView.ViewHolder {
		CardView cardView;
		ImageView categoryImage;
		TextView categoryName;

		ViewHolder(View itemView) {
			super(itemView);
			cardView = itemView.findViewById(R.id.cardView);
			categoryImage = itemView.findViewById(R.id.categoryImage);
			categoryName = itemView.findViewById(R.id.categoryName);
		}
	}

	public CategoriesGridAdapter(Context context, List<RecipesCategories> categoriesList) {
		this.context = context;
		this.categoriesList = categoriesList;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.categories_grid_item, viewGroup, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		RecipesCategories category = categoriesList.get(position);
		holder.categoryName.setText(category.getCategoryName());
		Picasso.get().load(category.getCategoryImage()).into(holder.categoryImage);

//		holder.cardView.setOnClickListener(view -> {
//
//		});

	}

	@Override
	public int getItemCount() {
		return categoriesList.size();
	}
}
