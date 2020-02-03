package com.education.cocktails.util

import androidx.room.TypeConverter
import com.education.cocktails.model.Cocktail
import com.google.gson.*
import com.google.gson.reflect.TypeToken

var customPolicy =
    FieldNamingStrategy { field ->
        changeFieldName(field.name)
    }

fun changeFieldName(fieldName: String): String? =
    if(fieldName.contains("[1-9]".toRegex())) {
        val str = StringBuilder()
        str.append("str").append(fieldName[0].toUpperCase()).append(fieldName.substring(1))

        str.toString()
    }
    else
        fieldName


val jsonDeserializer =
    JsonDeserializer<Cocktail> { json, _, _ ->
        val jsonObject = json.asJsonObject
        val idDrink = jsonObject.get("idDrink")
        val drink = jsonObject.get("strDrink")

        val image = jsonObject.customGet("strDrinkThumb")

        val drinkAlternate = jsonObject.customGet("strDrinkAlternate")
        val video = jsonObject.customGet("strVideo")
        val category = jsonObject.customGet("strCategory")
        val alcoholic = jsonObject.customGet("strAlcoholic")
        val instructions = jsonObject.customGet("strInstructions")

        val mutableList = mutableListOf<Pair<String, String>>()

        if (jsonObject.has("strIngredient1"))
            for (it in (1..15)) {
                val ingr = jsonObject.get("strIngredient$it")
                val meas = jsonObject.get("strMeasure$it")
                if (ingr.isJsonNull)
                    break

                if (meas.isJsonNull)
                    mutableList.add(Pair(ingr.asString, ""))
                else
                    mutableList.add(Pair(ingr.asString, meas.asString))
            }

        Cocktail(
            idDrink.asLong,
            drink.asString,
            drinkAlternate?.asString,
            video?.asString,
            category?.asString,
            alcoholic?.asString,
            instructions?.asString,
            image?.asString,
            false,
            mutableList)
    }

private fun JsonObject.customGet(memberName: String): JsonElement? =
    if (this.has(memberName))
        if (this.get(memberName).isJsonNull)
            null
        else
            this.get(memberName)
    else
        null


class Converters {

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