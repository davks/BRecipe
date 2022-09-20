package eu.davidknotek.brecipe.repositories

import androidx.lifecycle.LiveData
import eu.davidknotek.brecipe.data.models.Category

interface CategoryRepository {
    val allCategories: LiveData<List<Category>>
    fun getAllCategories(exceptId: Int): LiveData<List<Category>>
    suspend fun insertCategory(category: Category)
    suspend fun deleteCategory(category: Category)
    suspend fun updateCategory(category: Category)
}