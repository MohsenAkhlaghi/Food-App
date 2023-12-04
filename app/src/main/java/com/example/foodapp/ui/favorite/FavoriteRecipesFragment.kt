package com.example.foodapp.ui.favorite

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment(R.layout.fragment_favorite_recipes) {
    private lateinit var binding: FragmentFavoriteRecipesBinding
    private val viewModelMain: MainViewModel by viewModels()
    private val mAdapter: FavoriteRecipesAdapter by lazy { FavoriteRecipesAdapter(requireActivity(), viewModelMain) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModelMain = viewModelMain
        binding.mAdapter = mAdapter
        setHasOptionsMenu(true)
        setupRecyclerView(binding.favoriteRecipesRecyclerview)
        viewModelMain.readFavoriteRecipes.observe(viewLifecycleOwner) { favoriteEntity ->
            mAdapter.setData(favoriteEntity)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipes_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAll_favorite_recipes_menu) {
            viewModelMain.deleteAllFavoriteRecipes()
            showSnackBar()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        mAdapter.clearContextualActionMode()
    }

    private fun showSnackBar() {
        Snackbar.make(binding.root, "All recipes removed", Snackbar.LENGTH_SHORT).setAction("Okay") {}.show()
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        binding.favoriteRecipesRecyclerview.adapter = mAdapter
        binding.favoriteRecipesRecyclerview.layoutManager = LinearLayoutManager(requireContext())
    }

}