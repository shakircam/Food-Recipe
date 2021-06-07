package com.example.foodrecipe.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodrecipe.data.local.entity.FavoritesEntity
import com.example.foodrecipe.data.local.entity.RecipesEntity


@Database(entities = [RecipesEntity::class,FavoritesEntity::class],
    version = 1,
    exportSchema = false)

  @TypeConverters(RecipesTypeConverter::class)
  abstract class RecipesDatabase : RoomDatabase() {

       abstract fun recipesDao() : RecipesDao
}