package eu.davidknotek.brecipe.fragments.recipe.adapters

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import eu.davidknotek.brecipe.data.models.Recipe
import eu.davidknotek.brecipe.fragments.recipe.detail.DetailRecipeFragment
import eu.davidknotek.brecipe.fragments.recipe.detail.ViewpagerDetailFragment
import eu.davidknotek.brecipe.fragments.recipe.detail.ViewpagerIngredientsFragment
import eu.davidknotek.brecipe.fragments.recipe.detail.ViewpagerProcedureFragment

class DetailRecipeAdapter(
    activity: FragmentActivity,
    val recipe: Recipe
): FragmentStateAdapter(activity) {
    private var fragments: List<Fragment>
    private val detail = ViewpagerDetailFragment()
    private val ingredients = ViewpagerIngredientsFragment()
    private val procedure = ViewpagerProcedureFragment()

    init {
//        Log.d(TAG, "${recipe.name} ${recipe.procedure} ")
        detail.arguments = bundleOf(DetailRecipeFragment.RECIPE_AND_CATEGORY to recipe)
        ingredients.arguments = bundleOf(DetailRecipeFragment.INGREDIENTS to recipe.ingredients)
        procedure.arguments = bundleOf(DetailRecipeFragment.PROCEDURE to recipe.procedure)
        fragments = listOf(detail, ingredients, procedure)
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}