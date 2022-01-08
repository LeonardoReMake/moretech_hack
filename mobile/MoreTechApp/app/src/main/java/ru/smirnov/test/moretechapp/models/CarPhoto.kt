package ru.smirnov.test.moretechapp.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class CarPhoto(
    val type: String,
    val link: String
)