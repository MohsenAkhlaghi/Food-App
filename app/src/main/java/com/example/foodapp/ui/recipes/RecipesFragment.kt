package com.example.foodapp.ui.recipes

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentRecipesBinding
import com.example.foodapp.ui.MainViewModel
import com.example.foodapp.ui.adapter.RecipesAdapter
import com.example.foodapp.util.NetworkResult
import com.example.foodapp.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment(R.layout.fragment_recipes) {
    private lateinit var binding: FragmentRecipesBinding
    private lateinit var viewModelMain: MainViewModel
    private lateinit var viewModelRecipes: RecipesViewModel
    private val mAdapter by lazy { RecipesAdapter() }
    private val args by navArgs<RecipesFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelMain = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        viewModelRecipes = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModelMain

        setupRecyclerView()
        readDatabase()

        binding.recipesFab.setOnClickListener {
            findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
        }

    }

    private fun readDatabase() {
        lifecycleScope.launch {
            viewModelMain.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    mAdapter.setData(database[0].foodRecipe)
                    hideProgress()
                } else {
                    requestApiData()
                }
            }
        }

    }

    private fun requestApiData() {
        Log.e(TAG, "requestApiData: ")
        viewModelMain.getRecipes(viewModelRecipes.applyQueries())
        viewModelMain.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
//                    hideShimmerEffect()
                    hideProgress()
                    response.data?.let { mAdapter.setData(it) }
                }

                is NetworkResult.Error -> {
//                    hideShimmerEffect()
                    hideProgress()
                    localDataFromCache()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
//                    showShimmerEffect()
                    showProgress()
                }
            }
        }
    }

    private fun localDataFromCache() {
        lifecycleScope.launch {
            viewModelMain.readRecipes.observe(viewLifecycleOwner) { database ->
                if (database.isNullOrEmpty()) {
                    mAdapter.setData(database[0].foodRecipe)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
//        showShimmerEffect()
        showProgress()
    }

    private fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }

    /*private fun showShimmerEffect() {
        binding.recyclerview.showShimmer()
    }*/

    /*private fun hideShimmerEffect() {
        binding.recyclerview.hideShimmer()
    }*/

}