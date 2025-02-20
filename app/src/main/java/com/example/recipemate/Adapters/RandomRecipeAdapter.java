package com.example.recipemate.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipemate.Listeners.RecipeClickListener;
import com.example.recipemate.Modals.Recipe;
import com.example.recipemate.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RandomRecipeAdapter extends RecyclerView.Adapter<RandomRecipeViewHolder> {

    Context context;
    List<Recipe> recipeList;
    RecipeClickListener listener;

    public RandomRecipeAdapter(Context context, List<Recipe> recipeList, RecipeClickListener listener) {
        this.context = context;
        this.recipeList = recipeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RandomRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_card,parent,false);
        return new RandomRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RandomRecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        String servings = recipe.servings == 1 ? " Serving" : " Servings";
        holder.textView_Title.setText(recipe.title);
        holder.textView_Title.setSelected(true);
        holder.textView_Time.setText(recipe.readyInMinutes + " Minutes");
        holder.textView_Servings.setText(recipe.servings + servings);
        Picasso.get().load(recipeList.get(position).image).into(holder.imageView_RecipeImage);

        holder.random_list_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRecipeClick(String.valueOf(recipeList.get(holder.getAdapterPosition()).id));
            }
        });


        holder.favoriteButton.setOnClickListener(view -> {
            holder.favoriteButton.setSelected(!holder.favoriteButton.isSelected());
            if (holder.favoriteButton.isSelected()) {
                holder.favoriteButton.setImageResource(R.drawable.ic_favorite_selected);
            } else {
                holder.favoriteButton.setImageResource(R.drawable.ic_favorites);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }
}

class RandomRecipeViewHolder extends RecyclerView.ViewHolder{

    CardView random_list_container;
    TextView textView_Title, textView_Servings, textView_Time;
    ImageView imageView_RecipeImage;
    ImageButton favoriteButton;

    public RandomRecipeViewHolder(@NonNull View itemView){
        super(itemView);

        random_list_container = itemView.findViewById(R.id.RecipeCard);
        textView_Title = itemView.findViewById(R.id.DishTitle);
        textView_Servings = itemView.findViewById(R.id.CookingTimeText);
        textView_Time = itemView.findViewById(R.id.ServingsAmountText);
        imageView_RecipeImage = itemView.findViewById(R.id.RecipeImage);
        favoriteButton = itemView.findViewById(R.id.FavoriteButton);
    }
}