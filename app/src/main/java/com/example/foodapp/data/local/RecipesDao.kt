package com.example.foodapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    /**
     * ذخیره و وارد کردن موادغذایی در دیتابیس
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    /**
     * ذخیره و وارد کردن موادغذایی مورد علاقه در دیتابیس
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipes(favoriteEntity: FavoriteEntity)

    /**
     * ذخیره و وارد کردن جُک در دیتابیس
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)

    /**
     * صدا کردن و خواندن موادغذایی از دیتابیس
     */
    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>>


    /**
     * صدا کردن و خواندن موادغذایی مورد علاقه از دیتابیس
     */
    @Query("SELECT * FROM favorite_recipes_table ORDER BY id ASC")
    fun readFavoriteRecipes(): Flow<List<FavoriteEntity>>

    /**
     * صدا کردن و خواندن جُک از دیتابیس
     */
    @Query("SELECT * FROM food_joke_table ORDER BY id ASC")
    fun readFoodJoke(): Flow<List<FoodJokeEntity>>

    /**
     * حذف یک موادغذایی مورد علاقه از دیتابیس
     */
    @Delete
    suspend fun deleteFavoriteRecipe(favoriteEntity: FavoriteEntity)

    /**
     * حذف تمامی موادغذایی مورد علاقه ها از دیتابیس
     */
    @Query("DELETE FROM favorite_recipes_table")
    suspend fun deleteAllFavoriteRecipes()


}