package services

import android.app.Application
import android.location.Geocoder
import java.lang.Exception
import java.util.*

class DetectCityService(private val application: Application, private val ltd: Double, private val lng: Double) {

    fun getCityName():String {
        try {
            val geocoder = Geocoder(application.applicationContext, Locale.getDefault())
            val adresses = geocoder.getFromLocation(ltd, lng, 1)

            adresses?.let {
                return adresses[0].locality
            }
        }
        catch (e: Exception) {
            return ""
        }

        return ""

    }
}