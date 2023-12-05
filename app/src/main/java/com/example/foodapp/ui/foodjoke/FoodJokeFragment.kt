package com.example.foodapp.ui.foodjoke

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentFoodJokeBinding
import com.example.foodapp.ui.MainViewModel
import com.example.foodapp.util.Constants.API_KEY
import com.example.foodapp.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodJokeFragment : Fragment(R.layout.fragment_food_joke) {
    private lateinit var binding: FragmentFoodJokeBinding
    private val viewModelMain: MainViewModel by viewModels()
    private var foodJoke = "No Food Joke"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModelMain = viewModelMain
            setHasOptionsMenu(true)
            viewModelMain!!.getFoodJokes(API_KEY)
            viewModelMain!!.foodJokeResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        foodJokeTextView.text = response.data?.text
                        if (response.data != null) {
                            foodJoke = response.data.text
                        }
                    }

                    is NetworkResult.Error -> {
                        loadDataFromCache()
                        Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                    }

                    is NetworkResult.Loading -> {
                        Log.e(TAG, "FragmentFoodJoke: LOADING")
                    }
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_joke_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.share_food_joke_menu) {
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, foodJoke)
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            viewModelMain.readFoodJoke.observe(viewLifecycleOwner) { database ->
                if (!database.isNullOrEmpty()) {
                    binding.foodJokeTextView.text = database.first().foodJoke.text
                    foodJoke = database.first().foodJoke.text
                }
            }
        }
    }

}