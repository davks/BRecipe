package eu.davidknotek.brecipe.fragments.recipe.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import eu.davidknotek.brecipe.databinding.FragmentViewpagerIngredientsBinding
import eu.davidknotek.brecipe.fragments.recipe.adapters.IngredientsAdapter
import eu.davidknotek.brecipe.viewmodels.IngredientsViewModel

class ViewpagerIngredientsFragment : Fragment() {
    private lateinit var binding: FragmentViewpagerIngredientsBinding
    private val ingredientsViewModel: IngredientsViewModel by activityViewModels()
    private val ingredientsAdapter: IngredientsAdapter by lazy { IngredientsAdapter(ingredientsViewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewpagerIngredientsBinding.inflate(layoutInflater, container, false)

        // Adapter
        binding.ingredientsRecyclerView.adapter = ingredientsAdapter
        binding.ingredientsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        ingredientsViewModel.allIngredients.observe(viewLifecycleOwner) {
            ingredientsAdapter.setIngredients(it)
        }

        changeIngredients()
        return binding.root
    }

    private fun changeIngredients() {
        val ingredients = arguments?.getString(DetailRecipeFragment.INGREDIENTS)
        ingredientsViewModel.isNewRecipe.observe(viewLifecycleOwner) {
            if (!it) {
                ingredientsViewModel.restartIngredients(ingredientsViewModel.ingredientsToList(ingredients))
            }
        }
    }
}