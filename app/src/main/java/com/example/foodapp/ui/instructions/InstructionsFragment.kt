package com.example.foodapp.ui.instructions

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentInstructionsBinding
import com.example.foodapp.models.dto.Result
import com.example.foodapp.util.Constants.RECIPE_RESULT_KEY

class InstructionsFragment : Fragment(R.layout.fragment_instructions) {
    private lateinit var binding: FragmentInstructionsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!
        with(binding) {
            val args = arguments
            val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)
            instructionsWebView.webViewClient = object : WebViewClient() {}
            val webSiteUrl: String = myBundle!!.sourceUrl
            instructionsWebView.loadUrl(webSiteUrl)

        }

    }
}