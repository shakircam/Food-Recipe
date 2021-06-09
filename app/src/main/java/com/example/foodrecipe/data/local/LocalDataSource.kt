package com.example.foodrecipe.data.local

import com.example.foodrecipe.data.local.database.RecipesDao
import com.example.foodrecipe.data.local.entity.FavoritesEntity
import com.example.foodrecipe.data.local.entity.FoodJokeEntity
import com.example.foodrecipe.data.local.entity.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao) {

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }

    suspend fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity) {
        recipesDao.insertFavoriteRecipes(favoritesEntity)
    }

    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) {
        recipesDao.insertFoodJoke(foodJokeEntity)
    }

    fun readRecipes(): Flow<List<RecipesEntity>> {
       return recipesDao.readRecipes()
    }

    fun readFavoriteRecipes(): Flow<List<FavoritesEntity>> {
        return recipesDao.readFavoriteRecipes()
    }

    fun readFoodJoke(): Flow<List<FoodJokeEntity>> {
        return recipesDao.readFoodJoke()
    }

    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity){
        return recipesDao.deleteFavoriteRecipe(favoritesEntity)
    }
    suspend fun deleteAllFavoriteRecipes(){
        return recipesDao.deleteAllFavoriteRecipes()
    }
}