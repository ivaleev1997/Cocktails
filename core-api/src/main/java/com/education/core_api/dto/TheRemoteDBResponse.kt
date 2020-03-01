package com.education.core_api.dto

data class TheRemoteDBResponse<T> (
    val drinks: List<T>
)