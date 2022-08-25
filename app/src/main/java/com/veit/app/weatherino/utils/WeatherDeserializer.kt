package com.veit.app.weatherino.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.veit.app.weatherino.api.Weather
import java.lang.reflect.Type

/**
 * API returns Weather object as array with one element, so this deserializer converts it to single Weather object.
 */
class WeatherDeserializer: JsonDeserializer<Weather> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Weather {
        return with(json.asJsonArray[0].asJsonObject) {
            Weather(
                get("description").asString,
                get("icon").asString,
                get("id").asInt,
                get("main").asString
            )
        }
    }
}