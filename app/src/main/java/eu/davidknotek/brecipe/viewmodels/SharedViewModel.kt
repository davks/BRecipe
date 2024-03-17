package eu.davidknotek.brecipe.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import eu.davidknotek.brecipe.R
import eu.davidknotek.brecipe.data.models.Category
import eu.davidknotek.brecipe.data.models.Recipe

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application
    val isEmptyDatabase = MutableLiveData(false)
    val isEmptyCategory = MutableLiveData(false) // categories in ChooseCategoryDialogFragment

    val recipeIngredients = MutableLiveData("")
    val recipeProcedure = MutableLiveData("")
    val recipeNote = MutableLiveData("")

    val selectedCategory = MutableLiveData<Category>() // we use it in EditRecipeFragment
    val deleteCategory = MutableLiveData<Category>() // we use it in ListCategoryFragment

    val refreshCategory = MutableLiveData(false)
    val isSelectedCategory = MutableLiveData(false) // we need to close chooseCategoryDialogFragment

    val category = MutableLiveData<Category?>(null)
    val recipe = MutableLiveData(Recipe(0, 0, "", 0, 0, 0, false, 0, "", "", "", ""))

    fun <T> checkIfDatabaseIsEmpty(list: List<T>) {
        isEmptyDatabase.value = list.isEmpty()
    }

    fun <T> checkIfCategoriesIsEmpty(list: List<T>) {
        isEmptyCategory.value = list.isEmpty()
    }

    fun setRecipeCategory(category: Category?) {
        if (category != null) {
            this.category.value = category
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

    fun getIngredientsMessage(ingredients: String): String {
        return ingredients.ifEmpty {
            app.resources.getString(R.string.ingredients_empty)
        }
    }

    fun getProcedureMessage(procedure: String): String {
        return procedure.ifEmpty {
            app.resources.getString(R.string.procedure_is_empty)
        }
    }

    fun geNoteMessage(note: String): String {
        return note.ifEmpty {
            app.resources.getString(R.string.empty_notes)
        }
    }
}