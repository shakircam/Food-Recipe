package com.example.foodrecipe.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodrecipe.data.local.DataStoreRepository
import com.example.foodrecipe.util.Constants
import com.example.foodrecipe.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foodrecipe.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foodrecipe.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RecipesViewModel @Inject constructor (
    application: Application,
    private val dataStoreRepository: DataStoreRepository)
    : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

     var networkStatus = false
     var backOnline = false

    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    fun saveMealAndDietType(mealType : String, mealTypeId : Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }

    fun saveBackOnline(backOnline : Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }


     fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

    viewModelScope.launch { readMealAndDietType.collect { value ->
        mealType = value.selectedMealType
        dietType = value.selectedDietType
    }
      }

        queries[Constants.QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_TYPE] = mealType
        queries[Constants.QUERY_DIET] = dietType
        queries[Constants.QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[Constants.QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    fun applySearchQueries(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[Constants.QUERY_SEARCH] = searchQuery
        queries[Constants.QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[Constants.QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    fun showNetworkStatus(){
     if (!networkStatus){
         Toast.makeText(getApplication(),"No Internet Connection",Toast.LENGTH_SHORT).show()
         saveBackOnline(true)
     } else if (networkStatus){
         if (backOnline){
             Toast.makeText(getApplication(),"We are back online..",Toast.LENGTH_SHORT).show()
             saveBackOnline(false)
         }
     }
    }
}


