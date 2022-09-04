package eu.davidknotek.brecipe.data.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeAndCategory(
    @Embedded
    val recipe: Recipe,
    @Relation(
        parentColumn = "id_category",
        entityColumn = "id"
    )
    val category: Category
) : Parcelable
