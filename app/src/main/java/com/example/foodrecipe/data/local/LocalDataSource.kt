package com.example.foodrecipe.data.local

import com.example.foodrecipe.data.local.database.RecipesDao
import com.example.foodrecipe.data.local.entity.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao) {

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }

    fun readRecipes(): Flow<List<RecipesEntity>> {
       return recipesDao.readRecipes()
    }
}