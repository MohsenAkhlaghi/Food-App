package com.example.foodapp.data.remote

import com.example.foodapp.models.dto.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface IFoodRecipesApi {

    /**
     * روش قدیمی کوئری نوشتن
     */
    /*@GET("/recipes/complexSearch")
    suspend fun getRecipesOld(
        @Query("number") number: Int = 10,
        @Query("name") name: String = "Ali",
    ): Response<FoodRecipe>*/

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<FoodRecipe>

    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(
        @QueryMap searchQueries: Map<String, String>
    ): Response<FoodRecipe>

}