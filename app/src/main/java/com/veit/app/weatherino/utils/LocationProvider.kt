package com.veit.app.weatherino.utils

import com.veit.app.weatherino.api.Coord

interface LocationProvider {
    fun getLocation(): Coord
}

class LocationProviderImpl: LocationProvider {
    override fun getLocation(): Coord {
        return Coord(53.449051, 14.509188)
    }
}