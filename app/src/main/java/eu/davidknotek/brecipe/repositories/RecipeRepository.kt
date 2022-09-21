package eu.davidknotek.brecipe.repositories

import androidx.lifecycle.LiveData
import eu.davidknotek.brecipe.data.models.Recipe
import eu.davidknotek.brecipe.data.models.RecipeAndCategory

interface RecipeRepository {
    val allRecipes: LiveData<List<RecipeAndCategory>>
    fun getRecipes(idCategory: Int): LiveData<List<RecipeAndCategory>>
    fun getFavoriteRecipes(): LiveData<List<RecipeAndCategory>>
    fun searchRecipes(query: String): LiveData<List<RecipeAndCategory>>
    suspend fun changeRecipeCategory(oldIdCategory: Int, newIdCategory: Int)
    suspend fun insertRecipe(recipe: Recipe)
    suspend fun updateRecipe(recipe: Recipe)
    suspend fun deleteRecipe(recipe: Recipe)
}