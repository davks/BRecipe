package eu.davidknotek.brecipe.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ingredient(
    val id: Int,
    val item: String,
    var done: Boolean
) : Parcelable
