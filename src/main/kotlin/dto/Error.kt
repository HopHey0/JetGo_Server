package com.hophey.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse (
    val error: String,
    val code: Int? = null
)