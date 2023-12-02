package com.example.foodapp.ui.overview

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import coil.load
import com.example.foodapp.R
import com.example.foodapp.databinding.FragmentOverviewBinding
import com.example.foodapp.models.dto.Result
import com.example.foodapp.util.Constants.RECIPE_RESULT_KEY
import dagger.hilt.android.AndroidEntryPoint
import org.jsoup.Jsoup

@AndroidEntryPoint
class OverviewFragment : Fragment(R.layout.fragment_overview) {
    private lateinit var binding: FragmentOverviewBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!
        binding.apply {
            val args = arguments
            val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)
            mainImageView.load(myBundle?.image)
            titleTextView.text = myBundle?.title
            likesTextView.text = myBundle?.aggregateLikes.toString()
            timeTextView.text = myBundle?.readyInMinutes.toString()
            myBundle?.summary.let {
                val summary = Jsoup.parse(it).text()
                summaryTextView.text = summary
            }

            myBundle?.let {
                if (it.vegetarian) setGreenColor(vegetarianImageView, vegetarianTextView)
                if (it.vegan) setGreenColor(veganImageView, veganTextView)
                if (it.glutenFree) setGreenColor(glutenFreeImageView, glutenFreeTextView)
                if (it.dairyFree) setGreenColor(dairyFreeImageView, dairyFreeTextView)
                if (it.veryHealthy) setGreenColor(healthyImageView, healthyTextView)
                if (it.cheap) setGreenColor(cheapImageView, cheapTextView)
            }

        }

    }

    private fun setGreenColor(imageView: ImageView, textView: TextView) {
        imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
    }

}

