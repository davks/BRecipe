package eu.davidknotek.brecipe.data.models

data class Procedure(
    val id: Int,
    val item: String,
    var done: Boolean
)
