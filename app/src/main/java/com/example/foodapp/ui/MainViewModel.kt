package com.example.foodapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodapp.domain.repository.Repository
import com.example.foodapp.models.dto.FoodRecipe
import com.example.foodapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository, application: Application) : AndroidViewModel(application) {

    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    /**
     *گرفتن و ذخیره کردن api در لایو دیتا
     */
    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    /**
     *چک کردن جواب api  در صورت وجود یا عدم وجود اتصال اینترنت (کانکشن)
     */
    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        //اگر اینترنت کانکشن برقرار بود
        if (hasInternetConnection()) {
            try {
                val response = repository.remoteDataSource.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    /**
     *مدیریت کردن انواع ارور ها و بررسی جواب های مختلف خود ای پی آی (Response) رو بررسی می کنه
     */
    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when {
            //اگر در متن پیام (message) کلمه ی تایم اوت (timeout) وجود داشت، ارور NetworkResult مون رو تایم اوت قرار بده
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            //اگر پاسخمون از api کدش برابر با 402 شد، ارور NetworkResult مون رو محدودیت ای پی آی کی (API Key) قرار بده
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            //اگر پاسخمون از api بدنش (body) خالی یا نال بود، ارور NetworkResult مون رو دستورالعمل (Recipes) درخواستی یافت نشد
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found.")
            }
            //اگر پاسخمون از api موفقیت آمیز بود، بدنه پاسخ را در NetworkResult.Success قرار بده
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            //دز غیر این صورت، پیام خود ارور api رو نمایش بده
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    /**
     * فانکشن برای چک کردن اتصال اینترنت
     */
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}