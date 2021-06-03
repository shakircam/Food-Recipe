package com.example.foodrecipe.ui.fragments.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import com.example.foodrecipe.R
import com.example.foodrecipe.adapter.RecipesItemBinding
import com.example.foodrecipe.model.Result
import com.example.foodrecipe.databinding.FragmentOverviewBinding
import com.example.foodrecipe.util.Constants.Companion.RECIPE_RESULT_KEY

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result = args!!.getParcelable<Result>(RECIPE_RESULT_KEY) as Result
        binding.mainImageView.load(myBundle.image)
        binding.titleTextView.text = myBundle.title
        binding.likesTextView.text = myBundle.aggregateLikes.toString()
        binding.timeTextView.text = myBundle.readyInMinutes.toString()
        RecipesItemBinding.parseHtml(binding.summaryTextView, myBundle.summary)

        updateColor(myBundle.vegetarian,binding.vegetarianTextView,binding.vegetarianImageView)
        updateColor(myBundle.vegan,binding.veganTextView,binding.veganImageView)
        updateColor(myBundle.glutenFree,binding.glutenFreeTextView,binding.glutenFreeImageView)
        updateColor(myBundle.cheap,binding.cheapTextView,binding.cheapImageView)
        updateColor(myBundle.dairyFree,binding.dairyFreeTextView,binding.dairyFreeImageView)
        updateColor(myBundle.veryHealthy,binding.healthyTextView,binding.healthyImageView)

        return binding.root
    }

    private fun updateColor(stateIsOn: Boolean, textView: TextView, imageView: ImageView){
        if (stateIsOn){
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            imageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}