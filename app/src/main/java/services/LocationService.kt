package services

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import com.google.android.gms.location.*
import models.LocationData

class LocationService(private val context: Context, private val onSuccessCallback: (LocationData)->Unit, private val onFailureCallback: ()->Unit): LiveData<LocationData>() {

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest: LocationRequest = LocationRequest.create().apply {
       /* interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY*/

    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }

        override fun onLocationAvailability(p0: LocationAvailability?) {
         //   if (p0 != null) {
         //       if (!(p0.isLocationAvailable))
                    onFailureCallback()
           // }
        }
    }

    init {
        startLocationUpdates()
    }

    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }



    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.also {
                    setLocationData(it)
                }
            }
        startLocationUpdates()
    }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private fun setLocationData(location:Location) {
        onSuccessCallback(LocationData(lng = location.longitude, ltd = location.latitude))
        value = LocationData(lng = location.longitude, ltd = location.latitude)
    }



}