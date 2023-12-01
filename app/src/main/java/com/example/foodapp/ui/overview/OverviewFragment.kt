package com.example.foodapp.ui.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentOverviewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OverviewFragment : Fragment(R.layout.fragment_overview) {
    private lateinit var binding: FragmentOverviewBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

    }
}