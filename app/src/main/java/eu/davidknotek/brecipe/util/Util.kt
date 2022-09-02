package eu.davidknotek.brecipe.util

import eu.davidknotek.brecipe.data.models.Recipe

fun verifyRecipe(recipe: Recipe): Boolean =
    (recipe.name.isNotEmpty()
            && recipe.ingredients.isNotEmpty()
            && recipe.procedure.isNotEmpty()
            && recipe.cooking > 0
            && recipe.preparation > 0
            && recipe.yield > 0
            && recipe.idCategory > 0)

fun strToInt(str: String): Int =
    try {
        str.toInt()
    } catch (e: Exception) {
        0
    }
