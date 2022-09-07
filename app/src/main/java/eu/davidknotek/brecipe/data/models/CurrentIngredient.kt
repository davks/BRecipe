package eu.davidknotek.brecipe.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "current_ingredients")
@Parcelize
data class CurrentIngredient(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val item: String,
    var done: Boolean
) : Parcelable
