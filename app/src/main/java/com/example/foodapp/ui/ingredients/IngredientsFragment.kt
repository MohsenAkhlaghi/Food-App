package com.example.foodapp.ui.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentIngredientsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class IngredientsFragment : Fragment(R.layout.fragment_ingredients) {
    private lateinit var binding: FragmentIngredientsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

    }
}