package com.example.foodapp.data.datasource

import com.example.foodapp.data.remote.IFoodRecipesApi
import com.example.foodapp.models.dto.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val iFoodRecipesApi: IFoodRecipesApi) {
    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return iFoodRecipesApi.getRecipes(queries)
    }
}