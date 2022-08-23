package eu.davidknotek.brecipe.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithRecipes(
    @Embedded
    val category: Category,

    @Relation(
        parentColumn = "id",
        entityColumn = "id_category"
    )
    val recipes: List<Recipe>
)
