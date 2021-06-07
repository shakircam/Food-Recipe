package com.example.foodrecipe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipe.data.local.entity.FavoritesEntity
import com.example.foodrecipe.databinding.FavoriteRecipesItemLayoutBinding
import com.example.foodrecipe.ui.viewmodel.MainViewModel
import com.example.foodrecipe.util.RecipesDiffUtil

class FavoriteRecipesAdapter(private val requireActivity: FragmentActivity,
                             private val mainViewModel: MainViewModel
    ) : RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>() {
    private var favoriteRecipes = emptyList<FavoritesEntity>()

    class MyViewHolder(private val binding : FavoriteRecipesItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favoritesEntity: FavoritesEntity){
            binding.favoritesEntity = favoritesEntity
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup): MyViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipesItemLayoutBinding.inflate(layoutInflater,parent,false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipe = favoriteRecipes[position]
        holder.bind(currentRecipe)
    }

    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }

    fun setData(newFavoriteRecipes: List<FavoritesEntity>) {
        val favoriteRecipesDiffUtil =
            RecipesDiffUtil(favoriteRecipes, newFavoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteRecipesDiffUtil)
        favoriteRecipes = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }
}