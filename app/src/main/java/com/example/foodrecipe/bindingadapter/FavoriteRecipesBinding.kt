package com.example.foodrecipe.bindingadapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipe.adapter.FavoriteRecipesAdapter
import com.example.foodrecipe.data.local.entity.FavoritesEntity

class FavoriteRecipesBinding {

    companion object{
        @BindingAdapter("viewVisibility","setData",requireAll = false)
        @JvmStatic
        fun setDataAndViewVisibility(
            view : View,
            favoritesEntity: List<FavoritesEntity>?,
            mAdapter : FavoriteRecipesAdapter?
        ){
            when (view) {
                is RecyclerView -> {
                    val dataCheck = favoritesEntity.isNullOrEmpty()
                    view.isInvisible = dataCheck
                    if(!dataCheck){
                        favoritesEntity?.let { mAdapter?.setData(it) }
                    }
                }
                else -> view.isVisible = favoritesEntity.isNullOrEmpty()
            }
        }

    }
}