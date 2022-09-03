package eu.davidknotek.brecipe.repositories

import androidx.lifecycle.LiveData
import eu.davidknotek.brecipe.data.models.Recipe

interface RecipeRepository {
    fun getRecipes(idCategory: Int): LiveData<List<Recipe>>
    fun getFavoriteRecipes(): LiveData<List<Recipe>>
    suspend fun insertRecipe(recipe: Recipe)
    suspend fun updateRecipe(recipe: Recipe)
    suspend fun deleteRecipe(recipe: Recipe)
}