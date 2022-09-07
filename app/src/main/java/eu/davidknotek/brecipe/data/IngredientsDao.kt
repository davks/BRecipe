package eu.davidknotek.brecipe.data

import androidx.lifecycle.LiveData
import androidx.room.*
import eu.davidknotek.brecipe.data.models.CurrentIngredient

@Dao
interface IngredientsDao {
    @Insert
    suspend fun addIngredients(ingredients: List<CurrentIngredient>)

    @Query("SELECT * FROM current_ingredients")
    fun getAllIngredients(): LiveData<List<CurrentIngredient>>

    @Query("DELETE FROM current_ingredients")
    suspend fun deleteAllIngredients()

    @Update
    suspend fun updateIngredient(ingredient: CurrentIngredient)
}