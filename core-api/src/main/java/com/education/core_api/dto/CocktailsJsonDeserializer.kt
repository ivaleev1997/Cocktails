package com.education.core_api.dto

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

object CocktailsJsonDeserializer : JsonDeserializer<Cocktail> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Cocktail? {
        val jsonObject = json?.asJsonObject
        var result: Cocktail? = null
        if (jsonObject != null) {
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
                    if (ingr.isJsonNull || ingr.asString.isEmpty())
                        break

                    if (meas.isJsonNull)
                        mutableList.add(Pair(ingr.asString, ""))
                    else
                        mutableList.add(Pair(ingr.asString, meas.asString))
                }

            result = Cocktail(
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

        return result
    }

    private fun JsonObject.customGet(memberName: String): JsonElement? =
        if (this.has(memberName))
            if (this.get(memberName).isJsonNull)
                null
            else
                this.get(memberName)
        else
            null
}