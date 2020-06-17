package models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

data class Flat(var id: Long, var building_type: String, var rooms: Int, var address: String, var coordinates: LocationData, var description: String, var photo_default: Photo,
    var photos: List<Photo>, var contacts: Contacts, var prices: Price): Serializable