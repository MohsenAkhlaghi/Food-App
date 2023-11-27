package com.example.foodapp.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.foodapp.models.dto.Result

class RecipesDiffUtil(
    private val oldList: List<Result>,
    private val newList: List<Result>,
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