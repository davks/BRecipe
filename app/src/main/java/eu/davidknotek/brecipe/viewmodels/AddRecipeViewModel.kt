package eu.davidknotek.brecipe.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.davidknotek.brecipe.data.models.Category
import eu.davidknotek.brecipe.data.models.Recipe

class AddRecipeViewModel(application: Application): AndroidViewModel(application) {
    private val _category = MutableLiveData<Category?>(null)
    val category: LiveData<Category?> = _category

    val recipe = MutableLiveData(
        Recipe(
            0,
            0,
            "",
            0,
            0,
            0,
            false,
            0,
            "",
            "",
            "",
            "")
    )

    fun setRecipeCategory(category: Category?) {
        if (category != null) {
            this._category.value = category
            recipe.value?.idCategory = category.id
        }
    }

    fun setRecipeNotes(note: String) {
        recipe.value?.note = note
    }

    fun setRecipeIngredients(ingredients: String) {
        recipe.value?.ingredients = ingredients
    }

    fun setRecipeProcedure(procedure: String) {
        recipe.value?.procedure = procedure
    }

    fun setRecipeName(name: String) {
        recipe.value?.name = name
    }

    fun setRecipePreparation(preparation: Int) {
        recipe.value?.preparation = preparation
    }

    fun setRecipeCooking(cooking: Int) {
        recipe.value?.cooking = cooking
    }

    fun setRecipeYield(yield: Int) {
        recipe.value?.yield = yield
    }

    fun setRecipeImageUrl(url: String) {
        recipe.value?.imageUrl = url
    }

    fun setRecipeRating(rating: Int) {
        recipe.value?.rating = rating
    }
}