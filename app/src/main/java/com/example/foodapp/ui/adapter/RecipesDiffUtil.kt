package com.example.foodapp.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.foodapp.models.dto.Result

class RecipesDiffUtil<T>(
    private val oldList: List<T>,
    private val newList: List<T>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        //مقایسه آدرس آبجکت ها در حافظه
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        //مقایسه محتویات آبجکت ها
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}