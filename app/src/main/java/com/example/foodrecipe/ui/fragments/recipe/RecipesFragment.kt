package com.example.foodrecipe.ui.fragments.recipe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrecipe.R
import com.example.foodrecipe.adapter.RecipesAdapter
import com.example.foodrecipe.databinding.FragmentRecipesBinding
import com.example.foodrecipe.ui.viewmodel.MainViewModel
import com.example.foodrecipe.ui.viewmodel.RecipesViewModel
import com.example.foodrecipe.util.NetworkListener
import com.example.foodrecipe.util.NetworkResult
import com.example.foodrecipe.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private val args by navArgs<RecipesFragmentArgs>()
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var networkListener: NetworkListener
    private val mAdapter by lazy { RecipesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel


        initRecyclerView()
        recipesViewModel.readBackOnline.observe(viewLifecycleOwner,{
            recipesViewModel.backOnline = it
        })

        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("Network Listener",status.toString())
                    recipesViewModel.networkStatus = status
                    recipesViewModel.showNetworkStatus()
                    readDatabase()
                }
        }

        binding.recipesFab.setOnClickListener {
            if (recipesViewModel.networkStatus){
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            }else{
                recipesViewModel.showNetworkStatus()
            }
        }

        return binding.root
    }

    private fun readDatabase() {
       lifecycleScope.launch {
           mainViewModel.readRecipes.observeOnce(viewLifecycleOwner,{database ->
               if (database.isNotEmpty() && !args.backFromBottomSheet){
                   Log.d("RecipeFragment","Request Database!!!")
                   mAdapter.setData(database[0].foodRecipe)
                   hideShimmerEffect()
               }else{
                   requestApiData()
               }
           })
       }
    }

    private fun requestApiData() {
     Log.d("RecipeFragment","Request Api Data!!")
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, { response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading ->{
                    showShimmerEffect()
                }
            }
        })
    }

    private fun initRecyclerView() {
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun loadDataFromCache(){
       lifecycleScope.launch {  mainViewModel.readRecipes.observe(viewLifecycleOwner,{ database ->
           if (database.isNotEmpty()){
               mAdapter.setData(database[0].foodRecipe)
           }
       }) }
    }

    private fun showShimmerEffect() {
        binding.shimmerFrameLayout.startShimmer()
        binding.recyclerview.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.shimmerFrameLayout.hideShimmer()
        binding.recyclerview.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}