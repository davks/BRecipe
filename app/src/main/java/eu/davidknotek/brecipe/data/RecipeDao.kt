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

    @Query("UPDATE recipes SET id_category = :newIdCategory WHERE id_category = :oldIdCategory")
    suspend fun changeRecipeCategory(oldIdCategory: Int, newIdCategory: Int)

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

    @Query("SELECT *, (SELECT count(*) FROM recipes r WHERE c.id = r.id_category) AS numberOfRecipes FROM categories c ORDER BY name ASC")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM categories WHERE id != :exceptCategoryId")
    fun getAllCategories(exceptCategoryId: Int): LiveData<List<Category>>
}