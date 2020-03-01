package com.education.core_api.dto

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class IngredientsConverter {

    val gson = Gson()

    @TypeConverter
    fun listToJson(value: List<Pair<String, String>>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun jsonToMap(value: String): List<Pair<String, String>> {
        val mapType = object : TypeToken<List<Pair<String, String>>>() {}.type

        return gson.fromJson(value, mapType)
    }
}