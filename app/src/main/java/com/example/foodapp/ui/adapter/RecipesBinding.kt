package com.example.foodapp.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.example.foodapp.R
import com.example.foodapp.data.local.RecipesEntity
import com.example.foodapp.models.dto.FoodRecipe
import com.example.foodapp.util.NetworkResult
import retrofit2.Response

class RecipesBinding {

    companion object {

        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun errorViewVisibility(imageView: ImageView, apiResponse: NetworkResult<FoodRecipe>?, database: List<RecipesEntity>?) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                imageView.visibility = View.VISIBLE
            } else if (apiResponse is NetworkResult.Loading) {
                imageView.visibility = View.VISIBLE
            } else if (apiResponse is NetworkResult.Success) {
                imageView.visibility = View.VISIBLE
            }
        }

        @BindingAdapter("readApiResponse2", "readDatabase2", requireAll = true)
        @JvmStatic
        fun errorTextVisibility(textView: TextView, apiResponse: NetworkResult<FoodRecipe>?, database: List<RecipesEntity>?) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                textView.visibility = View.VISIBLE
                textView.text = apiResponse.message.toString()
            } else if (apiResponse is NetworkResult.Loading) {
                textView.visibility = View.VISIBLE
            } else if (apiResponse is NetworkResult.Success) {
                textView.visibility = View.VISIBLE
            }
        }

    }

}