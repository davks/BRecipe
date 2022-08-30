package eu.davidknotek.brecipe.repositories

import androidx.lifecycle.LiveData
import eu.davidknotek.brecipe.data.RecipeDao
import eu.davidknotek.brecipe.data.models.Category

class CategoryRepositoryImpl(private val recipeDao: RecipeDao): CategoryRepository {
    override val allCategories: LiveData<List<Category>>
        get() = recipeDao.getAllCategories()

    override suspend fun insertCategory(category: Category) {
        recipeDao.insertCategory(category)
    }

    override suspend fun deleteCategory(category: Category) {
        recipeDao.deleteCategory(category)
    }

    override suspend fun updateCategory(category: Category) {
        recipeDao.updateCategory(category)
    }
}