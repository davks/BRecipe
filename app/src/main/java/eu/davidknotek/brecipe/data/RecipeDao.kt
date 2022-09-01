package eu.davidknotek.brecipe.data

import androidx.lifecycle.LiveData
import androidx.room.*
import eu.davidknotek.brecipe.data.models.Category
import eu.davidknotek.brecipe.data.models.CategoryWithRecipes
import eu.davidknotek.brecipe.data.models.Recipe

@Dao
interface RecipeDao {
    /* Reciepes */
    @Insert
    suspend fun insertRecipe(recipe: Recipe)

    @Update
    suspend fun updateRecipe(recipe: Recipe)

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    @Query("SELECT * FROM recipe ORDER BY name ASC")
    fun getRecipes(): LiveData<CategoryWithRecipes>

    @Transaction
    @Query("SELECT * FROM recipe WHERE id_category = :idCategory")
    fun getRecipes(idCategory: Int): LiveData<CategoryWithRecipes>

    /* Categories */
    @Insert
    suspend fun insertCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM category ORDER BY name ASC")
    fun getAllCategories(): LiveData<List<Category>>
}