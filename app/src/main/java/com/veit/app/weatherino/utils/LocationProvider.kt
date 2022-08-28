package com.veit.app.weatherino.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.*
import com.veit.app.weatherino.api.Coord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow


interface LocationProvider {
    val locationAvailableFlow: Flow<Boolean>
    fun getLocation(): Coord
}

@SuppressLint("MissingPermission") // we should have permission at this point
class LocationProviderImpl(private val context: Context): LocationProvider {
    private val _locationAvailableFlow = MutableStateFlow(false)
    override val locationAvailableFlow: Flow<Boolean> = _locationAvailableFlow

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private var cachedLocation: Coord? = null

    init {
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if(it != null) {
                Log.i(LOG_TAG, "Location received")
                cacheLocation(it)
            } else {
                Log.i(LOG_TAG, "Location not received")
                requestLocationUpdates() // trying to request location
            }
        }
    }

    private fun cacheLocation(location: Location) {
        cachedLocation = Coord(location.latitude, location.longitude)
        _locationAvailableFlow.value = true
    }

    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 1000
            priority = Priority.PRIORITY_LOW_POWER
        }
        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let {
                    Log.i(LOG_TAG, "Requested location received")
                    cacheLocation(it)
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                super.onLocationAvailability(locationAvailability)
                Log.i(LOG_TAG, "Location availability: $locationAvailability")
            }
        }
        LocationServices.getFusedLocationProviderClient(context)
            .requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun getLocation(): Coord {
        return cachedLocation
            ?: throw Exception("Location not available, make sure to check it before making any API calls!")
    }

    companion object {
        private const val LOG_TAG = "LocationProvider"
    }
}