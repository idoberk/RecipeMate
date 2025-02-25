package com.example.recipemate.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipemate.Listeners.RecipeClickListener;
import com.example.recipemate.Managers.FavoriteManager;
import com.example.recipemate.Modals.Recipe;
import com.example.recipemate.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RandomRecipeAdapter extends RecyclerView.Adapter<RandomRecipeViewHolder> {

    FavoriteManager favoriteManager;
    Context context;
    List<Recipe> recipeList;
    RecipeClickListener listener;
    int adapterType;

    public RandomRecipeAdapter(Context context, List<Recipe> recipeList, RecipeClickListener listener, int adapterType) {
        this.context = context;
        this.recipeList = recipeList;
        this.listener = listener;
        this.adapterType = adapterType;
        this.favoriteManager = new FavoriteManager();
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
        if (adapterType == 1) {
            holder.textView_Servings.setVisibility(View.GONE);
            holder.textView_Time.setVisibility(View.GONE);
            holder.imageView_CookingTimeImage.setVisibility(View.GONE);
            holder.imageView_ServingsAmountImage.setVisibility(View.GONE);
        }
        else {
            String servings = recipe.servings == 1 ? " Serving" : " Servings";
            holder.textView_Time.setText(recipe.readyInMinutes + " Minutes");
            holder.textView_Servings.setText(recipe.servings + servings);
        }

        holder.textView_Title.setText(recipe.title);
        holder.textView_Title.setSelected(true);
        Picasso.get().load(recipeList.get(position).image)
                .resize(600, 300)
                .centerCrop()
                .into(holder.imageView_RecipeImage);

        holder.random_list_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRecipeClick(String.valueOf(recipeList.get(holder.getAdapterPosition()).id));
            }
        });

        favoriteManager.isRecipeFavorite(recipe, new FavoriteManager.FavoriteCheckCallback() {
            @Override
            public void onSuccess(boolean isFavorite) {
                holder.favoriteButton.setSelected(isFavorite);
                holder.favoriteButton.setImageResource(
                        isFavorite ? R.drawable.ic_favorite_selected : R.drawable.ic_favorites
                );
            }

            @Override
            public void onError(String error) {
                Log.d("Favorite", "Error: " + error);
                Toast.makeText(context, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        holder.favoriteButton.setOnClickListener(view -> {
            favoriteManager.toggleFavorite(recipe, new FavoriteManager.FavoriteCallback() {
                @Override
                public void onSuccess() {
                    boolean buttonState = !holder.favoriteButton.isSelected();

                    holder.favoriteButton.setSelected(buttonState);
                    holder.favoriteButton.setImageResource(
                            buttonState ? R.drawable.ic_favorite_selected : R.drawable.ic_favorites
                    );
                }

                @Override
                public void onError(String error) {
                    Log.d("Favorite", "Error: " + error);
                    Toast.makeText(context, "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            });
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
    ImageView imageView_RecipeImage, imageView_CookingTimeImage, imageView_ServingsAmountImage;
    ImageButton favoriteButton;

    public RandomRecipeViewHolder(@NonNull View itemView){
        super(itemView);

        random_list_container = itemView.findViewById(R.id.RecipeCard);
        textView_Title = itemView.findViewById(R.id.DishTitle);
        textView_Servings = itemView.findViewById(R.id.ServingsAmountText);
        textView_Time = itemView.findViewById(R.id.CookingTimeText);
        imageView_RecipeImage = itemView.findViewById(R.id.RecipeImage);
        favoriteButton = itemView.findViewById(R.id.FavoriteButton);
        imageView_CookingTimeImage =  itemView.findViewById(R.id.CookingTime);
        imageView_ServingsAmountImage = itemView.findViewById(R.id.ServingsAmount);
    }
}