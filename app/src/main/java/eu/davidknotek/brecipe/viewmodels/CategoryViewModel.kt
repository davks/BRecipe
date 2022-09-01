package eu.davidknotek.brecipe.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import eu.davidknotek.brecipe.data.RecipeDatabase
import eu.davidknotek.brecipe.data.models.Category
import eu.davidknotek.brecipe.repositories.CategoryRepository
import eu.davidknotek.brecipe.repositories.CategoryRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application): AndroidViewModel(application) {
    private val categoryRepository: CategoryRepository

    val allCategories: LiveData<List<Category>>
        get() = categoryRepository.allCategories

    init {
        val recipeDao = RecipeDatabase.getInstance(application).recipeDao()
        categoryRepository = CategoryRepositoryImpl(recipeDao)
    }

    fun insertCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.insertCategory(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.deleteCategory(category)
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.updateCategory(category)
        }
    }
}