package eu.davidknotek.brecipe.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import eu.davidknotek.brecipe.data.RecipeDatabase
import eu.davidknotek.brecipe.data.models.CategoryWithRecipes
import eu.davidknotek.brecipe.data.models.Recipe
import eu.davidknotek.brecipe.repositories.RecipeRepository
import eu.davidknotek.brecipe.repositories.RecipeRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application): AndroidViewModel(application) {
    private val recipeRepository: RecipeRepository

    val allRecipes: LiveData<CategoryWithRecipes>
        get() = recipeRepository.allRecipes

    init {
        val recipeDao = RecipeDatabase.getInstance(application).recipeDao()
        recipeRepository = RecipeRepositoryImpl(recipeDao)
    }

    fun insertRecipe(recipe: Recipe) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.insertRecipe(recipe)
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.deleteRecipe(recipe)
        }
    }

    fun updateRecipe(recipe: Recipe) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.updateRecipe(recipe)
        }
    }

    fun getRecipes(idCategory: Int): LiveData<CategoryWithRecipes> =
        recipeRepository.getRecipes(idCategory)
}