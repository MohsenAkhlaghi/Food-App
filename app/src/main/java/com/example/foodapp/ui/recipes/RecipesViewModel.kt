package com.example.foodapp.ui.recipes

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodapp.data.DataStoreRepository
import com.example.foodapp.util.Constants.API_KEY
import com.example.foodapp.util.Constants.DEFAULT_DIET_TYPE
import com.example.foodapp.util.Constants.DEFAULT_MEAL_TYPE
import com.example.foodapp.util.Constants.DEFAULT_RECIPES_NUMBER
import com.example.foodapp.util.Constants.QUERY_ADD_RECIPE_INFORMATION
import com.example.foodapp.util.Constants.QUERY_API_KEY
import com.example.foodapp.util.Constants.QUERY_DIET
import com.example.foodapp.util.Constants.QUERY_FILL_INGREDIENTS
import com.example.foodapp.util.Constants.QUERY_NUMBER
import com.example.foodapp.util.Constants.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    var mealType = DEFAULT_MEAL_TYPE
    var dietType = DEFAULT_DIET_TYPE

    //NETWORK LISTENER (4)
    var networkStatus = false

    //NETWORK LISTENER (10)
    var backOnline = false

    val readMealAndDietType = dataStoreRepository.readMealAndDietType

    //NETWORK LISTENER (11)
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int): Job {
        return viewModelScope.launch {
            dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }
    }

    //NETWORK LISTENER (12)
    fun saveBackOnline(backOnline: Boolean): Job {
        return viewModelScope.launch {
            dataStoreRepository.saveBackOnline(backOnline)
        }
    }


    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }

        //تعداد کوئری و نتایجی که می خوایم برگردونیم
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }

    //NETWORK LISTENER (5)
    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection", Toast.LENGTH_SHORT).show()
            //NETWORK LISTENER (13)
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "We're back online", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }

}