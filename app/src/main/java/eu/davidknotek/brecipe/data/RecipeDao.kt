package eu.davidknotek.brecipe.data

import androidx.lifecycle.LiveData
import androidx.room.*
import eu.davidknotek.brecipe.data.models.Category
import eu.davidknotek.brecipe.data.models.Recipe
import eu.davidknotek.brecipe.data.models.RecipeAndCategory

@Dao
interface RecipeDao {
    /* Reciepes */
    @Insert
    suspend fun insertRecipe(recipe: Recipe)

    @Update
    suspend fun updateRecipe(recipe: Recipe)

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)

    @Transaction
    @Query("SELECT * FROM recipes ORDER BY name ASC")
    fun getRecipes(): LiveData<List<RecipeAndCategory>>

    @Transaction
    @Query("SELECT * FROM recipes WHERE id_category = :idCategory")
    fun getRecipes(idCategory: Int): LiveData<List<RecipeAndCategory>>

    @Transaction
    @Query("SELECT * FROM recipes WHERE heart = 1")
    fun getFavoriteRecipes(): LiveData<List<RecipeAndCategory>>

    @Transaction
    @Query("SELECT * FROM recipes WHERE name LIKE :search")
    fun searchRecipes(search: String): LiveData<List<RecipeAndCategory>>

    /* Categories */
    @Insert
    suspend fun insertCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): LiveData<List<Category>>
}