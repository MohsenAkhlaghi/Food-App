package com.example.foodapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foodapp.R
import com.example.foodapp.databinding.IngredientsRowLayoutBinding
import com.example.foodapp.models.dto.ExtendedIngredient
import com.example.foodapp.util.Constants.BASE_IMAGE_URL

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredientsList = emptyList<ExtendedIngredient>()

    class MyViewHolder(private val binding: IngredientsRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ExtendedIngredient) {
            with(binding) {
                ingredientImageView.load(BASE_IMAGE_URL + item.image) {
                    crossfade(600)
                    error(R.drawable.ic_error)
                }
                ingredientName.text = item.name.capitalize()
                ingredientAmount.text = item.amount.toString()
                ingredientUnit.text = item.unit
                ingredientConsistency.text = item.consistency
                ingredientOriginal.text = item.original
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = IngredientsRowLayoutBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentIngredients = ingredientsList[position]
        holder.bind(currentIngredients)
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun setData(newIngredients: List<ExtendedIngredient>) {
        //ساخت دیف یوتیل و پاس دادن لیست مواد تشکیل دهنده
        val ingredientsDiffUtil = RecipesDiffUtil(ingredientsList, newIngredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientsList = newIngredients
        diffUtilResult.dispatchUpdatesTo(this)
    }


}