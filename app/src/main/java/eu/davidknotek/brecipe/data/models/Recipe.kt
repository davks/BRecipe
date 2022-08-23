package eu.davidknotek.brecipe.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "id_category")
    val idCategory: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "preparation")
    val preparation: Int,

    @ColumnInfo(name = "cooking")
    val cooking: Int,

    @ColumnInfo(name = "yield")
    val yield: Int,

    @ColumnInfo(name = "heart")
    val heart: Boolean,

    @ColumnInfo(name = "rating")
    val rating: Int,

    @ColumnInfo(name = "note")
    val note: String,

    @ColumnInfo(name = "ingredients")
    val ingredients: String,

    @ColumnInfo(name = "procedure")
    val procedure: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String
)
