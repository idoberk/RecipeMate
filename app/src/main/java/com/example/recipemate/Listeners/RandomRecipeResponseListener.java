package com.example.recipemate.Listeners;

import com.example.recipemate.Modals.RandomRecipeApiResponse;
import com.example.recipemate.Modals.SearchRecipeApiResponse;

public interface RandomRecipeResponseListener {
    void didFetch(RandomRecipeApiResponse response, String message);
    void didError(String message);
}

