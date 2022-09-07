package eu.davidknotek.brecipe.repositories

import androidx.lifecycle.LiveData
import eu.davidknotek.brecipe.data.models.CurrentIngredient

interface IngredientRepository {
    val allIngredients: LiveData<List<CurrentIngredient>>
    suspend fun addIngredients(ingredients: List<CurrentIngredient>)
    suspend fun deleteAllIngredients()
    suspend fun updateIngredient(ingredient: CurrentIngredient)

}