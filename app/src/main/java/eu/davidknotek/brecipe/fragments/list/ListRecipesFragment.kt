package eu.davidknotek.brecipe.fragments.list

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
import eu.davidknotek.brecipe.fragments.UsedRecipesBy
import eu.davidknotek.brecipe.fragments.adapters.ListRecipeAdapter
import eu.davidknotek.brecipe.fragments.detail.DetailRecipeFragment
import eu.davidknotek.brecipe.viewmodels.RecipeViewModel
import eu.davidknotek.brecipe.viewmodels.SharedViewModel

class ListRecipesFragment : Fragment() {
    private lateinit var binding: FragmentListRecipesBinding
    private val recipeViewModel: RecipeViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val listRecipeAdapter: ListRecipeAdapter by lazy { ListRecipeAdapter(recipeViewModel) }
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

        // We use this fragment by Category and Favorites
        usedRecipesBy = arguments?.get(DetailRecipeFragment.SHOW_RECIPES_BY) as UsedRecipesBy
        when (usedRecipesBy) {
            UsedRecipesBy.CATEGORY -> showByCategory()
            UsedRecipesBy.FAVORITES -> showByFavorites()
        }

        setListeners()

        sharedViewModel.isEmptyDatabase.observe(viewLifecycleOwner) {
            if (it) {
                binding.noItemsLinearLayout.visibility = View.VISIBLE
            } else {
                binding.noItemsLinearLayout.visibility = View.GONE
            }
        }

        return binding.root
    }

    private fun showByFavorites() {
        binding.addRecipeActionButton.visibility = View.GONE // don't want to add new recipe
        (activity as androidx.appcompat.app.AppCompatActivity).supportActionBar?.title =
            getString(R.string.favorites)

        recipeViewModel.getFavoriteRecipes().observe(viewLifecycleOwner) { recipes ->
            sharedViewModel.checkIfDatabaseIsEmpty(recipes)
            listRecipeAdapter.addRecipes(recipes)
        }
    }

    private fun showByCategory() {
        currentCategory = arguments?.getParcelable(DetailRecipeFragment.CATEGORY)
        (activity as androidx.appcompat.app.AppCompatActivity).supportActionBar?.title =
            "${currentCategory?.name}"

        recipeViewModel.getRecipes(currentCategory?.id!!).observe(viewLifecycleOwner) { list ->
            list?.let {
                sharedViewModel.checkIfDatabaseIsEmpty(it)
                listRecipeAdapter.addRecipes(it)
            }
        }
    }

    private fun setListeners() {
        // Add new recipe
        binding.addRecipeActionButton.setOnClickListener {
            val bundle = bundleOf(DetailRecipeFragment.CATEGORY to currentCategory)
            findNavController().navigate(R.id.action_listRecipesFragment_to_addRecipeFragment, bundle)
        }
    }

}