package com.example.foodapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodapp.models.dto.Result
import com.example.foodapp.util.Constants.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result,
)