package eu.davidknotek.brecipe.fragments.recipe.detail

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.RecipeAndCategory
import eu.davidknotek.brecipe.databinding.FragmentDetailRecipeBinding
import eu.davidknotek.brecipe.fragments.recipe.adapters.DetailRecipeAdapter

class DetailRecipeFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentDetailRecipeBinding
    private var recipeAndCategory: RecipeAndCategory? = null

    companion object {
        const val RECIPE_AND_CATEGORY = "recipe"
        const val INGREDIENTS = "ingredients"
        const val PROCEDURE = "procedure"
        const val SHOW_RECIPES_BY = "show_recipes_by"
        const val CATEGORY = "category"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailRecipeBinding.inflate(layoutInflater, container, false)

        // Menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        setViewPager()
        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.recipe_detail_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.editRecipe -> {
                val bundle = bundleOf(RECIPE_AND_CATEGORY to recipeAndCategory)
                findNavController().navigate(R.id.action_detailRecipeFragment_to_editRecipeFragment, bundle)
                true
            }
            else -> false
        }
    }

    private fun setViewPager() {
        recipeAndCategory = requireArguments().getParcelable(RECIPE_AND_CATEGORY)
        recipeAndCategory?.let {
            val adapter = DetailRecipeAdapter(requireActivity(), it.recipe)
            binding.viewPager.adapter = adapter
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = adapter.tabs[position]
            }.attach()
        }
    }
}