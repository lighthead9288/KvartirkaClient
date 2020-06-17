package viewmodelsfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import models.Currency
import models.Flat
import viewmodels.FlatDetailsViewModel

class FlatDetailsViewModelFactory(private val flat: Flat, private val currency: Currency): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlatDetailsViewModel::class.java))
            return FlatDetailsViewModel(flat, currency) as T

        throw IllegalAccessException()

    }
}