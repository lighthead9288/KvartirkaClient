package viewmodels

import androidx.lifecycle.ViewModel
import models.Currency
import models.Flat

class FlatDetailsViewModel(private val flat: Flat, private val currency: Currency): ViewModel() {

    var address: String
    var rooms: String
    var buildingType: String
    var description: String
    var price: String
    var contacts: String

    init {
        val stringPrice = flat.prices.day.toString()
        val currencyLabel = currency.label
        address = flat.address
        val roomsCount = flat.rooms.toString()
        rooms = "Комнат: $roomsCount"
        buildingType = flat.building_type
        description = flat.description
        price = "$stringPrice $currencyLabel"
        val owner = flat.contacts.name
        val phone = flat.contacts.phones[0].phone
        contacts = "$owner, $phone"
    }


}