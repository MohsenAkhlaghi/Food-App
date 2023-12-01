package com.example.foodapp.ui.overview

import android.content.Context
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
            val myBundle: Result? = args?.getParcelable("recipeBundle")
            mainImageView.load(myBundle?.image)
            titleTextView.text = myBundle?.title
            likesTextView.text = myBundle?.aggregateLikes.toString()
            timeTextView.text = myBundle?.readyInMinutes.toString()
            myBundle?.summary.let {
                val summary = Jsoup.parse(it).text()
                summaryTextView.text = summary
            }

            if (myBundle?.vegetarian == true) {
                setGreenColor(vegetarianImageView, vegetarianTextView, requireContext())
            }

            if (myBundle?.vegan == true) {
                setGreenColor(veganImageView, veganTextView, requireContext())
            }

            if (myBundle?.glutenFree == true) {
                setGreenColor(glutenFreeImageView, glutenFreeTextView, requireContext())
            }

            if (myBundle?.dairyFree == true) {
                setGreenColor(dairyFreeImageView, dairyFreeTextView, requireContext())
            }

            if (myBundle?.veryHealthy == true) {
                setGreenColor(healthyImageView, healthyTextView, requireContext())
            }

            if (myBundle?.cheap == true) {
                setGreenColor(cheapImageView, cheapTextView, requireContext())
            }

        }

    }
}

fun setGreenColor(imageView: ImageView, textView: TextView, context: Context) {
    imageView.setColorFilter(ContextCompat.getColor(context, R.color.green))
    textView.setTextColor(ContextCompat.getColor(context, R.color.green))
}