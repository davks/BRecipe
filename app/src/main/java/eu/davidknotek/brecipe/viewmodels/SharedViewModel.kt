package eu.davidknotek.brecipe.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import eu.davidknotek.brecipe.R

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application
    val refreshCategory = MutableLiveData(false)
    val isEmptyDatabase = MutableLiveData(false)
    val recipeIngredients = MutableLiveData("")
    val recipeProcedure = MutableLiveData("")
    val recipeNote = MutableLiveData("")

    fun <T> checkIfDatabaseIsEmpty(list: List<T>) {
        isEmptyDatabase.value = list.isEmpty()
    }

    fun getIngredientsMessage(ingredients: String): String? {
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