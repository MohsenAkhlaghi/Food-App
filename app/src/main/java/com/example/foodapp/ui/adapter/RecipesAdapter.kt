package com.example.foodapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.RecipesRowLayoutBinding
import com.example.foodapp.models.dto.FoodRecipe
import com.example.foodapp.models.dto.Result

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {
    //Old List
    private var recipes = emptyList<Result>()

    class MyViewHolder(private val binding: RecipesRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result) {
            binding.result = result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipe = recipes[position]
        holder.bind(currentRecipe)
    }


    fun setData(newData:FoodRecipe) {
        //newData = New List
        val recipesDiffUtil = RecipesDiffUtil(recipes,newData.results)
        //فانکشن calculateDiff میاد فقط مقادیر متفاوت رو آپدیت می کنه، یعنی جدیدا رو جایگزین قدیمیا می کنه
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipes = newData.results
        //دستور dispatchUpdatesTo تغییراتی که پیدا کرده روی این کلاس که درش هستیم اعمال می کند
        diffUtilResult.dispatchUpdatesTo(this)
    }

}