package eu.davidknotek.brecipe.util

import android.app.Dialog
import androidx.constraintlayout.widget.ConstraintLayout
import eu.davidknotek.brecipe.data.models.Recipe

fun verifyRecipe(recipe: Recipe): Boolean =
    (recipe.name.isNotEmpty()
            && recipe.ingredients.isNotEmpty()
            && recipe.procedure.isNotEmpty()
            && recipe.preparation > 0
            && recipe.yield > 0
            && recipe.idCategory > 0)

fun strToInt(str: String): Int =
    try {
        str.toInt()
    } catch (e: Exception) {
        0
    }

/**
 * Dialog dimension
 */
fun setDialogDimension(dialog: Dialog?) {
    val width = ConstraintLayout.LayoutParams.MATCH_PARENT
    val height = ConstraintLayout.LayoutParams.WRAP_CONTENT
    dialog?.window?.setLayout(width, height)
}

/**
 * Extends TabLayout and show tabs
 */
//fun TabLayout.setupWithViewPager(viewPager: ViewPager2, labels: List<String>) {
//
//    if (labels.size != viewPager.adapter?.itemCount)
//        throw Exception("The size of list and the tab count should be equal!")
//
//    TabLayoutMediator(this, viewPager,
//        TabLayoutMediator.TabConfigurationStrategy { tab, position ->
//            tab.text = labels[position]
//        }).attach()
//}

/**
 * Item in ingredients or procedure is BOLD.
 * Starts and ends with '*'
 */
fun isBold(item: String): Boolean {
    return item.startsWith("*") && item.endsWith("*")
}