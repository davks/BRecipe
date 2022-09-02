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
    var idCategory: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "preparation")
    var preparation: Int,

    @ColumnInfo(name = "cooking")
    var cooking: Int,

    @ColumnInfo(name = "yield")
    var yield: Int,

    @ColumnInfo(name = "heart")
    var heart: Boolean,

    @ColumnInfo(name = "rating")
    var rating: Int,

    @ColumnInfo(name = "note")
    var note: String,

    @ColumnInfo(name = "ingredients")
    var ingredients: String,

    @ColumnInfo(name = "procedure")
    var procedure: String,

    @ColumnInfo(name = "image_url")
    var imageUrl: String
)
