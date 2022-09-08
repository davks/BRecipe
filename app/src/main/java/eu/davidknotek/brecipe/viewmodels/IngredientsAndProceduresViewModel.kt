package eu.davidknotek.brecipe.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.davidknotek.brecipe.data.models.Ingredient
import eu.davidknotek.brecipe.data.models.Procedure
import eu.davidknotek.brecipe.data.models.Recipe

class IngredientsAndProceduresViewModel: ViewModel() {
    var allIngredients: MutableLiveData<List<Ingredient>> = MutableLiveData(emptyList())
    var allProcedures: MutableLiveData<List<Procedure>> = MutableLiveData(emptyList())

    private var isNewRecipe = MutableLiveData(false)
    private var recipe: Recipe? = null

    fun isNewRecipe(recipe: Recipe) {
        if (this.recipe == recipe) {
            isNewRecipe.value = true
        } else {
            isNewRecipe.value = false
            this.recipe = recipe
            allIngredients.value = ingredientsToList(recipe.ingredients)
            allProcedures.value = proceduresToList(recipe.procedure)
        }
    }

    fun updateIngredient(newIngredient: Ingredient) {
        val newIngredients = mutableListOf<Ingredient>()
        for (ingredient in allIngredients.value!!) {
            if (ingredient == newIngredient) {
                ingredient.done = newIngredient.done
            }
            newIngredients.add(ingredient)
        }

        allIngredients.value = newIngredients
    }

    fun updateProcedure(newProcedure: Procedure) {
        val newProcedures = mutableListOf<Procedure>()
        for (procedure in allProcedures.value!!) {
            if (procedure == newProcedure) {
                procedure.done = newProcedure.done
            }
            newProcedures.add(procedure)
        }

        allProcedures.value = newProcedures
    }

    private fun ingredientsToList(text: String): List<Ingredient> {
        val ingredients = mutableListOf<Ingredient>()
        val ingredientsList = text.split("\n")

        for ((id, ingredient) in ingredientsList.withIndex()) {
            ingredients.add(Ingredient(id, ingredient, false))
        }

        return ingredients
    }

    private fun proceduresToList(text: String): List<Procedure> {
        val procedures = mutableListOf<Procedure>()
        val proceduresList = text.split("\n")

        for ((id, procedure) in proceduresList.withIndex()) {
            procedures.add(Procedure(id, procedure, false))
        }

        return procedures
    }
}