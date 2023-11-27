package com.example.foodapp.data.datasource

import com.example.foodapp.data.local.RecipesDao
import com.example.foodapp.data.local.RecipesEntity
import com.example.foodapp.data.remote.IFoodRecipesApi
import com.example.foodapp.models.dto.FoodRecipe
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val recipesDao: RecipesDao) {

    fun readDatabase(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        return recipesDao.insertRecipes(recipesEntity)
    }

}