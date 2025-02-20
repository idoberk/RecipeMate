package com.example.recipemate.Listeners;

import com.example.recipemate.Modals.SearchRecipeApiResponse;

public interface SearchRecipeResponseListener {
    void didFetch(SearchRecipeApiResponse response, String message);
    void didError(String message);
}
