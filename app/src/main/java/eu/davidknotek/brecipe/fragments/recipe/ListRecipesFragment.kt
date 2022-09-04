package eu.davidknotek.brecipe.fragments.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.Category
import eu.davidknotek.brecipe.databinding.FragmentListRecipesBinding
import eu.davidknotek.brecipe.fragments.recipe.adapters.ListRecipeAdapter
import eu.davidknotek.brecipe.viewmodels.RecipeViewModel
import eu.davidknotek.brecipe.viewmodels.SharedViewModel

class ListRecipesFragment : Fragment() {
    private lateinit var binding: FragmentListRecipesBinding
    private val recipeViewModel: RecipeViewModel by viewModels()
    private val listRecipeAdapter: ListRecipeAdapter by lazy { ListRecipeAdapter(recipeViewModel, UsedRecipesBy.CATEGORY) }
    private var usedRecipesBy: UsedRecipesBy = UsedRecipesBy.CATEGORY
    private var currentCategory: Category? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListRecipesBinding.inflate(layoutInflater, container, false)

        // RecyclerView
        binding.recipesRecyclerView.adapter = listRecipeAdapter
        binding.recipesRecyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)

        usedRecipesBy = arguments?.get(SharedViewModel.SHOW_RECIPES_BY) as UsedRecipesBy

        if (usedRecipesBy == UsedRecipesBy.CATEGORY) {
            showByCategory()
        }
        if (usedRecipesBy == UsedRecipesBy.FAVORITES) {
             showByFavorites()
        }

        return binding.root
    }

    private fun showByFavorites() {
        binding.addRecipeActionButton.visibility = View.GONE // don't want to add new recipe
        (activity as androidx.appcompat.app.AppCompatActivity).supportActionBar?.title = getString(R.string.favorites)

        searchFavoriteRecipes()
    }

    private fun showByCategory() {
        currentCategory = arguments?.getParcelable(SharedViewModel.CATEGORY)
        (activity as androidx.appcompat.app.AppCompatActivity).supportActionBar?.title = "${currentCategory?.name}"

        setListeners()
        searchRecipes(currentCategory?.id!!)
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
            list?.let {
                listRecipeAdapter.addRecipes(it)
            }
        }
    }

    private fun searchFavoriteRecipes() {
        recipeViewModel.getFavoriteRecipes().observe(viewLifecycleOwner) { recipes ->
            listRecipeAdapter.addRecipes(recipes)
        }
    }

}