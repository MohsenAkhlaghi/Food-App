package com.example.foodapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.foodapp.data.DataStoreRepository.PreferenceKey.selectedDietType
import com.example.foodapp.data.DataStoreRepository.PreferenceKey.selectedDietTypeId
import com.example.foodapp.data.DataStoreRepository.PreferenceKey.selectedMealType
import com.example.foodapp.data.DataStoreRepository.PreferenceKey.selectedMealTypeId
import com.example.foodapp.util.Constants.DEFAULT_DIET_TYPE
import com.example.foodapp.util.Constants.DEFAULT_MEAL_TYPE
import com.example.foodapp.util.Constants.PREFERENCES_BACK_ONLINE
import com.example.foodapp.util.Constants.PREFERENCES_DIET_TYPE
import com.example.foodapp.util.Constants.PREFERENCES_DIET_TYPE_ID
import com.example.foodapp.util.Constants.PREFERENCES_MEAL_TYPE
import com.example.foodapp.util.Constants.PREFERENCES_MEAL_TYPE_ID
import com.example.foodapp.util.Constants.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKey {
        val selectedMealType = stringPreferencesKey(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(PREFERENCES_DIET_TYPE_ID)
        val backOnline = booleanPreferencesKey(PREFERENCES_BACK_ONLINE)
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

    //NETWORK LISTENER (8)
    suspend fun saveBackOnline(backOnline: Boolean) {
        context.dataStore.edit { preference ->
            preference[PreferenceKey.backOnline] = backOnline
        }
    }

    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        context.dataStore.edit { preferences ->
            preferences[selectedMealType] = mealType
            preferences[selectedMealTypeId] = mealTypeId
            preferences[selectedDietType] = dietType
            preferences[selectedDietTypeId] = dietTypeId
        }
    }

    val readMealAndDietType: Flow<MealAndDietType> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val selectedMealType = preferences[selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preferences[selectedMealTypeId] ?: 0
            val selectedDietType = preferences[selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = preferences[selectedDietTypeId] ?: 0
            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }

    //NETWORK LISTENER (9)
    val readBackOnline: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val backOnline = preferences[PreferenceKey.backOnline] ?: false
            backOnline
        }

}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)
