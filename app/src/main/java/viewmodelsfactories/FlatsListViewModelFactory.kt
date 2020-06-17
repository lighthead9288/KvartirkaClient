package viewmodelsfactories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import viewmodels.FlatsListViewModel

class FlatsListViewModelFactory(private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlatsListViewModel::class.java))
            return FlatsListViewModel(application) as T
        throw IllegalAccessException()
    }
}