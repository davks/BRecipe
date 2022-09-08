package eu.davidknotek.brecipe.fragments.edit

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
import eu.davidknotek.brecipe.data.models.RecipeAndCategory
import eu.davidknotek.brecipe.databinding.FragmentEditRecipeBinding
import eu.davidknotek.brecipe.fragments.detail.DetailRecipeFragment
import eu.davidknotek.brecipe.viewmodels.RecipeViewModel

class EditRecipeFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentEditRecipeBinding
    private val recipeViewModel: RecipeViewModel by viewModels()
    private var recipeAndCategory: RecipeAndCategory? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditRecipeBinding.inflate(layoutInflater, container, false)

        recipeAndCategory = requireArguments().getParcelable(DetailRecipeFragment.RECIPE_AND_CATEGORY)

        // Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.recipe_edit_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.delete -> {
                deleteRecipe()
                true
            }
            R.id.save -> {
                saveRecipe()
                val bundle = bundleOf(DetailRecipeFragment.RECIPE_AND_CATEGORY to recipeAndCategory)
                findNavController().navigate(R.id.action_editRecipeFragment_to_detailRecipeFragment, bundle)
                true
            }
            else -> false
        }
    }

    private fun saveRecipe() {

    }

    private fun deleteRecipe() {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        dialog.setTitle("Delete recipe: ${recipeAndCategory?.recipe?.name}")
        dialog.setMessage("Do you want to delete this recipe: ${recipeAndCategory?.recipe?.name}?")
        dialog.setIcon(android.R.drawable.ic_dialog_alert)
        dialog.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        dialog.setPositiveButton("Yes") { dialogInterface, _ ->
            recipeAndCategory?.let {
                recipeViewModel.deleteRecipe(it.recipe)
                findNavController().navigate(R.id.action_editRecipeFragment_to_listCategoryFragment)
            }
            dialogInterface.dismiss()
        }
        dialog.show()
    }
}