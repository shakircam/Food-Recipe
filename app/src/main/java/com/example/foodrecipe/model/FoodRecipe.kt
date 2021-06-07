package com.example.foodrecipe.model


import com.example.foodrecipe.model.Result
import com.google.gson.annotations.SerializedName

data class FoodRecipe(

    @SerializedName("results")
    val results: List<Result>,

    )