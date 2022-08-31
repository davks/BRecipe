package eu.davidknotek.brecipe.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import eu.davidknotek.brecipe.data.models.Category

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    val refreshCategory = MutableLiveData(false)
    val isEmptyDatabase = MutableLiveData(false)

    fun checkIfDatabaseIsEmpty(categories: List<Category>) {
        isEmptyDatabase.value = categories.isEmpty()
    }
}