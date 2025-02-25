package com.example.recipemate.api;

import android.content.Context;

import com.example.recipemate.Listeners.RandomRecipeResponseListener;
import com.example.recipemate.Listeners.RecipeDetailsListener;
import com.example.recipemate.Listeners.SearchRecipeResponseListener;
import com.example.recipemate.Modals.RandomRecipeApiResponse;
import com.example.recipemate.Modals.RecipeDetailsResponse;
import com.example.recipemate.Modals.SearchRecipeApiResponse;
import com.example.recipemate.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
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
		CallRecipeApi callRecipeApi = retrofit.create(CallRecipeApi.class);
		Call<RandomRecipeApiResponse> call = callRecipeApi.callRandomRecipes(context.getString(R.string.api_key), "25", category);
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

	public void searchRecipes(String searchQuery, SearchRecipeResponseListener listener) {
		CallRecipeApi callSearchRecipes = retrofit.create(CallRecipeApi.class);
		Call<SearchRecipeApiResponse> call = callSearchRecipes.callSearchRecipes(context.getString(R.string.api_key), searchQuery, "25");

		call.enqueue(new Callback<SearchRecipeApiResponse>() {
			@Override
			public void onResponse(Call<SearchRecipeApiResponse> call, Response<SearchRecipeApiResponse> response) {
				if (!response.isSuccessful()) {
					listener.didError(response.message());
					return;
				}
				listener.didFetch(response.body(), response.message());
			}

			@Override
			public void onFailure(Call<SearchRecipeApiResponse> call, Throwable t) {
				listener.didError(t.getMessage());
			}
		});
	}

	public void getRecipeDetails(RecipeDetailsListener listener, int id) {
		CallRecipeDetails callRecipeDetails = retrofit.create(CallRecipeDetails.class);
		Call<RecipeDetailsResponse> call = callRecipeDetails.callRecipeDetails(id, context.getString(R.string.api_key));
		call.enqueue(new Callback<RecipeDetailsResponse>() {
			@Override
			public void onResponse(Call<RecipeDetailsResponse> call, Response<RecipeDetailsResponse> response) {
				if (!response.isSuccessful()) {
					listener.didError(response.message());
					return;
				}
				listener.didFetch(response.body(), response.message());
			}

			@Override
			public void onFailure(Call<RecipeDetailsResponse> call, Throwable t) {
				listener.didError(t.getMessage());
			}
		});
	}

	private interface CallRecipeApi {
	    @GET("recipes/random")
	    Call<RandomRecipeApiResponse> callRandomRecipes(
            @Query("apiKey") String apiKey,
            @Query("number") String number,
            @Query("tags") String tags
	    );

		@GET("recipes/complexSearch")
		Call<SearchRecipeApiResponse> callSearchRecipes(
			@Query("apiKey") String apiKey,
			@Query("query") String query,
			@Query("number") String number
		);
	}

	private interface CallRecipeDetails {
		@GET("recipes/{id}/information")
		Call<RecipeDetailsResponse> callRecipeDetails(
			@Path("id") int id,
			@Query("apiKey") String apiKey
		);
	}
}
