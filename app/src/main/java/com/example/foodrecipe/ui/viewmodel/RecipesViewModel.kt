package com.example.foodrecipe.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrecipe.data.local.DataStoreRepository
import com.example.foodrecipe.util.Constants
import com.example.foodrecipe.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foodrecipe.util.Constants.Companion.DEFAULT_MEAL_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
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

    val readMealAndDietType = dataStoreRepository.readMealAndDietType

    fun saveMealAndDietType(mealType : String, mealTypeId : Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) { dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }


     fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

    viewModelScope.launch { readMealAndDietType.collect { value ->
        mealType = value.selectedMealType
        dietType = value.selectedDietType
    }
      }

        queries[Constants.QUERY_NUMBER] = "50"
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_TYPE] = mealType
        queries[Constants.QUERY_DIET] = dietType
        queries[Constants.QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[Constants.QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }


}


