package eu.davidknotek.brecipe.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import eu.davidknotek.brecipe.data.models.Category
import eu.davidknotek.brecipe.data.models.CurrentIngredient
import eu.davidknotek.brecipe.data.models.Recipe

@Database(
    entities = [
        Recipe::class,
        Category::class,
        CurrentIngredient::class
    ],
    version = 3
)
abstract class RecipeDatabase: RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun ingredientsDao(): IngredientsDao

    companion object {
        private var INSTANCE: RecipeDatabase? = null
        fun getInstance(context: Context): RecipeDatabase {
            synchronized(this) {
                return INSTANCE?:Room.databaseBuilder(context,
                    RecipeDatabase::class.java,
                    "recipe-database"
                ).fallbackToDestructiveMigration().build().also {
                    INSTANCE = it
                }
            }
        }
    }
}