package com.example.foodrecipe.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodrecipe.model.Result
import com.example.foodrecipe.util.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result

)
