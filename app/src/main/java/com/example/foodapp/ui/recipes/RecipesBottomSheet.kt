package com.example.foodapp.ui.recipes

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.foodapp.R
import com.example.foodapp.databinding.RecipesBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class RecipesBottomSheet : BottomSheetDialogFragment(R.layout.recipes_bottom_sheet) {
    private lateinit var binding: RecipesBottomSheetBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!
        binding.lifecycleOwner = viewLifecycleOwner

    }
}