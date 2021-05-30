package com.example.foodrecipe.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodrecipe.model.FoodRecipe
import com.example.foodrecipe.util.Constants.Companion.RECIPES_TABLE


@Entity(tableName = RECIPES_TABLE )
class RecipesEntity (
    var foodRecipe: FoodRecipe
        ){
    @PrimaryKey(autoGenerate = false)
    var id : Int = 0
}