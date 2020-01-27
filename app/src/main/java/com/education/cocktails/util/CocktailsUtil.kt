package com.education.cocktails.util

import com.google.gson.FieldNamingStrategy

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

