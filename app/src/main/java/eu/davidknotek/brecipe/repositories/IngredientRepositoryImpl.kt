package eu.davidknotek.brecipe.repositories

import androidx.lifecycle.LiveData
import eu.davidknotek.brecipe.data.IngredientsDao
import eu.davidknotek.brecipe.data.models.CurrentIngredient

class IngredientRepositoryImpl(private val ingredientsDao: IngredientsDao): IngredientRepository {
    override val allIngredients: LiveData<List<CurrentIngredient>>
        get() = ingredientsDao.getAllIngredients()

    override suspend fun addIngredients(ingredients: List<CurrentIngredient>) {
        ingredientsDao.addIngredients(ingredients)
    }

    override suspend fun deleteAllIngredients() {
        ingredientsDao.deleteAllIngredients()
    }

    override suspend fun updateIngredient(ingredient: CurrentIngredient) {
        ingredientsDao.updateIngredient(ingredient)
    }
}