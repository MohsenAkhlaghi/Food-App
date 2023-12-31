package com.example.foodapp.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodapp.models.dto.FoodJoke
import com.example.foodapp.models.dto.Result
import com.example.foodapp.util.Constants.FAVORITE_RECIPES_TABLE
import com.example.foodapp.util.Constants.FOOD_JOKE_TABLE

@Entity(tableName = FOOD_JOKE_TABLE)
class FoodJokeEntity(
    @Embedded
    var foodJoke: FoodJoke
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}