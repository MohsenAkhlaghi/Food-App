package com.example.foodapp.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentFavoriteRecipesBinding
import com.example.foodapp.databinding.FragmentRecipesBinding

class FavoriteRecipesFragment : Fragment(R.layout.fragment_favorite_recipes) {
    private lateinit var binding: FragmentFavoriteRecipesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!
        binding.lifecycleOwner = viewLifecycleOwner

    }

}