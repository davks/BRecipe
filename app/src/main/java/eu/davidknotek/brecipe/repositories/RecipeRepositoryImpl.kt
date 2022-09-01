package eu.davidknotek.brecipe.repositories

import androidx.lifecycle.LiveData
import eu.davidknotek.brecipe.data.RecipeDao
import eu.davidknotek.brecipe.data.models.CategoryWithRecipes
import eu.davidknotek.brecipe.data.models.Recipe

class RecipeRepositoryImpl(private val recipeDao: RecipeDao): RecipeRepository {
    override val allRecipes: LiveData<CategoryWithRecipes>
        get() = recipeDao.getRecipes()

    override fun getRecipes(idCategory: Int): LiveData<CategoryWithRecipes> {
        return recipeDao.getRecipes(idCategory)
    }

    override suspend fun insertRecipe(recipe: Recipe) {
        recipeDao.insertRecipe(recipe)
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        recipeDao.updateRecipe(recipe)
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteRecipe(recipe)
    }
}