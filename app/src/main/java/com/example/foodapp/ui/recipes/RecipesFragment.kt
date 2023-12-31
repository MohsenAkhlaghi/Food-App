package com.example.foodapp.ui.recipes

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentRecipesBinding
import com.example.foodapp.ui.MainViewModel
import com.example.foodapp.ui.adapter.RecipesAdapter
import com.example.foodapp.util.NetworkListener
import com.example.foodapp.util.NetworkResult
import com.example.foodapp.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipesFragment : Fragment(R.layout.fragment_recipes), SearchView.OnQueryTextListener {
    private lateinit var binding: FragmentRecipesBinding
    private val viewModelMain: MainViewModel by viewModels()
    private val viewModelRecipes: RecipesViewModel by viewModels()
    private val mAdapter by lazy { RecipesAdapter() }
    private val args by navArgs<RecipesFragmentArgs>()
    //NETWORK LISTENER (2)
    private lateinit var networkListener: NetworkListener
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModelMain
        setHasOptionsMenu(true)
        setupRecyclerView()
        //NETWORK LISTENER (15)
        viewModelRecipes.readBackOnline.observe(viewLifecycleOwner) {
            viewModelRecipes.backOnline = it
        }

        //NETWORK LISTENER (3)
        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext()).collect { status ->
                Log.e(TAG, "networkListener: ${status.toString()}")
                //NETWORK LISTENER (6)
                viewModelRecipes.networkStatus = status
                viewModelRecipes.showNetworkStatus()
                //NETWORK LISTENER (14)
                readDatabase()
            }
        }

        binding.recipesFab.setOnClickListener {
            //NETWORK LISTENER (7)
            if (viewModelRecipes.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            } else {
                viewModelRecipes.showNetworkStatus()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            try {
                viewModelMain.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                    if (database.isNotEmpty() && !args.backFromBottomSheet) {
                        mAdapter.setData(database.first().foodRecipe)
                        hideProgress()
                    } else {
                        requestApiData()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                Log.e("RecipesFragment", "readDatabase: Error = ${e.message.toString()}")
            }
        }
    }

    private fun requestApiData() {
        viewModelMain.getRecipes(viewModelRecipes.applyQueries())
        viewModelMain.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideProgress()
                    response.data?.let { mAdapter.setData(it) }
                }

                is NetworkResult.Error -> {
                    hideProgress()
                    loadDataFromCache()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    showProgress()
                }
            }
        }
    }

    private fun searchApiData(searchQuery: String) {
        hideProgress()
        viewModelMain.searchRecipes(viewModelRecipes.applySearchQuery(searchQuery))
        viewModelMain.searchRecipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Error -> {
                    hideProgress()
                    loadDataFromCache()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    showProgress()
                }

                is NetworkResult.Success -> {
                    hideProgress()
                    val foodRecipes = response.data
                    foodRecipes?.let { mAdapter.setData(it) }

                }
            }
        }
    }

    /**
     * اطلاعات رو از کش می گیره
     */
    private fun loadDataFromCache() {
        lifecycleScope.launch {
            viewModelMain.readRecipes.observe(viewLifecycleOwner) { database ->
                if (database.isNullOrEmpty()) {
                    mAdapter.setData(database.first().foodRecipe)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showProgress()
    }

    private fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }
}