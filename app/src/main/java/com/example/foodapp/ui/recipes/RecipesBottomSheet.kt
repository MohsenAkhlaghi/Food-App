package com.example.foodapp.ui.recipes

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.foodapp.R
import com.example.foodapp.databinding.RecipesBottomSheetBinding
import com.example.foodapp.util.Constants.DEFAULT_DIET_TYPE
import com.example.foodapp.util.Constants.DEFAULT_MEAL_TYPE
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.Locale


class RecipesBottomSheet : BottomSheetDialogFragment(R.layout.recipes_bottom_sheet) {
    private lateinit var binding: RecipesBottomSheetBinding
    private lateinit var viewModelRecipes: RecipesViewModel
    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelRecipes = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!
        binding.lifecycleOwner = viewLifecycleOwner

        viewModelRecipes.readMealAndDietType.asLiveData().observe(viewLifecycleOwner) { value ->
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType
            updateChip(value.selectedMealTypeId, binding.mealTypeChipGroup)
            updateChip(value.selectedDietTypeId, binding.dietTypeChipGroup)
        }

        binding.mealTypeChipGroup.setOnCheckedStateChangeListener { group, selectedChipIds ->
            val chip = group.findViewById<Chip>(selectedChipIds.first())
            val selectedMealType = chip.text.toString().lowercase(Locale.ROOT)
            mealTypeChip = selectedMealType
            mealTypeChipId = selectedChipIds.first()
        }

        binding.dietTypeChipGroup.setOnCheckedStateChangeListener { group, selectedChipIds ->
            val chip = group.findViewById<Chip>(selectedChipIds.first())
            val selectedDietType = chip.text.toString().lowercase(Locale.ROOT)
            dietTypeChip = selectedDietType
            dietTypeChipId = selectedChipIds.first()
        }

        binding.applyBtn.setOnClickListener {
            viewModelRecipes.saveMealAndDietType(mealTypeChip, mealTypeChipId, dietTypeChip, dietTypeChipId)
            val action = RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
            findNavController().navigate(action)
        }

    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.e(TAG, "updateChip: ${e.message.toString()}")
            }
        }
    }
}