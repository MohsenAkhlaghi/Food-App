package com.example.foodapp.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentFavoriteRecipesBinding
import com.example.foodapp.ui.MainViewModel
import com.example.foodapp.ui.adapter.FavoriteRecipesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment(R.layout.fragment_favorite_recipes) {
    private lateinit var binding: FragmentFavoriteRecipesBinding
    private val mAdapter: FavoriteRecipesAdapter by lazy { FavoriteRecipesAdapter() }
    private val viewModelMain: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModelMain = viewModelMain
        binding.mAdapter = mAdapter
        setupRecyclerView(binding.favoriteRecipesRecyclerview)
        viewModelMain.readFavoriteRecipes.observe(viewLifecycleOwner) { favoriteEntity ->
            mAdapter.setData(favoriteEntity)
        }

    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        binding.favoriteRecipesRecyclerview.adapter = mAdapter
        binding.favoriteRecipesRecyclerview.layoutManager = LinearLayoutManager(requireContext())
    }

}