package com.example.foodapp.ui.resipes

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentRecipesBinding
import com.example.foodapp.ui.MainViewModel
import com.example.foodapp.ui.adapter.RecipesAdapter
import com.example.foodapp.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment(R.layout.fragment_recipes) {
    private lateinit var binding: FragmentRecipesBinding
    private lateinit var viewModelMain: MainViewModel
    private lateinit var viewModelRecipes: RecipesViewModel
    private val mAdapter by lazy { RecipesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelMain = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        viewModelRecipes = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!
        binding.lifecycleOwner = viewLifecycleOwner

        setupRecyclerView()
        requestApiData()
    }

    private fun requestApiData() {
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
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
//                    showShimmerEffect()
                    showProgress()
                }
            }
        }
    }

    /*private fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        //تعداد کوئری و نتایجی که می خوایم برگردونیم
        queries["number"] = "50"
        queries["apiKey"] = API_KEY
        queries["type"] = "snack"
        queries["diet"] = "vegan"
        queries["addRecipeInformation"] = "true"
        queries["fillIngredients"] = "true"
        return queries
    }*/

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