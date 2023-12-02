package com.example.foodapp.ui.ingredients

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentIngredientsBinding
import com.example.foodapp.models.dto.Result
import com.example.foodapp.ui.adapter.IngredientsAdapter
import com.example.foodapp.util.Constants.RECIPE_RESULT_KEY
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class IngredientsFragment : Fragment(R.layout.fragment_ingredients) {
    private lateinit var binding: FragmentIngredientsBinding
    private val mAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)
        setUpRecyclerView()
        myBundle?.extendedIngredients?.let { mAdapter.setData(it) }

    }

    private fun setUpRecyclerView() {
        binding.ingredientsRecyclerView.adapter = mAdapter
        binding.ingredientsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

}