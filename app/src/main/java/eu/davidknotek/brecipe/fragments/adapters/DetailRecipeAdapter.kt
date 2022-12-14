package eu.davidknotek.brecipe.fragments.adapters

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.Recipe
import eu.davidknotek.brecipe.fragments.detail.DetailRecipeFragment
import eu.davidknotek.brecipe.fragments.detail.ViewpagerDetailFragment
import eu.davidknotek.brecipe.fragments.detail.ViewpagerIngredientsFragment
import eu.davidknotek.brecipe.fragments.detail.ViewpagerProcedureFragment

/**
 * Adapter for viewpager2 that displays the recipe detail.
 */
class DetailRecipeAdapter(
    activity: FragmentActivity,
    val recipe: Recipe
): FragmentStateAdapter(activity) {
    private var fragments: List<Fragment>

    private val detail = ViewpagerDetailFragment()
    private val ingredients = ViewpagerIngredientsFragment()
    private val procedure = ViewpagerProcedureFragment()

    val tabs = listOf(
        activity.resources.getString(R.string.wp_detail),
        activity.resources.getString(R.string.wp_ingredients),
        activity.resources.getString(R.string.wp_procedure)
    )

    init {
        detail.arguments = bundleOf(DetailRecipeFragment.RECIPE_AND_CATEGORY to recipe)
        fragments = listOf(detail, ingredients, procedure)
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}