package com.veit.app.weatherino.utils

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.veit.app.weatherino.data.TempData

class TempDataAdapter: TypeAdapter<TempData>() {
    override fun write(out: JsonWriter, value: TempData?) {
        out.value(value?.value)
    }

    override fun read(reader: JsonReader): TempData {
        return with(reader) {
            nextDouble().let {
                TempData(it, Utils.toReadableTemperature(it))
            }
        }
    }
}