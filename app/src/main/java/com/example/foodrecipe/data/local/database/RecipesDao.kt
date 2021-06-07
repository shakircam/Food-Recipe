package com.example.foodrecipe.data.local.database

import androidx.room.*
import com.example.foodrecipe.data.local.entity.FavoritesEntity
import com.example.foodrecipe.data.local.entity.RecipesEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity)

    @Query("SELECT * FROM recipes_table ORDER BY id ASC" )
    fun readRecipes() : Flow<List<RecipesEntity>>

    @Query("SELECT * FROM favorite_recipes_table ORDER BY id ASC" )
    fun readFavoriteRecipes() : Flow<List<FavoritesEntity>>

    @Delete
    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity)

    @Query("DELETE FROM FAVORITE_RECIPES_TABLE")
    suspend fun deleteAllFavoriteRecipes()
}