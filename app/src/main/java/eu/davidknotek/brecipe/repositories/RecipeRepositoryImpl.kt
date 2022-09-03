package eu.davidknotek.brecipe.repositories

import androidx.lifecycle.LiveData
import eu.davidknotek.brecipe.data.RecipeDao
import eu.davidknotek.brecipe.data.models.Recipe

class RecipeRepositoryImpl(private val recipeDao: RecipeDao): RecipeRepository {

    override fun getRecipes(idCategory: Int): LiveData<List<Recipe>> {
        return recipeDao.getRecipes(idCategory)
    }

    override fun getFavoriteRecipes(): LiveData<List<Recipe>> {
        return recipeDao.getFavoriteRecipes()
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