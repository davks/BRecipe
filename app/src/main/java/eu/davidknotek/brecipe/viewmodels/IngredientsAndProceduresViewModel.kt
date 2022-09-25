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

    /**
     * If a new recipe is not displayed, the old list of procedures and ingredients is displayed.
     */
    fun isNewRecipe(recipe: Recipe) {
        if (this.recipe == recipe) {
            isNewRecipe.value = true
        } else {
            isNewRecipe.value = false
            this.recipe = recipe
            setNewIngredientsAndProcedure(recipe)
        }
    }

    fun setNewIngredientsAndProcedure(recipe: Recipe) {
        allIngredients.value = ingredientsToList(recipe.ingredients)
        allProcedures.value = proceduresToList(recipe.procedure)
    }

    /**
     * Create a new list of ingredients. We need it in case the ingredients changes to finished.
     */
    fun updateIngredient(updatedIngredient: Ingredient) {
        val newIngredients = mutableListOf<Ingredient>()
        for (ingredient in allIngredients.value!!) {
            if (ingredient == updatedIngredient) {
                ingredient.done = updatedIngredient.done
            }
            newIngredients.add(ingredient)
        }

        allIngredients.value = newIngredients
    }

    /**
     * Create a new list of procedure. We need it in case the procedure changes to finished.
     */
    fun updateProcedure(updatedProcedure: Procedure) {
        val newProcedures = mutableListOf<Procedure>()
        for (procedure in allProcedures.value!!) {
            if (procedure == updatedProcedure) {
                procedure.done = updatedProcedure.done
            }
            newProcedures.add(procedure)
        }

        allProcedures.value = newProcedures
    }

    private fun ingredientsToList(text: String): List<Ingredient> {
        val ingredients = mutableListOf<Ingredient>()
        val ingredientsList = text.split("\n")

        for ((id, ingredient) in ingredientsList.withIndex()) {
            if (ingredient == "") {
                continue
            }
            ingredients.add(Ingredient(id, ingredient, false))
        }

        return ingredients
    }

    private fun proceduresToList(text: String): List<Procedure> {
        val procedures = mutableListOf<Procedure>()
        val proceduresList = text.split("\n")

        for ((id, procedure) in proceduresList.withIndex()) {
            if (procedure == "") {
                continue
            }
            procedures.add(Procedure(id, procedure, false))
        }

        return procedures
    }
}