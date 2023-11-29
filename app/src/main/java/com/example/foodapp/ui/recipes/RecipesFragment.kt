package com.example.foodapp.ui.recipes

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.SearchView
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
    private lateinit var viewModelMain: MainViewModel
    private lateinit var viewModelRecipes: RecipesViewModel
    private val mAdapter by lazy { RecipesAdapter() }
    private val args by navArgs<RecipesFragmentArgs>()

    //NETWORK LISTENER (2)
    private lateinit var networkListener: NetworkListener

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

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
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
                    loadDataFromCache()
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
//                    showShimmerEffect()
                    showProgress()
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