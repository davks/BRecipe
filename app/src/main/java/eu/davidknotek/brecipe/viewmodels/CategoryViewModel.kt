package eu.davidknotek.brecipe.viewmodels

import android.app.Application
import android.view.MenuItem
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.davidknotek.brecipe.data.RecipeDatabase
import eu.davidknotek.brecipe.data.models.Category
import eu.davidknotek.brecipe.repositories.CategoryRepository
import eu.davidknotek.brecipe.repositories.CategoryRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application): AndroidViewModel(application) {
    private val CategoryRepository: CategoryRepository

    val isEmptyDatabase = MutableLiveData<Boolean>(false)
    val allCategories: LiveData<List<Category>>
        get() = CategoryRepository.allCategories

    init {
        val recipeDao = RecipeDatabase.getInstance(application).recipeDao()
        CategoryRepository = CategoryRepositoryImpl(recipeDao)
    }

    fun checkIfDatabaseIsEmpty(categories: List<Category>) {
        isEmptyDatabase.value = categories.isEmpty()
    }

    fun insertCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            CategoryRepository.insertCategory(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            CategoryRepository.deleteCategory(category)
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            CategoryRepository.updateCategory(category)
        }
    }
}