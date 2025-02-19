package com.example.recipemate.api;

import android.content.Context;

import com.example.recipemate.Listeners.RandomRecipeResponseListener;
import com.example.recipemate.Modals.RandomRecipeApiResponse;
import com.example.recipemate.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestManager {
	Context context;
	Retrofit retrofit = new Retrofit.Builder()
			.baseUrl("https://api.spoonacular.com/")
			.addConverterFactory(GsonConverterFactory.create())
			.build();

	public RequestManager(Context context) {
		this.context = context;
	}

	public void getRandomRecipes(String category, RandomRecipeResponseListener listener) {
		CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);
		Call<RandomRecipeApiResponse> call = callRandomRecipes.callRandomRecipes(context.getString(R.string.api_key), "50", category);
		call.enqueue(new Callback<RandomRecipeApiResponse>() {
			@Override
			public void onResponse(Call<RandomRecipeApiResponse> call, Response<RandomRecipeApiResponse> response) {
				if (!response.isSuccessful()) {
					listener.didError(response.message());
					return;
				}
				listener.didFetch(response.body(), response.message());
			}

			@Override
			public void onFailure(Call<RandomRecipeApiResponse> call, Throwable t) {
                listener.didError(t.getMessage());
			}
		});
	}

	private interface CallRandomRecipes{
	    @GET("recipes/random")
	    Call<RandomRecipeApiResponse> callRandomRecipes(
            @Query("apiKey") String apiKey,
            @Query("number") String number,
            @Query("tags") String tags
	    );
	}
}
