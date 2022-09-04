package eu.davidknotek.brecipe.fragments.recipe.detail

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.Recipe
import eu.davidknotek.brecipe.databinding.FragmentDetailRecipeBinding
import eu.davidknotek.brecipe.fragments.recipe.adapters.DetailRecipeAdapter
import eu.davidknotek.brecipe.viewmodels.RecipeViewModel
import eu.davidknotek.brecipe.viewmodels.SharedViewModel

class DetailRecipeFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentDetailRecipeBinding
    private var recipe: Recipe? = null

    companion object {
        const val RECIPE = "recipe"
        const val INGREDIENTS = "ingredients"
        const val PROCEDURE = "procedure"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailRecipeBinding.inflate(layoutInflater, container, false)

        // Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // ViewPager
        recipe = requireArguments().getParcelable(RECIPE)
        recipe?.let {
            val adapter = DetailRecipeAdapter(requireActivity(), it)
            binding.viewPager.adapter = adapter
        }

        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.recipe_detail_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.editRecipe -> {
                editRecipe()
                true
            }
            else -> false
        }
    }

    private fun editRecipe() {
        val bundle = bundleOf(RECIPE to recipe)
        findNavController().navigate(R.id.action_detailRecipeFragment_to_editRecipeFragment, bundle)
    }
}