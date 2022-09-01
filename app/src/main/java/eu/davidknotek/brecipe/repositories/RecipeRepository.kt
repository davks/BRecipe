package eu.davidknotek.brecipe.repositories

import androidx.lifecycle.LiveData
import eu.davidknotek.brecipe.data.models.CategoryWithRecipes
import eu.davidknotek.brecipe.data.models.Recipe

interface RecipeRepository {
    val allRecipes: LiveData<CategoryWithRecipes>
    fun getRecipes(idCategory: Int): LiveData<CategoryWithRecipes>
    suspend fun insertRecipe(recipe: Recipe)
    suspend fun updateRecipe(recipe: Recipe)
    suspend fun deleteRecipe(recipe: Recipe)
}