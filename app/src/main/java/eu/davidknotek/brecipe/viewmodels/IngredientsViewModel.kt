package eu.davidknotek.brecipe.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import eu.davidknotek.brecipe.data.RecipeDatabase
import eu.davidknotek.brecipe.data.models.CurrentIngredient
import eu.davidknotek.brecipe.data.models.Recipe
import eu.davidknotek.brecipe.repositories.IngredientRepository
import eu.davidknotek.brecipe.repositories.IngredientRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IngredientsViewModel(application: Application): AndroidViewModel(application) {
    private val ingredientRepository: IngredientRepository

    val allIngredients: LiveData<List<CurrentIngredient>>
    var isNewRecipe = MutableLiveData(false)
    private var recipe: Recipe? = null

    init {
        val ingredientsDao = RecipeDatabase.getInstance(application).ingredientsDao()
        ingredientRepository = IngredientRepositoryImpl(ingredientsDao)
        allIngredients = ingredientRepository.allIngredients
    }

    fun addIngredients(ingredients: List<CurrentIngredient>) {
        viewModelScope.launch(Dispatchers.IO) {
            ingredientRepository.addIngredients(ingredients)
        }
    }

    fun deleteAllIngredients() {
        viewModelScope.launch(Dispatchers.IO) {
            ingredientRepository.deleteAllIngredients()
        }
    }

    fun restartIngredients(ingredients: List<CurrentIngredient>) {
        viewModelScope.launch(Dispatchers.IO) {
            ingredientRepository.deleteAllIngredients()
            ingredientRepository.addIngredients(ingredients)
        }
    }

    fun updateIngredient(ingredient: CurrentIngredient) {
        viewModelScope.launch(Dispatchers.IO) {
            ingredientRepository.updateIngredient(ingredient)
        }
    }

//    fun checkLastRecipe(recipe: Recipe): Boolean {
//        return if (this.recipe == null) {
//            recipe.let {
//                this.recipe?.value = it
//            }
//            false
//        } else if (this.recipe?.value != recipe) {
//            recipe.let {
//                this.recipe?.value = it
//            }
//            false
//        } else {
//            true
//        }
//    }

    fun checkLastRecipe(recipe: Recipe) {
        if (this.recipe == recipe) {
            isNewRecipe.value = true
        } else {
            this.recipe = recipe
            isNewRecipe.value = false
        }
    }


    fun ingredientsToList(ingredientsString: String?): List<CurrentIngredient> {
        val ingredients = mutableListOf<CurrentIngredient>()
        val ingredientsList = ingredientsString?.split("\n")

        ingredientsList?.let {
            for (ingredient in ingredientsList) {
                ingredients.add(CurrentIngredient(0, ingredient, false))
            }
        }

        return ingredients
    }
}