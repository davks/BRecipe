package eu.davidknotek.brecipe.fragments.recipe

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.Category
import eu.davidknotek.brecipe.databinding.FragmentListRecipesBinding
import eu.davidknotek.brecipe.fragments.recipe.adapters.ListRecipeAdapter
import eu.davidknotek.brecipe.viewmodels.RecipeViewModel
import eu.davidknotek.brecipe.viewmodels.SharedViewModel

class ListRecipesFragment : Fragment() {
    private lateinit var binding: FragmentListRecipesBinding
    private val recipeViewModel: RecipeViewModel by viewModels()
    private val listRecipeAdapter: ListRecipeAdapter by lazy { ListRecipeAdapter() }
    private var currentCategory: Category? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListRecipesBinding.inflate(layoutInflater, container, false)
        currentCategory = arguments?.getParcelable(SharedViewModel.CATEGORY)

        // RecyclerView
        binding.recipesRecyclerView.adapter = listRecipeAdapter
        binding.recipesRecyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
//        binding.recipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as androidx.appcompat.app.AppCompatActivity).supportActionBar?.title = "${currentCategory?.name}"

        setListeners()
        searchRecipes(currentCategory?.id!!)

        val aaa = recipeViewModel.allRecipes.value
        //Log.d(TAG, "onViewCreated: ${aaa?.recipes?.size}")
    }


    private fun setListeners() {
        // Add new recipe
        binding.addRecipeActionButton.setOnClickListener {
            val bundle = bundleOf(SharedViewModel.CATEGORY to currentCategory)
            findNavController().navigate(R.id.action_listRecipesFragment_to_addRecipeFragment, bundle)
        }
    }

    private fun searchRecipes(idCategory: Int) {
        recipeViewModel.getRecipes(idCategory).observe(viewLifecycleOwner) { list ->
            Log.d(TAG, "setObservers: ")
            list?.let {
                listRecipeAdapter.addRecipes(it)
            }

        }
        //recipeViewModel.allRecipes.observe(viewLifecycleOwner) { list ->
            //listRecipeAdapter.addRecipes()
            //Log.d(TAG, "searchRecipes: ${list.recipes.size}")
        //}
    }


}