package com.example.foodapp.data.datasource

import com.example.foodapp.data.local.FavoriteEntity
import com.example.foodapp.data.local.RecipesDao
import com.example.foodapp.data.local.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val recipesDao: RecipesDao) {

    /**
     * ذخیره و وارد کردن موادغذایی در دیتابیس
     */
    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        return recipesDao.insertRecipes(recipesEntity)
    }

    /**
     * ذخیره و وارد کردن موادغذایی مورد علاقه در دیتابیس
     */
    suspend fun insertFavoriteRecipes(favoriteEntity: FavoriteEntity) {
        return recipesDao.insertFavoriteRecipes(favoriteEntity)
    }


    /**
     * صدا کردن و خواندن موادغذایی از دیتابیس
     */
    fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }

    /**
     * صدا کردن و خواندن موادغذایی مورد علاقه از دیتابیس
     */
    fun readFavoriteRecipes(): Flow<List<FavoriteEntity>> {
        return recipesDao.readFavoriteRecipes()
    }

    /**
     * حذف یک موادغذایی مورد علاقه از دیتابیس
     */
    suspend fun deleteFavoriteRecipe(favoriteEntity: FavoriteEntity) {
        return recipesDao.deleteFavoriteRecipe(favoriteEntity)
    }

    /**
     * حذف تمامی موادغذایی مورد علاقه ها از دیتابیس
     */
    suspend fun deleteAllFavoriteRecipes() {
        recipesDao.deleteAllFavoriteRecipes()
    }

}