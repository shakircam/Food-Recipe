package com.example.foodrecipe.adapter

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipe.R
import com.example.foodrecipe.data.local.entity.FavoritesEntity
import com.example.foodrecipe.databinding.FavoriteRecipesItemLayoutBinding
import com.example.foodrecipe.ui.fragments.favorite.FavoriteRecipesFragmentDirections
import com.example.foodrecipe.ui.viewmodel.MainViewModel
import com.example.foodrecipe.util.RecipesDiffUtil
import com.google.android.material.snackbar.Snackbar

class FavoriteRecipesAdapter(private val requireActivity: FragmentActivity,
                             private val mainViewModel: MainViewModel
    ) : RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>(),ActionMode.Callback {

    private var multiSelection = false
    private lateinit var mActionMode : ActionMode
    private lateinit var rootView : View

    private var selectedRecipes = ArrayList<FavoritesEntity>()
    private var myViewHolder = ArrayList<MyViewHolder>()
    private var favoriteRecipes = emptyList<FavoritesEntity>()

    class MyViewHolder(val binding : FavoriteRecipesItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
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
        myViewHolder.add(holder)
        rootView = holder.itemView.rootView
        val currentRecipe = favoriteRecipes[position]
        holder.bind(currentRecipe)
        /**
         * Single onClickListener....
         */
        holder.binding.favoriteRecipesRowLayout.setOnClickListener {
            if (multiSelection){
                applySelection(holder,currentRecipe)
            } else{
                val action = FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(currentRecipe.result)
                holder.itemView.findNavController().navigate(action)
            }
        }
        /**
         * Long Click Listener
         */
        holder.binding.favoriteRecipesRowLayout.setOnLongClickListener {
            if (!multiSelection){
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentRecipe)
                true
            } else{
                multiSelection = false
                false
            }

        }
    }

    private fun applySelection(holder : MyViewHolder, currentRecipe : FavoritesEntity){
        if (selectedRecipes.contains(currentRecipe)){
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder,R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle()
        } else{
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder,R.color.cardBackgroundLightColor, R.color.colorPrimary)
            applyActionModeTitle()
        }
    }

    private fun changeRecipeStyle(holder : MyViewHolder, backgroundColor : Int, strokeColor : Int){
        holder.binding.favoriteRecipesRowLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity,backgroundColor)
        )
        holder.binding.favoriteRowCardView.strokeColor =
            ContextCompat.getColor(requireActivity,strokeColor)
    }

    private fun applyActionModeTitle(){
        when(selectedRecipes.size) {
            0 ->{
                mActionMode.finish()
            }
            1 ->{
                mActionMode.title = "${selectedRecipes.size} item selected!"
            }
            else ->{
                mActionMode.title = "${selectedRecipes.size} items selected!"
            }
        }
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

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favorite_delete_recipe,menu)
        mActionMode = mode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete_favorite_recipe_menu){
            selectedRecipes.forEach {
                mainViewModel.deleteFavoriteRecipe(it)
            }
            showSnackBar("${selectedRecipes.size} Recipes removed.")
            multiSelection = false
            selectedRecipes.clear()
            mode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        myViewHolder.forEach { holder ->
            changeRecipeStyle(holder,R.color.cardBackgroundColor,R.color.strokeColor)
        }
        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor =
            ContextCompat.getColor(requireActivity, color)
    }
    private fun showSnackBar(message : String){
         Snackbar.make(
             rootView,
             message,
             Snackbar.LENGTH_SHORT
         ).setAction("Okey"){}.show()
    }

    fun clearContextualActionMode() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }
}