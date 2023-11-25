package com.example.foodapp.ui.foodjoke

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentFoodJokeBinding
import com.example.foodapp.databinding.FragmentRecipesBinding

class FoodJokeFragment : Fragment(R.layout.fragment_food_joke) {
    private lateinit var binding: FragmentFoodJokeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!
        binding.lifecycleOwner = viewLifecycleOwner
    }


}